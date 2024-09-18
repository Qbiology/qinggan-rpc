package com.qinggan.rpc.bootstrap;

import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RegistryConfig;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.model.ServiceMetaInfo;
import com.qinggan.rpc.model.ServiceRegisterInfo;
import com.qinggan.rpc.registry.LocalRegistry;
import com.qinggan.rpc.registry.Registry;
import com.qinggan.rpc.registry.RegistryFactory;
import com.qinggan.rpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * Description:服务提供者初始化
 * Author: 1401687501x's
 * Date: 2024/9/18 22:15
 */
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo> serviceRegisterInfoList){
        // RPC 框架初始化
        RpcApplication.init();

        //获取服务注册器
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        for(ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList){
            //本地注册
            String serviceName = serviceRegisterInfo.getServiceName();
            Class<?> implClass = serviceRegisterInfo.getImplClass();
            LocalRegistry.register(serviceName,implClass);

            // 注册服务到注册中心
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName+"服务注册失败",e);
            }
        }

        // 启动 web 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
