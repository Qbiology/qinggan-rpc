package com.qinggan.rpc.fault.tolerant;

import com.qinggan.rpc.model.RpcResponse;

import java.util.Map;

/**
 * Description: 快速失败
 * Author: 1401687501x's
 * Date: 2024/9/17 21:49
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}
