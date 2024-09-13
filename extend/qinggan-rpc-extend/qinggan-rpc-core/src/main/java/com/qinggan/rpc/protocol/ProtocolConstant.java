package com.qinggan.rpc.protocol;

/**
 * Description: 协议常量
 * Author: 1401687501x's
 * Date: 2024/9/13 17:08
 */
public interface ProtocolConstant {

    byte PROTOCOL_MAGIC = 0x1;

    byte PROTOCOL_VERSION = 0x1;

    int MESSAGE_HEADER_LENGTH = 17;
}
