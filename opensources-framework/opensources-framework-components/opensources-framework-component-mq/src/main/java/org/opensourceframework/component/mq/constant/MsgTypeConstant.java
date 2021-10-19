package org.opensourceframework.component.mq.constant;

/**
 * 消息类型
 *
 * @author yuce
 * */
public class MsgTypeConstant {
    /**
     * 一般消息
     */
    public static final int CONCURRENTLY = 1;

    /**
     * 有序消息
     */
    public static final int ORDERLY = 2;

    /**
     * 事务消息
     */
    public static final int TRANSACTION = 3;
}
