package org.opensourceframework.starter.ratelimiter.annotation;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/2 上午9:26
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AccessLimit {
	/**
	 * 限制次数
	 *
	 * @return
	 */
	int limit() default 0;

	/**
	 * 时间间隔 单位秒
	 *
	 * @return
	 */
	int interval() default 1;
}
