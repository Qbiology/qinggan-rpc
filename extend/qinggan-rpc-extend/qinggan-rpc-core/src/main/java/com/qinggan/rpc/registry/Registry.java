package com.qinggan.rpc.registry;

import com.qinggan.rpc.config.RegistryConfig;
import com.qinggan.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * Description: 注册中心接口
 * Author: 1401687501x's
 * Date: 2024/9/11 15:16
 */
public interface Registry {

    /**
     * 初始化服务注册中心
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务发现，获取某个服务的所有节点
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();
}
