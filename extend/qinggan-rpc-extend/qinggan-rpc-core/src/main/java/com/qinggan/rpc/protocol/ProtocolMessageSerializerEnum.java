package com.qinggan.rpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 协议序列化器的枚举
 * Author: 1401687501x's
 * Date: 2024/9/13 17:13
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(1,"jdk"),
    JSON(2,"json"),
    HESSIAN(3,"hessian"),
    KRYO(4,"kryo");

    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key, String value){
        this.key = key;
        this.value = value;
    }

    public static List<String> getValues(){
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

    public static ProtocolMessageSerializerEnum getEnumByKey(int key){
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if(anEnum.key==key){
                return anEnum;
            }
        }
        return null;
    }

    public static ProtocolMessageSerializerEnum getEnumByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }

        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if(anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}
