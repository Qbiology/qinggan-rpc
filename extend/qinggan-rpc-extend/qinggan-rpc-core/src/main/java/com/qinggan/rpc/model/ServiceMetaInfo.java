package com.qinggan.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * Description: 服务注册信息
 * Author: 1401687501x's
 * Date: 2024/9/11 14:53
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String  serviceVersion = "1.0";

    /**
     * 服务端口
     */
    private Integer servicePort;

    /**
     * 服务主机
     */
    private String serviceHost;

    /**
     * 服务分组
     */
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     * @return
     */
    public String getServiceKey(){
        //含服务分组
        //return String.format("%s:%s:%s",serviceName, serviceVersion, serviceGroup);
        return String.format("%s:%s",serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     * @return
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s",getServiceKey(),serviceHost,servicePort);
    }

    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
