package com.qinggan.rpc.protocol;

import com.qinggan.rpc.serializer.Serializer;
import com.qinggan.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * Description: 编码器
 * Author: 1401687501x's
 * Date: 2024/9/13 19:53
 */
public class ProtocolMessageEncoder {

    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if(protocolMessage==null || protocolMessage.getHeader()==null){
            return Buffer.buffer();
        }

        Buffer buffer = Buffer.buffer();
        ProtocolMessage.Header header = protocolMessage.getHeader();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        ProtocolMessageSerializerEnum enumByKey = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(enumByKey==null){
            throw new RuntimeException("序列化协议不存在");
        }

        Serializer serializer = SerializerFactory.getInstance(enumByKey.getValue());
        byte[] bodyBytes = serializer.serialize(protocolMessage.getBody());

        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;
    }
}
