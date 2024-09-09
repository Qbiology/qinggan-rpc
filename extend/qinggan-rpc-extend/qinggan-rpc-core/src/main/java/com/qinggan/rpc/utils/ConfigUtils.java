package com.qinggan.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * Description: 配置工具类
 * Author: 1401687501x's
 * Date: 2024/9/9 17:23
 */
public class ConfigUtils {

    public static <T> T loadConfig(Class<T> tClass, String prefix){
        return loadConfig(tClass,prefix,"");
    }

    /**
     * 加载配置对象
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment){
        StringBuilder configFileBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass,prefix);
    }
}
