package com.qinggan.rpc.fault.tolerant;

import com.qinggan.rpc.model.RpcResponse;

import java.util.Map;

/**
 * Description: 容错机制接口
 * Author: 1401687501x's
 * Date: 2024/9/17 21:46
 */
public interface TolerantStrategy {

    RpcResponse doTolerant(Map<String,Object> context, Exception e);
}
