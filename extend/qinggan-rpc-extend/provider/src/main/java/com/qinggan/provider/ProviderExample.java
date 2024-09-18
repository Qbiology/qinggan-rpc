package com.qinggan.provider;

import com.qinggan.common.service.UserService;
import com.qinggan.rpc.bootstrap.ProviderBootstrap;
import com.qinggan.rpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 使用ProviderBootStrap启动服务提供
 * Author: 1401687501x's
 * Date: 2024/9/18 22:28
 */
public class ProviderExample {

    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
