package org.opensourceframework.component.dao.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hash MOD算法分表 获取子表名
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2019/2/22 10:19 AM
 */
public class HashModTable {
	private static final Logger logger = LoggerFactory.getLogger(HashModTable.class);

	/**
	 * 得到hsah子表名
	 * </p>
	 * 
	 * @param tableName
	 *            表名<br>
	 * @param splitKeyVal
	 *            表拆分键的值(根据splitKeyVal值来取模)<br>
	 * @param subTableCount
	 *            要拆分子表总数<br>
	 * @return
	 */
	public static String getSplitTableName(String tableName, Object splitKeyVal, Integer subTableCount) {
		if(splitKeyVal == null){
			throw new RuntimeException("splitKeyVal is null.tableName:" +tableName);
		}

		long hashVal = splitKeyVal.toString().hashCode();

		// 斐波那契（Fibonacci）散列
		hashVal = ( hashVal * 2654435769L ) >> 28;

		// 避免hashVal超出 MAX_VALUE = 0x7fffffff时变为负数,取绝对值
		hashVal = Math.abs(hashVal) % subTableCount;
		String hashTableName = tableName + "_" + hashVal;
		if (logger.isDebugEnabled()) {
			logger.debug("used hash mod algorithm. build {}'s subtable name: {}. ", tableName, hashTableName);
		}
		return hashTableName;
	}


}
