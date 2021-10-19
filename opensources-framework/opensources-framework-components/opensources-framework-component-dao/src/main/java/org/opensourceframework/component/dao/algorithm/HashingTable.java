package org.opensourceframework.component.dao.algorithm;

import org.opensourceframework.component.dao.algorithm.base.Hashing;
import org.opensourceframework.component.dao.algorithm.base.MurmurHash;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * hashing算法分表 获取子表名
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2019/2/22 10:19 AM
 */
public class HashingTable {
	/**
	 * 虚拟节点数
	 */
	private static final int DUMMY_NODE_NUM = 3000;

	/**
	 * MurmurHash算法
	 */
	private static final Hashing MURMUR_HASH = new MurmurHash();

	private static final ConcurrentHashMap<String , Object> lockMap = new ConcurrentHashMap<>(100);

	/**
	 * hash存储
	 */
	private static final Map<String, TreeMap<Long, String>> resources = new LinkedHashMap<String, TreeMap<Long, String>>();

	private static TreeMap<Long, String> buildHashingNodes(String tableName, int subTableCount){
		TreeMap<Long, String> nodes = null;
		List<String> subTableNames  = new ArrayList<String>();
		for (int i = 1; i < subTableCount + 1; i++) {
			subTableNames.add(tableName.concat("_") + i);
		}
		nodes = new TreeMap<Long, String>();
		for (int i = 0; i != subTableNames.size(); ++i) {
			final String subTableName = subTableNames.get(i);
			for (int n = 0; n < DUMMY_NODE_NUM; n++) {
				nodes.put(MURMUR_HASH.hash("hashing" + i + "-table-" + n), subTableName);
			}
		}
		resources.put(tableName, nodes);

		return nodes;
	}

	/**
	 * 得到hsah子表名
	 * </p>
	 *
	 * @param tableName
	 *            表名<br>
	 * @param splitKeyVal
	 *            表拆分键的值<br>
	 * @param subTableCount
	 *            要拆分子表总数<br>
	 * @return
	 */
	public static String getSplitTableName(String tableName, Object splitKeyVal, Integer subTableCount) {
		if(splitKeyVal == null || StringUtils.isBlank(splitKeyVal.toString())){
			throw new RuntimeException("split key value is not null!");
		}
		String hashTableName = null;
		TreeMap<Long, String> nodes = resources.get(tableName);

		if(nodes == null){
			Object lockObj = getLockObj(tableName);
			synchronized (lockObj) {
				nodes = buildHashingNodes(tableName, subTableCount);
			}
		}
		SortedMap<Long, String> tail = nodes.tailMap(MURMUR_HASH.hash(splitKeyVal.toString()));
		if (tail.isEmpty()) {
			hashTableName = nodes.get(nodes.firstKey());
		} else {
			hashTableName = tail.get(tail.firstKey());
		}
		return hashTableName;
	}

	private static Object getLockObj(String tableName){
		Object lockObj = lockMap.get(tableName);
		if(lockObj == null){
			lockMap.putIfAbsent(tableName,new Byte("1"));
		}
		return lockMap.get(tableName);
	}
}
