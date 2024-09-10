package com.qinggan.rpc.serializer;

import com.qinggan.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 序列化器工厂
 * Author: 1401687501x's
 * Date: 2024/9/10 19:47
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
