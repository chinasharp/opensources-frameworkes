package org.opensourceframework.component.redis.common;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.component.redis.cache.config.RedisConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * 基础缓存service
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseCacheService {
	public static final String COLON_NAME = ":";
	protected int SCAN_DEFAULT_COUNT = 500;
	protected String PATTERN_CHAR = "*";
	protected String SCAN_CURSOR_INDEX = "cursor";
	protected String REDIS_DATA_NULL = "null";
	protected String REDIS_DATA_NIL = "nil";

	/**
	 * hsah结构中 field字符串连接符号
	 */
	protected String CAHCE_FIELD_SPLIT_CHAR = ":";
	protected JedisPool jedisPool;
	protected ShardedJedisPool shardedJedisPool;
	protected JedisCluster jedisCluster;
	protected int dbIndex;

	protected String group;
	protected RedisConfig redisConfigVo;

	public BaseCacheService(String group, RedisConfig redisConfigVo) {
		this.group = group;
		this.redisConfigVo = redisConfigVo;
	}


	public String combineKey(String group, String key) {
		if (StringUtils.isNotBlank(group)) {
			return group.concat(COLON_NAME).concat(key);
		}
		return key;
	}

	public String[] combineKeyValues(String group, String[] keysvalues) {
		String[] templ = new String[keysvalues.length];
		for (int keyIdx = 0; keyIdx < templ.length / 2; keyIdx++) {
			templ[(keyIdx * 2)] = combineKey(group, keysvalues[(keyIdx * 2)]);
			templ[(keyIdx * 2 + 1)] = keysvalues[(keyIdx * 2 + 1)];
		}
		return templ;
	}

	public String[] combineKeys(String group, String[] keys) {
		String[] templ = new String[keys.length];
		for (int keyIdx = 0; keyIdx < templ.length; keyIdx++) {
			templ[keyIdx] = combineKey(group, keys[keyIdx]);
		}
		return templ;
	}

	public Map<String, String> combineKeys(String group, Map<String, String> dataMap) {
		Map<String, String> templ = new HashMap();
		for (Map.Entry<String, String> m : dataMap.entrySet()) {
			templ.put(combineKey(group, m.getKey()), m.getValue());
		}
		return templ;
	}

	public String[] combineKeyValues(String group, Map<String, String> dataMap) {
		if ((dataMap == null) || (dataMap.isEmpty())) {
			String[] templ = new String[dataMap.size() * 2];
			int index = 0;
			for (Map.Entry<String, String> m : dataMap.entrySet()) {
				templ[(index * 2)] = combineKey(group, m.getKey());
				templ[(index * 2 + 1)] = m.getValue();
				index++;
			}
			return templ;
		}
		return null;
	}

	/**
	 * 判断redis返回的数据是否为空
	 *
	 * @param jsonData
	 * @return
	 */
	public Boolean checkRedisDataNotNull(String jsonData){
		return StringUtils.isNotBlank(jsonData) && !REDIS_DATA_NULL.equals(jsonData) && !REDIS_DATA_NIL.equals(jsonData);
	}

	/**
	 * jsonList的值为单个对象的json
	 *
	 * @param jsonDataList
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	protected <T> List<T> parsePOJO(List<String> jsonDataList , Class<T> clazz){
		List<T> dataList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(jsonDataList)){
			for(String jsonData : jsonDataList){
				if(checkRedisDataNotNull(jsonData)){
					dataList.add(JSON.parseObject(jsonData , clazz));
				}
			}
		}
		return dataList;
	}

	/**
	 * jsonList的值为集合对象的json
	 *
	 * @param jsonDataList
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	protected <T> List<T> parseList(List<String> jsonDataList , Class<T> clazz){
		List<T> dataList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(jsonDataList)){
			for(String jsonData : jsonDataList){
				if(checkRedisDataNotNull(jsonData)){
					dataList.addAll(JSON.parseArray(jsonData , clazz));
				}
			}
		}
		return dataList;
	}

	protected Map<Long, String> getMap(Set<Tuple> list) {
		LinkedHashMap<Long, String> map = new LinkedHashMap();
		if(CollectionUtils.isNotEmpty(list)) {
			for (Tuple tuple : list) {
				map.put(Long.valueOf(Math.round(tuple.getScore())), tuple.getElement());
			}
		}
		return map;
	}

	protected Map<String, Long> getMapByScore(Set<Tuple> list) {
		LinkedHashMap<String, Long> map = new LinkedHashMap();
		if(CollectionUtils.isNotEmpty(list)) {
			for (Tuple tuple : list) {
				map.put(tuple.getElement(), Long.valueOf(Math.round(tuple.getScore())));
			}
		}
		return map;
	}
}
