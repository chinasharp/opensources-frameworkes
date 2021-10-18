package org.opensourceframework.component.dao.annotation;

import java.lang.annotation.*;

/**
 * 分表注解
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ShardingTable {
	/**
	 * 根据对象的那个属性进行hash分表
	 *
	 * @return
	 */
	String property() default "";

	/**
	 * 拆分键对应的数据库字段名
	 *
	 * @return
	 */
	String column() default "";

	/**
	 * 分表总数
	 *
	 * @return
	 */
	int tableCount() default 10;

	/**
	 * 分表算法  1:hash mod  2:hashing
	 *
	 * @return
	 */
	int algorithm() default 1;

	/**
	 * 没有分表建时指定分表序号的参数
	 *
	 * @return
	 */
	String tableNumParam() default "tableNumber";
}
