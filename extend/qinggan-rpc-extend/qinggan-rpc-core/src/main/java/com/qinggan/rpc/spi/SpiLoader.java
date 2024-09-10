package com.qinggan.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.qinggan.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: spi加载器
 * Author: 1401687501x's
 * Date: 2024/9/10 22:02
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类，存放spi接口名与其实现类们的映射
     */
    private static Map<String,Map<String,Class<?>>> loadMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存,类路径=>实例
     */
    private static Map<String,Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统SPI目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义SPI目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * SPI扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_CUSTOM_SPI_DIR,RPC_SYSTEM_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载loadClass的所有实现类
     * @param loadClass 要加载的接口
     * @return
     */
    public static Map<String,Class<?>> load(Class<?> loadClass){
        String loadClassName = loadClass.getName();
        log.info("加载类型为{}的SPI",loadClassName);
        Map<String,Class<?>> loadClassMap = new HashMap<>();
        for(String scan_dir : SCAN_DIRS){
            List<URL> resources = ResourceUtil.getResources(scan_dir+loadClassName);//"META-INF/rpc/custom/com.qinggan.rpc.serializer.Serializer"
            for(URL resource : resources){
                try {
                    InputStreamReader reader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String nextLine;
                    while ((nextLine = bufferedReader.readLine()) != null){
                        String[] strArray = nextLine.split("=");
                        if(strArray.length>1){
                            loadClassMap.put(strArray[0], Class.forName(strArray[1]));
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    log.error("SPI resource load failed...");
                }
            }
        }
        loadMap.put(loadClassName,loadClassMap);
        return loadClassMap;
    }

    /**
     * 获取某个接口对应类的实例
     * @param tClass 接口类
     * @param key 实现类名
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<?> tClass, String key){
        String tClassName = tClass.getName();
        Map<String, Class<?>> classMap = loadMap.get(tClassName);
        if(classMap==null){
            throw new RuntimeException(String.format("SpiLoader未加载 %s 类型",tClassName));
        }
        if(!classMap.containsKey(key)){
            throw new RuntimeException(String.format("SpiLoader的 %s 不存在 key=%s 的类型",tClassName,key));
        }

        Class<?> implClass = classMap.get(key);
        String implClassName = implClass.getName();
        if(instanceCache.containsKey(implClassName)){
           return (T) instanceCache.get(implClassName);
        }else {
            try {
                instanceCache.put(implClassName,implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("{}类实例化失败",implClassName);
                throw new RuntimeException(e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }
}
