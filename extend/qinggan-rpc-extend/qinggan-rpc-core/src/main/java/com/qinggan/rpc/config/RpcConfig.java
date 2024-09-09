package com.qinggan.rpc.config;

import lombok.Data;

/**
 * Description: Rpc框架配置
 * Author: 1401687501x's
 * Date: 2024/9/9 17:15
 */
@Data
public class RpcConfig {
    private String name = "qingggan-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8080;
}
