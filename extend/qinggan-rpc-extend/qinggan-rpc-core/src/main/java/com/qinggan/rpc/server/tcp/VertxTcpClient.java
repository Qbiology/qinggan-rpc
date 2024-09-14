package com.qinggan.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.model.RpcRequest;
import com.qinggan.rpc.model.RpcResponse;
import com.qinggan.rpc.model.ServiceMetaInfo;
import com.qinggan.rpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Description: TCP客户端
 * Author: 1401687501x's
 * Date: 2024/9/13 19:40
 */
public class VertxTcpClient {

    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo selectServiceMetaInfo) throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(selectServiceMetaInfo.getServicePort(),selectServiceMetaInfo.getServiceHost(),
                result->{
                    if(result.succeeded()){
                        NetSocket socket = result.result();

                        ProtocolMessage<RpcRequest> requestProtocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        header.setRequestId(IdUtil.getSnowflakeNextId());
                        requestProtocolMessage.setHeader(header);
                        requestProtocolMessage.setBody(rpcRequest);

                        try {
                            Buffer buffer = ProtocolMessageEncoder.encode(requestProtocolMessage);
                            socket.write(buffer);
                        } catch (IOException e) {
                            throw new RuntimeException("协议消息编码错误");
                        }

                        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
                                buffer -> {
                                    try {
                                        ProtocolMessage<RpcResponse> responseProtocolMessage =
                                                (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                        responseFuture.complete(responseProtocolMessage.getBody());
                                    } catch (IOException e) {
                                        throw new RuntimeException("协议消息解码错误");
                                    }
                                });
                        socket.handler(bufferHandlerWrapper);
                    }else {
                        System.err.println("Failed to connect to TCP server");
                    }
                });
        RpcResponse rpcResponse = responseFuture.get();
        netClient.close();
        return rpcResponse;
    }
}
