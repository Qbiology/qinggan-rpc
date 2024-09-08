package com.qinggan.server;

import io.vertx.core.Vertx;

/**
 * Description: 基于Vert.x实现的web服务器，能够监听指定端口并处理请求
 * Author: 1401687501x's
 * Date: 2024/9/8 15:47
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        //创建vertx实例
        Vertx vertx = Vertx.vertx();

        //创建Http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //处理请求
        server.requestHandler(request -> {
            //处理Http请求
            System.out.println("Receive request:"+request.method()+" "+request.uri());

            //发送Http响应
            request.response()
                    .putHeader("context-type","text/plain")
                    .end("Hello from Vertx Http server!");
        });

        //监听端口
        server.listen(port,result -> {
            if(result.succeeded()){
                System.out.println("Server is now listening on port:"+port);
            }else {
                System.out.println("Failed to start server:"+result.cause());
            }
        });
    }
}
