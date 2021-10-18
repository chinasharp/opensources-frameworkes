package org.opensourceframework.base.cache;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 缓存基本API接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface ICacheService {
	/**
	 * 写入缓存
	 *
	 * @param group
	 * @param cacheKey
	 * @param obj
	 * @param expireTime   0 为不过期 单位秒
	 * @return
	 */
	Boolean set(String group , String cacheKey, Object obj, Integer expireTime);

	/**
	 * 写入缓存  使用创建RedisCacheService Bean时的group配置
	 *
	 * @param cacheKey
	 * @param obj
	 * @param expireTime   0 为不过期  单位秒
	 * @return
	 */
	Boolean set(String cacheKey, Object obj, Integer expireTime);


	/**
	 * 获取缓存 String结构
	 *
	 * @param group  指定组
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T get(String group, String key, Class<T> clazz);

	/**
	 * 获取缓存 String结构  使用创建RedisCacheService Bean时的group配置
	 *
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T get(String key, Class<T> clazz);


	/**
	 * 获取缓存 String结构  value为List对象
	 *
	 * @param group
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> List<T> getList(String group , String key, Class<T> clazz);

	/**
	 * 获取缓存 String结构  value为List对象
	 *
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> List<T> getList(String key, Class<T> clazz);

	/**
	 * 删除缓存
	 *
	 * @param group
	 * @param key
	 * @return
	 */
	Boolean del(String group, String key);

	/**
	 * 删除缓存
	 *
	 * @param key
	 * @return
	 */
	Boolean del(String key);


	/**
	 * hash结构保存
	 *
	 * @param group
	 * @param key
	 * @param field
	 * @param value
	 * @param expireTime
	 * @param <T>
	 */
	<T> void hset(String group, String key, String field, T value, Integer expireTime);

	/**
	 * hash结构保存
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @param expireTime
	 * @param <T>
	 */
	<T> void hset(String key, String field, T value, Integer expireTime);

	/**
	 * 获取缓存并反序列化为指定类的对象
	 *
	 * @param group
	 * @param key
	 * @param field
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T hget(String group, String key, String field, Class<T> clazz);

	/**
	 * 获取缓存并反序列化为指定类的对象
	 *
	 * @param key
	 * @param field
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T hget(String key, String field, Class<T> clazz);

	/**
	 * 根据缓存key 得到JSONObject对象
	 *
	 * @param cacheKey
	 * @param cacheField
	 * @return
	 */
	JSONObject hgetJSONObj(String cacheKey, String cacheField);

	/**
	 * 根据缓存key 得到JSONObject对象
	 *
	 * @param group
	 * @param cacheKey
	 * @param cacheField
	 * @return
	 */
	JSONObject hgetJSONObj(String group ,String cacheKey, String cacheField);

	/**
	 * 根据缓存key 得到JSONObject对象
	 *
	 * @param cacheKey
	 * @return
	 */
	JSONObject getJSONObj(String cacheKey);

	/**
	 * 根据缓存key 得到JSONObject对象
	 *
	 * @param group
	 * @param cacheKey
	 * @return
	 */
	JSONObject getJSONObj(String group ,String cacheKey);


	/**
	 * 根据缓存key 得到缓存数据
	 *
	 * @param cacheKey
	 * @return
	 */
	String get(String cacheKey);

	/**
	 * 根据分组 缓存key 得到缓存数据
	 *
	 * @param cacheKey
	 * @return
	 */
	String get(String group , String cacheKey);
}
