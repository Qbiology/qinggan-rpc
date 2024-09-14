package com.qinggan.rpc.protocol;

import com.qinggan.rpc.model.RpcRequest;
import com.qinggan.rpc.model.RpcResponse;
import com.qinggan.rpc.serializer.Serializer;
import com.qinggan.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * Description: 解码器
 * Author: 1401687501x's
 * Date: 2024/9/13 20:06
 */
public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();

        if(buffer.getByte(0)!=ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("消息 magic 非法");
        }

        header.setMagic(buffer.getByte(0));
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());

        ProtocolMessageSerializerEnum enumByKey = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(enumByKey==null){
            throw new RuntimeException("序列化消息的协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(enumByKey.getValue());

        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if(messageTypeEnum==null){
            throw new RuntimeException("序列化消息的类型不存在");
        }

        switch (messageTypeEnum){
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header,rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header,rpcResponse);
            case HEART_BEAT:
            case OTHER:
            default:
                throw new RuntimeException("暂不支持该消息类型");
        }
    }
}
