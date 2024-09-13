package com.qinggan.rpc.server.tcp;

import io.vertx.core.Vertx;

/**
 * Description: TCP客户端
 * Author: 1401687501x's
 * Date: 2024/9/13 19:40
 */
public class VertxTcpClient {

    public void start(){
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(8888,"localhost",result->{
           if(result.succeeded()){
               System.out.println("Connected to TCP server");
               io.vertx.core.net.NetSocket socket = result.result();
               socket.write("Hello,server");
               socket.handler(buffer -> {
                   System.out.println("Received response from server "+buffer.toString());
               });
           }else {
               System.err.println("Failed to Connect to TCP server");
           }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
