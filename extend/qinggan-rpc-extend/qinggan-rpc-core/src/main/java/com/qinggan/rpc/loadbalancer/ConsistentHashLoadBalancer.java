package com.qinggan.rpc.loadbalancer;

import com.qinggan.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description: 一致性hash负载均衡器
 * Author: 1401687501x's
 * Date: 2024/9/15 21:49
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{

    private final TreeMap<Integer,ServiceMetaInfo> virtualNodes = new TreeMap<>();

    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList==null){
            return null;
        }

        int size = serviceMetaInfoList.size();
        if(size==1){
            return serviceMetaInfoList.get(0);
        }

        for(ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
            for(int i=0;i<VIRTUAL_NODE_NUM;++i){
                int hash = getHash(serviceMetaInfo.getServiceAddress()+"#"+i);
                virtualNodes.put(hash,serviceMetaInfo);
            }
        }

        int hash = getHash(requestParams);
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if(entry==null){
            entry = virtualNodes.firstEntry();
        }

        return entry.getValue();
    }

    private int getHash(Object key){
        return key.hashCode();
    }
}
