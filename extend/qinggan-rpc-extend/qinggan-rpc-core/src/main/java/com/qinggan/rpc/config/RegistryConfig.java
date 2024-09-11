package com.qinggan.rpc.config;

import com.qinggan.rpc.registry.RegistryKeys;
import lombok.Data;

/**
 * Description: 注册中心配置
 * Author: 1401687501x's
 * Date: 2024/9/11 15:09
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    private String username;

    private String password;

    /**
     * 连接超时时间（单位毫秒）
     */
    private Long timeout = 10000L;
}
