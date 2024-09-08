package com.qinggan.rpc.model;

/**
 * Description: Rpc请求
 * Author: 1401687501x's
 * Date: 2024/9/8 18:39
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest {

    //服务名称
    private String serviceName;

    //方法名称
    private String methodName;

    //方法参数类型列表
    private Class<?>[] parameterTypes;

    //方法参数列表
    private Object[] args;
}
