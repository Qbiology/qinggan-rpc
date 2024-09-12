package com.qinggan.rpc.registry;

import com.qinggan.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 注册中心服务本地缓存
 * Author: 1401687501x's
 * Date: 2024/9/12 16:00
 */
public class RegistryServiceCache {

    private Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    public List<ServiceMetaInfo> getCache(String serviceKey){
        return serviceCache.get(serviceKey);
    }

    public void putCache(String serviceKey, List<ServiceMetaInfo> serviceMetaInfoList){
        serviceCache.put(serviceKey,serviceMetaInfoList);
    }

    public void clearCache(String serviceKey){
        serviceCache.remove(serviceKey);
    }
}
