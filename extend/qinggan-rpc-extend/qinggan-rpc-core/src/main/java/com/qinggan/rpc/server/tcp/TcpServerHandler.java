package com.qinggan.rpc.server.tcp;

import com.qinggan.rpc.model.RpcRequest;
import com.qinggan.rpc.model.RpcResponse;
import com.qinggan.rpc.protocol.ProtocolMessage;
import com.qinggan.rpc.protocol.ProtocolMessageDecoder;
import com.qinggan.rpc.protocol.ProtocolMessageEncoder;
import com.qinggan.rpc.protocol.ProtocolMessageTypeEnum;
import com.qinggan.rpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Description: TCP请求处理器
 * Author: 1401687501x's
 * Date: 2024/9/13 20:33
 */
public class TcpServerHandler implements Handler<NetSocket> {

    @Override
    public void handle(NetSocket socket) {
        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误");
            }
            RpcRequest rpcRequest = protocolMessage.getBody();

            RpcResponse rpcResponse = new RpcResponse();
            try {
                Class<?> implServiceClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implServiceClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implServiceClass.newInstance(), rpcRequest.getArgs());
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header,rpcResponse);
            try {
                Buffer responseBuffer = ProtocolMessageEncoder.encode(responseProtocolMessage);
                socket.write(responseBuffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }
        });
        socket.handler(bufferHandlerWrapper);
    }
}
