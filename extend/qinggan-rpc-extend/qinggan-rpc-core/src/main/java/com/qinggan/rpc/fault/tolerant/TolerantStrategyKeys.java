package com.qinggan.rpc.fault.tolerant;

/**
 * Description: 容错策略键名常量
 * Author: 1401687501x's
 * Date: 2024/9/17 21:53
 */
public interface TolerantStrategyKeys {

    /**
     * 故障恢复
     */
    String FAIL_BACK = "failBack";

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";
}
