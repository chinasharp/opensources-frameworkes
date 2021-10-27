package org.opensourceframework.netty.rpc.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 生产者注解
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/29 下午6:11
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface RpcProvider {
	String serviceId() default "";
}
