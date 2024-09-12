package com.qinggan.consumer;


import com.qinggan.common.model.User;
import com.qinggan.common.service.UserService;
import com.qinggan.rpc.RpcApplication;
import com.qinggan.rpc.config.RpcConfig;
import com.qinggan.rpc.proxy.ServiceProxyFactory;
import com.qinggan.rpc.utils.ConfigUtils;

/**
 * Description: 简单消费者示例
 * Author: 1401687501x's
 * Date: 2024/9/8 15:00
 */
public class EasyConsumer {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("qinggan");
        User newUser1 = userService.getUser(user);
        System.out.println(newUser1.getName()+"1");

        UserService userService1 = ServiceProxyFactory.getProxy(UserService.class);
        User newUser2 = userService1.getUser(user);
        System.out.println(newUser2.getName()+"2");

          try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        UserService userService2 = ServiceProxyFactory.getProxy(UserService.class);
        User newUser3 = userService2.getUser(user);
        System.out.println(newUser3.getName()+"3");
    }
}
