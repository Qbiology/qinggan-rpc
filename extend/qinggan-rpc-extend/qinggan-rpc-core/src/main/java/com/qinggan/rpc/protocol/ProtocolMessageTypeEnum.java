package com.qinggan.rpc.protocol;

import lombok.Getter;

import java.util.Arrays;

/**
 * Description: 协议请求类型的枚举
 * Author: 1401687501x's
 * Date: 2024/9/13 17:28
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);

    private final int key;

    ProtocolMessageTypeEnum(int key){
        this.key = key;
    }

    public static ProtocolMessageTypeEnum getEnumByKey(int key){
        return Arrays.stream(values()).filter(item->item.getKey()==key).findFirst().orElse(null);
    }
}
