package org.opensourceframework.component.mq.constant;

/**
 * 应答模式
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum AckTypeEnum {
    NO_ACK("0", "无需应答(消息异步))"),
    LEADER_ACK("1", "leader应答(同步消息，主节点应答)"),
    ALL_ACK("-1", "全部应答(同步消息，主从节点都应答)");

    private final String ack;
    private final String description;

    AckTypeEnum(String ack, String description) {
        this.ack = ack;
        this.description = description;
    }

    public String getAck() {
        return this.ack;
    }

    public String getDescription() {
        return this.description;
    }
}
