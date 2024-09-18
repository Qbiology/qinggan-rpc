package com.qinggan.rpc.fault.tolerant;

import com.qinggan.rpc.spi.SpiLoader;

/**
 * Description: 容错策略工厂
 * Author: 1401687501x's
 * Date: 2024/9/17 21:54
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    private static final TolerantStrategy DEFAULT_RETRY_STRATEGY = new FailFastTolerantStrategy();

    public static TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class,key);
    }
}
