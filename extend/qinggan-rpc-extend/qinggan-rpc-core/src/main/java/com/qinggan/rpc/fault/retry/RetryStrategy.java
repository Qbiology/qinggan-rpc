package com.qinggan.rpc.fault.retry;

import com.qinggan.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * Description: 重试策略接口
 * Author: 1401687501x's
 * Date: 2024/9/16 20:47
 */
public interface RetryStrategy {

    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
