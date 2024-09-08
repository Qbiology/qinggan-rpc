package com.qinggan.rpc.registry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 本地服务注册器
 * 作用：根据服务名获取到对应的实现类
 * Author: 1401687501x's
 * Date: 2024/9/8 16:28
 */
public class LocalRegistry {
    private static final ConcurrentHashMap<String,Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param key
     * @param implClass
     */
    public static void registry(String key,Class<?> implClass){
        map.put(key,implClass);
    }

    /**
     * 根据服务名获取对应实现类
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName){
       return map.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName
     */
    public static void remove(String serviceName){
        map.remove(serviceName);
    }
}
