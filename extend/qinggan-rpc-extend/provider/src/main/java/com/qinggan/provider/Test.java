package com.qinggan.provider;

import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RpcConfig;

/**
 * Description:
 * Author: 1401687501x's
 * Date: 2024/9/10 20:39
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);
        Thread.sleep(500000000);
    }
}
