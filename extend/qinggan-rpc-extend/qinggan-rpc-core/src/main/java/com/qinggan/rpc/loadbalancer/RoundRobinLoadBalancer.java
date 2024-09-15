package com.qinggan.rpc.loadbalancer;

import com.qinggan.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 轮询负载均衡器
 * Author: 1401687501x's
 * Date: 2024/9/15 21:41
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private final AtomicInteger currIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList==null){
            return null;
        }

        int size = serviceMetaInfoList.size();
        if(size==1){
            return serviceMetaInfoList.get(0);
        }

        return serviceMetaInfoList.get(currIndex.getAndIncrement() % size);
    }
}
