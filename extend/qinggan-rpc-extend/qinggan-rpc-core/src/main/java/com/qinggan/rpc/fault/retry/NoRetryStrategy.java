package com.qinggan.rpc.fault.retry;

import com.qinggan.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * Description: 不重试策略
 * Author: 1401687501x's
 * Date: 2024/9/16 20:49
 */
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
