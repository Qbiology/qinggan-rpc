package com.qinggan.consumer;


import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.utils.ConfigUtils;

/**
 * Description: 简单消费者示例
 * Author: 1401687501x's
 * Date: 2024/9/8 15:00
 */
public class EasyConsumer {
    public static void main(String[] args) {

        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}
