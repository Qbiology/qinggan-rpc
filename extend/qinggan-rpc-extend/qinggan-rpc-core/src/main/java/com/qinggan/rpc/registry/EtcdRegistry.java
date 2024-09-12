package com.qinggan.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RegistryConfig;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.constant.RpcConstant;
import com.qinggan.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Description: etcd服务注册器
 * Author: 1401687501x's
 * Date: 2024/9/11 14:15
 */
@Slf4j
public class EtcdRegistry implements Registry{

    private Client client;

    private KV kvClient;

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本机注册的节点key集合
     */
    private final Set<String> localRegistryNodeKeySet = new HashSet<>();

    /**
     * 注册中心服务本地缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    private final Set<String> watchingServiceKeySet = new ConcurrentHashSet();

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个 30 秒的租约
        long leaseId = leaseClient.grant(3000).get().getID();

        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        localRegistryNodeKeySet.add(registerKey);
    }


    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registryKey,StandardCharsets.UTF_8)).get();
        localRegistryNodeKeySet.remove(registryKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        if(registryServiceCache.getCache(serviceKey)!=null){
            log.info("获取到了"+serviceKey+"对应的缓存内容");
            return registryServiceCache.getCache(serviceKey);
        }

        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            log.info("尝试从注册中心获取"+serviceKey+"的服务列表");
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues= kvClient.get(
                    ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
            registryServiceCache.putCache(serviceKey,serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destroy() {

        for(String key : localRegistryNodeKeySet){
            try {
                kvClient.delete(ByteSequence.from(key,StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key+"节点下线失败",e);
            }
        }

        System.out.println("当前节点下线");
        if(client!=null){
            client.close();
        }
        if(kvClient!=null){
            kvClient.close();
        }
    }

    @Override
    public void heartBeat() {
        CronUtil.schedule("*/10 * * * * *", new Task(){
            @Override
            public void execute() {
                for(String localRegistryNodeKey : localRegistryNodeKeySet){
                    try {
                        List<KeyValue> kvs = kvClient.get(ByteSequence.from(localRegistryNodeKey, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        if (CollUtil.isEmpty(kvs)){
                            continue;
                        }
                        KeyValue keyValue = kvs.get(0);
                        String s = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(s,ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(localRegistryNodeKey+"续签失败",e);
                    }
                }
            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String registryKey) {
        Watch watchClient = client.getWatchClient();

        boolean isNew = watchingServiceKeySet.add(registryKey);
        if(isNew){
            watchClient.watch(ByteSequence.from(registryKey,StandardCharsets.UTF_8),response->{
                for(WatchEvent event : response.getEvents()){
                    switch (event.getEventType()){
                        case DELETE:
                            Pattern pattern = Pattern.compile("/rpc/(.*?)/");
                            Matcher matcher = pattern.matcher(registryKey);

                            String serviceKey = "";
                            if (matcher.find()) {
                                serviceKey = matcher.group(1);
                                System.out.println("服务键: " + serviceKey);
                            } else {
                                throw new RuntimeException("没有找到匹配的服务键");
                            }
                            registryServiceCache.clearCache(serviceKey);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
