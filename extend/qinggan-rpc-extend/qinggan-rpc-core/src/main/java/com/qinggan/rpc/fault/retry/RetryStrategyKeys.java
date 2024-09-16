package com.qinggan.rpc.fault.retry;

/**
 * Description: 重试策略键名常量
 * Author: 1401687501x's
 * Date: 2024/9/16 21:09
 */
public interface RetryStrategyKeys {

    String NO = "no";

    String FIXED_INTERVAL = "fixedInterval";

    String EXPONENTIAL_BACKOFF = "ExponentialBackoff";
}
