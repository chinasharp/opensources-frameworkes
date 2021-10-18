package org.opensourceframework.component.dao.contant;

/**
 * SQL语句常量
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SqlStatementContant {
	public static final String FIELD_VALUE_SEPARATOR = "#";

	public static final String FIELDS_SEPARATOR = ";";

	public static Integer DEFAULT_PAGE_NUMBER = Integer.valueOf(1);

	public static Integer DEFAULT_PAGE_SIZE = Integer.valueOf(10);

	public static String NULL_EXAMPLE_STR = "{}";

	public static final String POLICY_FREE = "FREE";
	public static final String POLICY_TWO_PC = "TWO_PC";
	public static final String POLICY_XA = "XA";
	public static final String POLICY_FLEXIBLE = "FLEXIBLE";

	public static final String CLASS_PARAM = "class";

	/**
	 * 没有拆分键时查询分表数据的执行方法
	 */
	public static final String QUERY_SHARDING_DATA_NOT_KEY_METHOD = "findNotWithShardingKey";

	/**
	 * 没有拆分键时更新分表数据的执行方法
	 */
	public static final String UPDATE_SHARDING_DATA_NOT_KEY_METHOD = "updateNotWithShardingKey";

	/**
	 * 没有拆分键时删除分表数据的执行方法
	 */
	public static final String DELETE_SHARDING_DATA_NOT_KEY_METHOD = "deleteNotWithShardingKey";

	public static final String WHERE_MAP_KEY = "whereMap";
	public static final String UPDATE_MAP_KET = "updateMap";
	public static final String WHERE_CONDITION_KET = "condition";
	/**
	 * 是否逻辑删除
	 */
	public static final String IS_LOGIC_DEL_KEY = "isLogicDel";

	public static final String QUERY_DATA_LIMIT_PARAM = "limit";
	public static final int QUERY_DATA_DEFAULT_LIMIT = 1000;
}
