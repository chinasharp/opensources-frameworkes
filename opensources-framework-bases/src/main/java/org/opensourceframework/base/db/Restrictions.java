package org.opensourceframework.base.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 条件组合类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public abstract class Restrictions {
	/**
	 * 主键id等于条件
	 *
	 * @param value
	 * @return
	 */
	public static Condition idEq(Object value) {
		return new QueryCondition("id", value, OperationalEnum.EQ);
	}

	/**
	 * 等于条件
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition eq(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.EQ);
	}

	/**
	 * 不等于条件
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition ne(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.NE);
	}

	/**
	 * is null条件
	 *
	 * @param property
	 * @return
	 */
	public static Condition isNull(String property) {
		return new QueryCondition(property, null, OperationalEnum.IS_NULL);
	}

	/**
	 * is not null条件
	 *
	 * @param property
	 * @return
	 */
	public static Condition isNotNull(String property) {
		return new QueryCondition(property, null, OperationalEnum.IS_NOT_NULL);
	}

	/**
	 * 小于 条件
	 * @param property
	 * @return
	 */
	public static Condition lt(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.LT);
	}

	/**
	 * 大于
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition gt(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.GT);
	}

	/**
	 * 大于等于
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition gte(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.GTE);
	}

	/**
	 * 小于等于
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition lte(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.LTE);
	}


	/**
	 * 相似
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition like(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.LIKE);
	}

	/**
	 * 在...范围
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition in(String property, Collection<? extends Object> value) {
		return new QueryCondition(property, value, OperationalEnum.IN);
	}

	/**
	 * 不在...范围
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition nin(String property, Collection<? extends Object> value) {
		return new QueryCondition(property, value, OperationalEnum.NIN);
	}

	/**
	 * 模糊查询 正则表达式
	 *
	 * @param property
	 * @param value
	 *
	 * @return
	 */
	public static Condition regex(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.REGEX);
	}

	/**
	 * 模糊查询 包含
	 *
	 * @param property
	 * @param value
	 *
	 * @return
	 */
	public static Condition contains(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.CONTAINS);
	}

	/**
	 * 模糊查询 以..开头
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition startsWith(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.STARTS_WITH);
	}

	/**
	 * 模糊查询 以..结尾
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public static Condition endsWith(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.ENDS_WITH);
	}

	/**
	 * ES专用 使用ES原生表达式
	 * @param property
	 * @param value
	 *
	 * @return
	 */
	public static Condition expression(String property, Object value) {
		return new QueryCondition(property, value, OperationalEnum.EXPRESSION);
	}

	/**
	 * and连接
	 *
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Condition and(Condition lhs, Condition rhs) {
		List<Condition> conditions = new ArrayList<>();
		conditions.add(lhs);
		conditions.add(rhs);
		return new LogicalCondition(conditions, OperationalEnum.AND);
	}

	/**
	 * and连接
	 *
	 * @param conditions
	 * @return
	 */
	public static Condition and(List<Condition> conditions) {
		return new LogicalCondition(conditions, OperationalEnum.AND);
	}

	/**
	 * or连接
	 *
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Condition or(Condition lhs, Condition rhs) {
		List<Condition> conditions = new ArrayList<>();
		conditions.add(lhs);
		conditions.add(rhs);
		return new LogicalCondition(conditions, OperationalEnum.OR);
	}

	/**
	 * or连接
	 * @param conditions
	 *
	 * @return
	 */
	public static Condition or(List<Condition> conditions) {
		return new LogicalCondition(conditions, OperationalEnum.OR);
	}
}
