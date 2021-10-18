package org.opensourceframework.component.mq.constant;

/**
 * 消息发送方式
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum SendTypeEnum {
    SYNC("sync", "同步消息"),
    ASYNC("async", "异步消息");

    private final String syncFlag;
    private final String description;

    SendTypeEnum(String syncFlag, String description) {
        this.syncFlag = syncFlag;
        this.description = description;
    }

    public String getSyncFlag() {
        return this.syncFlag;
    }

    public String getDescription() {
        return this.description;
    }
}
