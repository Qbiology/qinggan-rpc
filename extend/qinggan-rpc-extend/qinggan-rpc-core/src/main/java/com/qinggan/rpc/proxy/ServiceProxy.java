package com.qinggan.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RegistryConfig;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.constant.RpcConstant;
import com.qinggan.rpc.model.RpcRequest;
import com.qinggan.rpc.model.RpcResponse;
import com.qinggan.rpc.model.ServiceMetaInfo;
import com.qinggan.rpc.protocol.*;
import com.qinggan.rpc.registry.Registry;
import com.qinggan.rpc.registry.RegistryFactory;
import com.qinggan.rpc.serializer.Serializer;
import com.qinggan.rpc.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Description: 动态代理
 * Author: 1401687501x's
 * Date: 2024/9/8 21:22
 */
public class ServiceProxy implements InvocationHandler {

    //调用代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        String serviceName = method.getDeclaringClass().getName();
        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] bytes = serializer.serialize(rpcRequest);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo selectServiceMetaInfo = serviceMetaInfoList.get(0);

            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectServiceMetaInfo.getServicePort(),selectServiceMetaInfo.getServiceAddress(),
                    result->{
                        if(result.succeeded()){
                            io.vertx.core.net.NetSocket socket = result.result();

                            ProtocolMessage<RpcRequest> requestProtocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = requestProtocolMessage.getHeader();
                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(rpcConfig.getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            requestProtocolMessage.setHeader(header);
                            requestProtocolMessage.setBody(rpcRequest);

                            try {
                                Buffer buffer = ProtocolMessageEncoder.encode(requestProtocolMessage);
                                socket.write(buffer);
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息编码错误");
                            }

                            socket.handler(buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> responseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decoder(buffer);
                                    responseFuture.complete(responseProtocolMessage.getBody());
                                } catch (IOException e) {
                                    throw new RuntimeException("协议消息解码错误");
                                }
                            });
                        }else {
                            System.err.println("Failed to connect to TCP server");
                        }
                    });
            RpcResponse rpcResponse = responseFuture.get();
            netClient.close();
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
