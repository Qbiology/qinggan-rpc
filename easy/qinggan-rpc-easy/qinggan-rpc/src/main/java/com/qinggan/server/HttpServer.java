package com.qinggan.server;

/**
 * Description: Http服务接口
 * Author: 1401687501x's
 * Date: 2024/9/8 15:45
 */
public interface HttpServer {

    /**
     * 启动服务器
     * @param port
     */
    void doStart(int port);
}
