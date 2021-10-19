package org.opensourceframework.component.mq.constant;

/**
 * 消费模式枚举
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum TunnelTypeEnum {
    SINGLE("single" , "消费一次消息"),
    PUBLIC("public" , "可消费多次消息(集群消息,广播消息)");

    private final String type;
    private final String name;

    TunnelTypeEnum(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }
}
