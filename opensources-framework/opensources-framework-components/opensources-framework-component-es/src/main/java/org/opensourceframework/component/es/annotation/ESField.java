package org.opensourceframework.component.es.annotation;

import java.lang.annotation.*;

/**
 * 字段注解
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ESField {
	String column() default "";

	String type() default "";

	String analyzer() default "";

	String search_analyzer() default "";

	String format() default "";

	/**
	 *
	 *
	 * @return
	 */
	int scalingFactor() default 1000;
}
