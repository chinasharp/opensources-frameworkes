package org.opensourceframework.component.mq.annotation;

import java.lang.annotation.*;

/**
 * 消息订阅注解
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQSubscribe {
    String topic() default "opensourceframework_COMMON_TOPIC";

    String consumer() default "GID_opensourceframework_COMMON";

    String tag() default "opensourceframework_COMMON_TAG";

    /**
     * 默认为一般消息 MessageEnum
     *
     * @return
     */
    int messageType() default 1;


}
