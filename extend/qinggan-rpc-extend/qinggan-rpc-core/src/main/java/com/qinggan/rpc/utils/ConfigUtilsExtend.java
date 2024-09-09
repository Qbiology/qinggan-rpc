package com.qinggan.rpc.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.qinggan.rpc.config.RpcConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Description: 扩展版
 * Author: 1401687501x's
 * Date: 2024/9/9 20:22
 */
public class ConfigUtilsExtend {

    private static final String YAML_EXTENSION = "yaml";
    private static final String PROPERTIES_EXTENSION = "properties";

    public static <T> T loadConfig(Class<T> tClass, String prefix, String preferredExt) throws InstantiationException, IllegalAccessException {
        return loadConfig(tClass, prefix, "",preferredExt);
    }

    public static <T> T loadConfig(Class<T> tClass, String prefix) throws InstantiationException, IllegalAccessException {
        return loadConfig(tClass, prefix, "",YAML_EXTENSION);
    }

    /**
     * 加载配置对象
     *
     * @param tClass       目标类
     * @param prefix       属性前缀
     * @param environment  环境变量
     * @param preferredExt 优先使用的文件扩展名（默认为空字符串，表示按默认顺序加载）
     * @return 配置对象
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment, String preferredExt) throws InstantiationException, IllegalAccessException {

        String[] extensions = {preferredExt, YAML_EXTENSION, PROPERTIES_EXTENSION};
        for (String ext : extensions) {
            if (StrUtil.isBlank(ext)) continue; // 如果是空字符串，则跳过
            String configFileName = buildConfigFileName(environment, ext);
            T result = loadFromExtension(tClass, configFileName, prefix, ext);
            if (result != null) {
                return result;
            }
        }

        throw new IllegalArgumentException("No valid configuration file found.");
    }

    private static String buildConfigFileName(String environment, String extension) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".").append(extension);
        return configFileBuilder.toString();
    }

    private static <T> T loadFromExtension(Class<T> tClass, String configFileName, String prefix, String extension) throws InstantiationException, IllegalAccessException {
        if (extension.equals(YAML_EXTENSION)) {
            return (T) loadFromYaml(configFileName, prefix);
        } else if (extension.equals(PROPERTIES_EXTENSION)) {
            return loadFromProperties(configFileName, tClass, prefix);
        }

        return null;
    }

    private static RpcConfig loadFromYaml(String configFileName, String prefix) throws InstantiationException, IllegalAccessException {
        InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(configFileName);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = yaml.load(inputStream);
        Map<String, Object> rpcMap = (Map<String, Object>) yamlMap.getOrDefault(prefix, new HashMap<>());

        RpcConfig rpcConfig = BeanUtil.fillBeanWithMap(rpcMap, new RpcConfig(), false);
        return rpcConfig;
    }

    private static <T> T loadFromProperties(String configFileName, Class<T> tClass, String prefix) {
        Props props = new Props(configFileName);
        return props.toBean(tClass,prefix);
    }
}
