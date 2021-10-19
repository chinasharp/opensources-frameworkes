package org.opensourceframework.component.es.annotation;

import java.lang.annotation.*;

/**
 * 文档注解
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ESDocument {
	/**
	 * @return
	 */
	String index() default "";

	/**
	 * @return
	 */
	String type() default "";

	/**
	 * 支持最大查询返回
	 *
	 * @return
	 */
	long maxResultWindow() default 50000L;

	/**
	 * 数据库字段是否为驼峰
	 *
	 * @return
	 */
	boolean isHumpCol() default true;

}
