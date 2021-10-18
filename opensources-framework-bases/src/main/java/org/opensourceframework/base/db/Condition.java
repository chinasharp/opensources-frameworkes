package org.opensourceframework.base.db;

import java.util.Map;

/**
 * 数据库条件基类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public interface Condition {
	/**
	 * 创建条件对象
	 *
	 * @return
	 */
    Condition createCondition();

	/**
	 * and条件连接
	 *
	 * @param condition
	 * @return
	 */
    Condition add(Condition condition);

	/**
	 * or条件连接
	 *
	 * @param condition
	 * @return
	 */
    Condition or(Condition condition);

	/**
	 * 获取操作
	 *
	 * @return
	 */
    OperationalEnum getOperator();

	Map<String , Object> getPropertyMap();
}
