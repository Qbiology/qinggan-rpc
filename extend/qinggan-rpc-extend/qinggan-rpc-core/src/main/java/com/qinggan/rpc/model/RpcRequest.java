package com.qinggan.rpc.model;

/**
 * Description: Rpc请求
 * Author: 1401687501x's
 * Date: 2024/9/8 18:39
 */

import com.qinggan.rpc.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    //服务名称
    private String serviceName;

    //方法名称
    private String methodName;

    //方法参数类型列表
    private Class<?>[] parameterTypes;

    //方法参数列表
    private Object[] args;

    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
}
