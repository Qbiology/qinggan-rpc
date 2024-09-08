package com.qinggan.provider;

import com.qinggan.server.VertxHttpServer;

/**
 * Description: 简易服务提供者示例
 * Author: 1401687501x's
 * Date: 2024/9/8 14:59
 */
public class EasyProvider {
    public static void main(String[] args) {
        VertxHttpServer server = new VertxHttpServer();

        server.doStart(8080);
    }
}
