package org.opensourceframework.component.mq.constant;

/**
 * 默认参数值
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 */
public class MQConstant {
    public static final String MQ_CONFIG_SPLIT_CHAR ="_";

    /**
     * 一般消息生产者默认的producerId
     */
    public static final String DEF_PRODUCER_ID = "PID_opensourceframework_BIZ";

    /**
     * 事务消息生产者默认的producerId
     */
    public static final String DEF_TRANSACTION_PRODUCER_ID = "PID_opensourceframework_BIZ_TRANS";
    public static final String PRODUCER_ID_PREFIX = "PID_";

    /**
     * 事务消息生产者Group ID的后缀
     */
    public static final String TRANSACTION_PRODUCER_SUFFIX = "TRANS";

    /**
     * Topic后缀
     */
    public static final String TOPIC_SUFFIX = "TOPIC";

    /**
     * 默认consumerId
     */
    public static final String DEF_CONSUMER_ID = "GID_opensourceframework_BIZ";

    /**
     * 默认消息topic
     */
    public static String DEF_TOPIC = "opensourceframework_COMMON_TOPIC";

    /**
     * 默认消息tag
     */
    public static final String DEF_TAG = "opensourceframework_COMMON_TAG";

    /**
     * 默认事务消息tag
     */
    public static final String DEF_TRANS_TAG = "opensourceframework_TRANS_TAG";

    public static final String COMMON_MSG_ID_PREFIX = "MSG_ID_";
    public static final String ORDER_MSG_ID_PREFIX = "ORDER_MSG_ID_";
    public static final String TRANS_MSG_ID_PREFIX = "TRAN_MSG_ID_";

    /**
     * 默认namespace
     */
    public static final String DEF_NAME_SPACE = "opensourceframework";

    public static final String ERROR = "error";

    /**
     * 消息发送默认超时时长
     */
    public static final Long DEF_SEND_TIME_OUT = 30000L;

    /**
     * 传递发送与接收log信息key
     */
    public static final String MQ_LOG_KEY = "mqLog";

    /**
     * 消费失败后，最多重新消费3次才投递到死信队列
     */
    public static final Integer DEF_MAX_RECONSUME_TIMES = 5;
}
