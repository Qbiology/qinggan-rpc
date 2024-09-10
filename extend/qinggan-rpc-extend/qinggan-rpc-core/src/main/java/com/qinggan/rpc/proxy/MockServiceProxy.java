package com.qinggan.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description: Mock服务代理
 * Author: 1401687501x's
 * Date: 2024/9/10 15:17
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}",method);
        return getDefaultReturn(returnType);
    }

    private Object getDefaultReturn(Class<?> returnType) {
        //基础类型
        if(returnType.isPrimitive()){
            if(returnType==boolean.class){
                return false;
            }else if(returnType==short.class){
                return (short) 0;
            }else if(returnType==int.class){
                return 0;
            }else if(returnType==long.class){
                return 0L;
            }else if(returnType==char.class){
                return '\u0000' ;
            }else if(returnType==float.class){
                return 0.0f;
            }else if(returnType==double.class){
                return 0.0d;
            }else {
                return 0;
            }
        }
        return null;
    }
}
