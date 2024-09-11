package com.qinggan.rpc;

import com.qinggan.rpc.config.RegistryConfig;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.constant.RpcConstant;
import com.qinggan.rpc.registry.Registry;
import com.qinggan.rpc.registry.RegistryFactory;
import com.qinggan.rpc.utils.ConfigUtilsExtend;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * RPC 框架应用
 * 相当于 holder，存放了项目全局用到的变量。双检锁单例模式实现
 * Author: 1401687501x's
 * Date: 2024/9/9 18:02
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}",rpcConfig.toString());

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
    }

    public static void init(String preferredExt){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtilsExtend.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX, preferredExt);
        }catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtilsExtend.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
