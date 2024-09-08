package com.qinggan.rpc.proxy;

import java.lang.reflect.Proxy;

/**
 * Description: 代理工厂
 * Author: 1401687501x's
 * Date: 2024/9/8 21:27
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()
        );
    }
}
