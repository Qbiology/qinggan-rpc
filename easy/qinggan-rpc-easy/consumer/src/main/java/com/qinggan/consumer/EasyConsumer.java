package com.qinggan.consumer;

import com.qinggan.common.model.User;
import com.qinggan.common.service.UserService;

/**
 * Description: 简单消费者示例
 * Author: 1401687501x's
 * Date: 2024/9/8 15:00
 */
public class EasyConsumer {
    public static void main(String[] args) {
        //需要获取到userService的实例对象
        UserService userService = null;

        User user = new User();
        user.setName("qinggan");

        User newUser = userService.getUser(user);
        if(newUser!=null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user==null");
        }
    }
}
