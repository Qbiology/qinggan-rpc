package com.qinggan.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: Rpc响应
 * Author: 1401687501x's
 * Date: 2024/9/8 18:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse {

    //响应数据
    private Object data;

    //响应类型
    private Class<?> dataType;

    //响应消息
    private String message;

    //异常信息
    private Exception exception;
}
