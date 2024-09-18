package com.qinggan.consumer;

import com.qinggan.common.model.User;
import com.qinggan.common.service.UserService;
import com.qinggan.rpc.bootstrap.ConsumerBootstrap;
import com.qinggan.rpc.proxy.ServiceProxyFactory;

public class ConsumerExample {

    public static void main(String[] args) {
        // 服务提供者初始化
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("qinggan");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
