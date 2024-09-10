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
        User newUser = userService.getUser(user);
        System.out.println(newUser.getName());
    }
}
