package com.qinggan.examplespringbootconsumer;

import com.qinggan.common.model.User;
import com.qinggan.common.service.UserService;
import com.qinggan.qingganrpcspringbootstarter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("qinggan");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
