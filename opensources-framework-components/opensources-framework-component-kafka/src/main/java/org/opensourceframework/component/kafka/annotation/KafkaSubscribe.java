package org.opensourceframework.component.kafka.annotation;

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
public @interface KafkaSubscribe {
    String topic() default "opensourceframework_COMMON_TOPIC";

    String pattern() default "";

    String consumer() default "GID_opensourceframework_COMMON";
}
