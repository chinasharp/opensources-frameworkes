package org.opensourceframework.netty.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * 消费者注解
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 上午10:23
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcConsumer {

}
