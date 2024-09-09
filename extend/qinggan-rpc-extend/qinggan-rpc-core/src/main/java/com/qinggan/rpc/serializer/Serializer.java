package com.qinggan.rpc.serializer;

import java.io.IOException;

/**
 * Description: 序列化器接口
 * Author: 1401687501x's
 * Date: 2024/9/8 16:44
 */
public interface Serializer {

    /**
     * 序列化
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serializer(T object) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T deserializer(byte[] bytes, Class<T> type) throws IOException;
}
