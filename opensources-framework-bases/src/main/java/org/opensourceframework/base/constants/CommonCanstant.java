package org.opensourceframework.base.constants;

/**
 * 常用常量定义
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class CommonCanstant {
	public static final int SUCCESS_CODE = 0;
	public static final int ERROR_CODE = -1;
	public static final int FAILURE_CODE = 1;

	public static final String SUCESS_MSG = "SUCCESS";
	public static final String ERROR_MSG = "ERROR";
	public static final String FAILURE_MSG = "FAILURE";

	public static final Integer YES = 1;
	public static final Integer NO = 0;

	public static final String YES_STR = "1";
	public static final String NO_STR = "0";

	/**
	 * 是
	 */
	public static final int TRUE = 1;

	/**
	 * 否
	 */
	public static final int FALSE = 0;

	public static int conver(String str) {
		return (("1".equals(str)) ? TRUE : FALSE);
	}

	public static Integer DEFAULT_PAGE_NUMBER = Integer.valueOf(1);

	public static Integer DEFAULT_PAGE_SIZE = Integer.valueOf(10);

	/**
	 * 是否将驼峰属性转换为下划线
	 */
	public static final String DB_AUTO_HUMP_MYSQL_COLUMN = "db.mysql.autoHumpCloumn";

	/**
	 * 是否将驼峰属性转换为下划线
	 */
	public static final String DB_AUTO_HUMP_ES_COLUMN = "db.es.autoHumpCloumn";

	/**
	 * 是否将驼峰属性转换为下划线
	 */
	public static final String DB_AUTO_HUMP_MOGODB_COLUMN = "db.mogodb.autoHumpCloumn";
}
