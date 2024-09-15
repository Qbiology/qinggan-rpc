package com.qinggan.rpc.loadbalancer;

import com.qinggan.rpc.spi.SpiLoader;

/**
 * Description:
 * Author: 1401687501x's
 * Date: 2024/9/15 22:07
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class,key);
    }
}
