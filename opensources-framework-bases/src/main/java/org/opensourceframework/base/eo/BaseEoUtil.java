package org.opensourceframework.base.eo;

import org.opensourceframework.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Random;

/**
 * Eo操作工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseEoUtil {
	private static final Logger logger = LoggerFactory.getLogger(BaseEoUtil.class);

	private static final String badParam = "drop|delete|update |truncate|table|--";
	private static final String badSql = "drop|delete|update |truncate|--";

	public static final String BASE_SELECT_SQL = "id,create_time,create_person,create_person_id,update_time,update_person,update_person_id,application_id,dr";
	public static final String BASE_INSERT_SQL = "create_time,create_time_stamp,update_time,update_time_stamp,create_person,create_person_id,application_id,update_person,update_person_id,dr";

	private static String workerId = null;
	private static final Random random = new Random();

	public BaseEoUtil() {
	}

	/**
	 * 获取雪花id生成所需的workId
	 *
	 * @return
	 */
	public static Long getWorkerId() {
		if (StringUtils.isNotEmpty(workerId)) {
			return Long.valueOf(workerId);
		} else {
			try {
				if (StringUtils.isEmpty(workerId)) {
					InetAddress localHost = InetAddress.getLocalHost();
					String hostAddress = localHost.getHostAddress();
					int lastIndex = hostAddress.lastIndexOf(".") + 1;
					String subHostAddress = hostAddress.substring(0, lastIndex - 1);
					int lastISecondndex = subHostAddress.lastIndexOf(".") + 1;
					workerId = String.valueOf(Integer.parseInt(subHostAddress.substring(lastISecondndex)) + Integer.parseInt(hostAddress.substring(lastIndex)));
				}
			} catch (Exception e) {
				logger.error("error ip generate app.worker.id.", e);
			} finally {
				if (StringUtils.isEmpty(workerId)) {
					logger.warn("use random to generate generate app.worker.id");
					workerId = String.valueOf(random.nextInt(1023));
				}
			}

			return Long.valueOf(workerId);
		}
	}

	/**
	 * 获取主键的名称
	 *
	 * @param clazz
	 * @return
	 */
	public static String idName(Class<?> clazz) {
		String fileName = null;
		while(clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(Id.class)) {
					fileName = field.getName();
					break;
				}
			}
			if(fileName == null){
				clazz = clazz.getSuperclass();
			}else{
				break;
			}
		}

		if(fileName == null) {
			throw new BizException("Undefine POJO @Id ,need Annotation(@Id) for pkId");
		}else{
			return fileName;
		}
	}

	/**
	 * 获取类对应的tableName
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T extends BaseEo> String tableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table != null) {
			return table.name();
		} else {
			throw new BizException("Undefine POJO @Table, need Annotation(@Table(name))");
		}
	}

	public static void setWorkerId(String workerId) {
		BaseEoUtil.workerId = workerId;
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
				if (sqlLowerCase.contains(badStrs[i])) {
					return false;
				}
			}
		}

		return bool;
	}
}
