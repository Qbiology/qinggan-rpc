package com.qinggan.common.service;

import com.qinggan.common.model.User;

/**
 * Description: 用户服务接口
 * Author: 1401687501x's
 * Date: 2024/9/8 14:50
 */
public interface UserService {

    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);

    default short getNumber(){
        return 1;
    }
}
