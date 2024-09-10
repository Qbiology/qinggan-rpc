package com.qinggan.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.model.RpcRequest;
import com.qinggan.rpc.model.RpcResponse;
import com.qinggan.rpc.serializer.JdkSerializer;
import com.qinggan.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description: 动态代理
 * Author: 1401687501x's
 * Date: 2024/9/8 21:22
 */
public class ServiceProxy implements InvocationHandler {

    //调用代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化器
        Serializer serializer = new JdkSerializer();

        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] bytes = serializer.serializer(rpcRequest);
            byte[] result;
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            String url = "http://"+rpcConfig.getServerHost()+":"+rpcConfig.getServerPort();//http://localhost:8087
            try(HttpResponse httpResponse = HttpRequest.post(url)
                    .body(bytes)
                    .execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserializer(result,RpcResponse.class);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
