package com.qinggan.common.model;

import java.io.Serializable;

/**
 * Description: 用户类
 * Author: 1401687501x's
 * Date: 2024/9/8 14:48
 */
public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
