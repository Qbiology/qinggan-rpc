package com.qinggan.provider;

import com.qinggan.common.model.User;
import com.qinggan.common.service.UserService;

/**
 * Description: 用户服务实现类
 * Author: 1401687501x's
 * Date: 2024/9/8 14:57
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getName());
        return user;
    }
}
