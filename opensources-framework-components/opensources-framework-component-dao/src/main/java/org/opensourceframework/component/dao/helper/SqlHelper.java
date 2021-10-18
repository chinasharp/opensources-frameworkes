package org.opensourceframework.component.dao.helper;

import org.apache.commons.lang3.StringUtils;

/**
 * sql工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SqlHelper {
	private static final String badParam = "drop|delete|update|truncate|table|--";
	private static final String badSql = "drop|delete|update|truncate|--";

	public SqlHelper() {
	}

	public static boolean isSpiteSql(String sql) {
		return sqlValidate(sql, badSql);
	}

	public static boolean isSpiteParams(String sqlParams) {
		return sqlValidate(sqlParams, badParam);
	}

	private static boolean sqlValidate(String sqlParams, String badStr) {
		boolean bool = true;
		if (StringUtils.isNotEmpty(sqlParams)) {
			String sqlLowerCase = sqlParams.toLowerCase();
			String[] badStrs = badStr.split("\\|");

			for(int i = 0; i < badStrs.length; ++i) {
				if (sqlLowerCase.indexOf(badStrs[i]) >= 0) {
					return false;
				}
			}
		}

		return bool;
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线的小写格式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	 * 例如：helloWorld->hello_world
	 *
	 * @param property 驼峰风格属性名
	 * @return 数据库字段名
	 */
	public static String propertyToColumn(String property) {
		StringBuilder result = new StringBuilder();
		if (property != null && property.length() > 0) {
			// 循环处理字符
			for (int i = 0; i < property.length(); i++) {
				String s = property.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toLowerCase());
			}
		}
		return result.toString();
	}

	/**
	 * 将下划线数据库字段名转换为驼峰式属性名。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：hello_world->helloWorld
	 *
	 * @param column 数据库字段名
	 * @return 驼峰式属性名
	 */
	public static String columnToProperty(String column) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (StringUtils.isBlank(column)) {
			// 没必要转换
			return column;
		} else if (!column.contains("_")) {
			// 不含下划线，仅将首字母小写
			return column.substring(0, 1).toLowerCase() + column.substring(1);
		}
		// 用下划线将原始字符串分割
		String[] columnStrs = column.split("_");
		for (String columnStr :  columnStrs) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (columnStr.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(columnStr.toLowerCase());
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(columnStr.substring(0, 1).toUpperCase());
				result.append(columnStr.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}
}
