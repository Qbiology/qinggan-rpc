package com.qinggan.qingganrpcspringbootstarter.annotation;

import com.qinggan.qingganrpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.qinggan.qingganrpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.qinggan.qingganrpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 启动rpc注解
 * Author: 1401687501x's
 * Date: 2024/9/18 22:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动 server
     *
     * @return
     */
    boolean needServer() default true;
}

