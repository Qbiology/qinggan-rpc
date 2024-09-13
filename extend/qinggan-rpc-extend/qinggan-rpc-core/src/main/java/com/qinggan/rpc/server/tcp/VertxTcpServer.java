package com.qinggan.rpc.server.tcp;

import com.qinggan.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

/**
 * Description: TCP服务器
 * Author: 1401687501x's
 * Date: 2024/9/13 19:27
 */
public class VertxTcpServer implements HttpServer {

    /*private byte[] handleRequest(byte[] requestData){
        return "hello".getBytes();
    }*/
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        NetServer server = vertx.createNetServer();
        server.connectHandler(new TcpServerHandler());

        /*server.connectHandler(socket->{
           socket.handler(buffer -> {
               byte[] requestData = buffer.getBytes();
               byte[] responseData = handleRequest(requestData);
               socket.write(Buffer.buffer(responseData));
           });
        });*/

        server.listen(port,result->{
            if(result.succeeded()){
                System.out.println("TCP Server is now listening on port:"+port);
            }else {
                System.err.println("Failed to start TCP server:"+result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
