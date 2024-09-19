package com.qinggan.rpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

/**
 * Description: TCP服务器
 * Author: 1401687501x's
 * Date: 2024/9/13 19:27
 */
public class VertxTcpServer{

    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        NetServer server = vertx.createNetServer();
        server.connectHandler(new TcpServerHandler());

        server.listen(port,result->{
            if(result.succeeded()){
                System.out.println("TCP Server is now listening on port:"+port);
            }else {
                System.err.println("Failed to start TCP server:"+result.cause());
            }
        });
    }
}
