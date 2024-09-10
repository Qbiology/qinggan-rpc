package com.qinggan.provider;

import com.qinggan.common.service.UserService;
import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.registry.LocalRegistry;
import com.qinggan.rpc.server.VertxHttpServer;

/**
 * Description: 简易服务提供者示例
 * Author: 1401687501x's
 * Date: 2024/9/8 14:59
 */
public class EasyProvider {
    public static void main(String[] args) {

        RpcApplication.init("yaml");

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        VertxHttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
