package com.qinggan.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RegistryConfig;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.constant.RpcConstant;
import com.qinggan.rpc.fault.retry.RetryStrategy;
import com.qinggan.rpc.fault.retry.RetryStrategyFactory;
import com.qinggan.rpc.fault.tolerant.TolerantStrategy;
import com.qinggan.rpc.fault.tolerant.TolerantStrategyFactory;
import com.qinggan.rpc.loadbalancer.LoadBalancer;
import com.qinggan.rpc.loadbalancer.LoadBalancerFactory;
import com.qinggan.rpc.model.RpcRequest;
import com.qinggan.rpc.model.RpcResponse;
import com.qinggan.rpc.model.ServiceMetaInfo;
import com.qinggan.rpc.registry.Registry;
import com.qinggan.rpc.registry.RegistryFactory;
import com.qinggan.rpc.serializer.Serializer;
import com.qinggan.rpc.serializer.SerializerFactory;
import com.qinggan.rpc.server.tcp.VertxTcpClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description: 动态代理
 * Author: 1401687501x's
 * Date: 2024/9/8 21:22
 */
public class ServiceProxy implements InvocationHandler {

    //调用代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        String serviceName = method.getDeclaringClass().getName();
        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] bytes = serializer.serialize(rpcRequest);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }

            String loadBalancerName = rpcConfig.getLoadBalancer();
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(loadBalancerName);
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

            RpcResponse rpcResponse;
            try {
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(()->
                        VertxTcpClient.doRequest(rpcRequest, selectServiceMetaInfo)
                );
            }catch (Exception e){
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                rpcResponse = tolerantStrategy.doTolerant(null,e);
            }

            return rpcResponse.getData();
        } catch (IOException e) {
            throw new RuntimeException("调用失败");
        }
    }
}
