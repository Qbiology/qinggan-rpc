package com.qinggan.rpc.loadbalancer;

import com.qinggan.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * Description: 负载均衡器接口
 * Author: 1401687501x's
 * Date: 2024/9/15 21:39
 */
public interface LoadBalancer {

    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
