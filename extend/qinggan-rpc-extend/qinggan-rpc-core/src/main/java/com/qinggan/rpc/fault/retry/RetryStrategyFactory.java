package com.qinggan.rpc.fault.retry;

import com.qinggan.rpc.spi.SpiLoader;

/**
 * Description: 重试策略工厂
 * Author: 1401687501x's
 * Date: 2024/9/16 21:12
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }
}
