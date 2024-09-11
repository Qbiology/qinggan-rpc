package com.qinggan.consumer;

import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RegistryConfig;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.registry.Registry;
import com.qinggan.rpc.registry.RegistryFactory;

/**
 * Description:
 * Author: 1401687501x's
 * Date: 2024/9/10 20:39
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
    }
}
