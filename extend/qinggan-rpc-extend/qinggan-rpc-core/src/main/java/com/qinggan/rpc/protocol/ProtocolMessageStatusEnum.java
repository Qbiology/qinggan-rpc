package com.qinggan.rpc.protocol;

import lombok.Getter;

import java.util.Arrays;

/**
 * Description: 协议消息状态的枚举
 * Author: 1401687501x's
 * Date: 2024/9/13 17:39
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok",20),
    BAD_REQUEST("bad_request",40),
    BAD_RESPONSE("bad_response",50);

    private final String key;

    private final int value;

    ProtocolMessageStatusEnum(String key, int value){
        this.key = key;
        this.value = value;
    }

    public static ProtocolMessageStatusEnum getEnumByValue(int value){
        return Arrays.stream(values()).filter(item->item.value==value).findFirst().orElse(null);
    }
}
