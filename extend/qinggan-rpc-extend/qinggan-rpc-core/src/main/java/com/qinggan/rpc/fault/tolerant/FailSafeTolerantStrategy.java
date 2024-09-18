package com.qinggan.rpc.fault.tolerant;

import com.qinggan.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Description: 静默处理异常
 * Author: 1401687501x's
 * Date: 2024/9/17 21:50
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常",e);
        return new RpcResponse();
    }
}
