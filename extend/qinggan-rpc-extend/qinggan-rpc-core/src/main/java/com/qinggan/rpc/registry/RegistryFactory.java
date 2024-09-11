package com.qinggan.rpc.registry;

import com.qinggan.rpc.spi.SpiLoader;

/**
 * Description: 注册中心工厂
 * Author: 1401687501x's
 * Date: 2024/9/11 16:17
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
