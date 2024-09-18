package com.qinggan.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 服务注册信息
 * Author: 1401687501x's
 * Date: 2024/9/18 22:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegisterInfo<T> {

    private String serviceName;

    private Class<? extends T> implClass;
}
