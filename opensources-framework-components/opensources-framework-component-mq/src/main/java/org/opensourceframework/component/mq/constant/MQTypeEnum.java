package org.opensourceframework.component.mq.constant;

/**
 * MQ类型
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum MQTypeEnum {
    ROCKET("rocket" , 1),
    RABBIT("rabbit" ,2),
    KAFKA("kafka" ,3),
    ALIYUN("aliyun",4);


    private final String type;
    private final Integer code;


    MQTypeEnum(String type, Integer code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return this.type;
    }
}
