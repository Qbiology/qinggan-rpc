package com.qinggan.rpc.protocol;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 自定义消息结构
 * Author: 1401687501x's
 * Date: 2024/9/13 16:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolMessage<T> {

    private Header header;

    private T body;

    @Data
    public static class Header{
        /**
         * 魔术，保证安全性
         */
        private byte magic;

        /**
         * 协议版本
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 请求类型
         */
        private byte type;

        /**
         * 状态
         */
        private byte status;

        /**
         * 请求id，用于唯一标识
         */
        private long requestId;

        /**
         * 请求体长度
         */
        private int bodyLength;
    }
}
