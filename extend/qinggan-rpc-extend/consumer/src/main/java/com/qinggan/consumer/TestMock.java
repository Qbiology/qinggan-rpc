package com.qinggan.consumer;

import com.qinggan.common.model.User;
import com.qinggan.common.service.UserService;
import com.qinggan.rpc.proxy.ServiceProxyFactory;

/**
 * Description: 测试mock代理
 * Author: 1401687501x's
 * Date: 2024/9/10 15:36
 */
public class TestMock {
    public static void main(String[] args) {

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("qinggan");
        User newUser = userService.getUser(user);
        if(newUser!=null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user==null");
        }

        System.out.println(userService.getNumber());
    }
}
