package org.opensourceframework.component.idempotent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分表注解
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface IdempotentHandler {

	/**
	 * 方法参数名
	 *
	 * @return
	 */
	String parameter() default "";

	/**
	 * 参数对象的属性 如果参数对象为基本对象 不指定即可
	 *
	 * @return
	 */
	String properties() default "";

	/**
	 * 获取幂等锁等待时间 单位秒
	 */
	int waitTimeout() default 120;

	/**
	 * 自动释放幂等锁时间 单位秒
	 * @return
	 */
	int leaseTime() default 120;

	/**
	 * 幂等数据有效期 单位秒
	 *
	 * @return
	 */
	int validTime() default 300;
}
