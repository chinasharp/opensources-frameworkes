package org.opensourceframework.component.redis.cache.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.component.redis.cache.config.RedisConfig;
import org.opensourceframework.component.redis.cache.queue.IRedisSubProcessor;
import org.opensourceframework.component.redis.cache.service.IRedisCacheService;
import org.opensourceframework.component.redis.common.BaseCacheService;
import org.opensourceframework.component.redis.cache.contants.RedisRespCodes;
import org.opensourceframework.component.redis.cache.contants.WorkModel;
import org.opensourceframework.component.redis.cache.queue.ListenerConver;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.Sharded;

import java.util.*;
import java.util.stream.Collectors;

import static redis.clients.jedis.util.Hashing.MURMUR_HASH;

/**
 * Redis缓存实现类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RedisCacheService extends BaseCacheService implements IRedisCacheService {
	protected static final Logger logger = org.slf4j.LoggerFactory.getLogger(RedisCacheService.class);
	//已缓存标志
	protected static final Boolean INIT_FALG = true;

	protected static final String VALUE_TYPE_POJO = "pojo";
	protected static final String VALUE_TYPE_LIST = "list";
	protected static final String VALUE_TYPE_MAP = "map";

	public RedisCacheService(String group, RedisConfig redisConfig) {
		super(group , redisConfig);
		init(group , redisConfig);
	}

	protected void init(String group, RedisConfig redisConfig) {
		this.redisConfigVo = redisConfig;
		this.group = group;

		if (logger.isDebugEnabled()) {
			logger.debug("group:{}, redis 配置:{}", group, JSON.toJSONString(redisConfig));
		}

		checkAddress(redisConfig.getAddresses());
		this.dbIndex = redisConfig.getDatabase();
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		setConfig(poolConfig);
		int timeOut = redisConfig.getTimeout();

		if (WorkModel.SINGLE.getName().equalsIgnoreCase(redisConfig.getWorkModel())) {
			String address = null;
			String host = null;
			String port = null;
			String[] addresses = redisConfig.getAddresses();
			if(addresses != null && addresses.length > 0) {
				address = redisConfig.getAddresses()[0];
				host = address.split(":")[0];
				port = address.split(":")[1];
			}else{
				host = redisConfig.getHost();
				port = redisConfig.getPort();
			}

			if(StringUtils.isBlank(host) || StringUtils.isBlank(port)){
				throw new BizException("init cache registryvo error. not found redis server host:port registryvo.");
			}

			this.jedisPool = new JedisPool(poolConfig, host, Integer.parseInt(port), timeOut,
					StringUtils.isEmpty(redisConfig.getAuthPwd()) ? null : redisConfig.getAuthPwd(), this.dbIndex, redisConfig.isSsl());
		} else if (WorkModel.CLUSTER.getName().equalsIgnoreCase(redisConfig.getWorkModel())) {
			this.jedisCluster = new JedisCluster(getHostAndPortSet(redisConfig), timeOut,
					3000, 1, StringUtils.isEmpty(redisConfig.getAuthPwd()) ? null : redisConfig.getAuthPwd(), poolConfig);
		} else if (WorkModel.SHARDING.getName().equalsIgnoreCase(redisConfig.getWorkModel())) {
			this.shardedJedisPool = new ShardedJedisPool(poolConfig, getShardInfoList(redisConfig), MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
		} else {
			throw new BizException("初始化RedisCache时找不到指定运行模式，workModel:" + redisConfig.getWorkModel());
		}

	}

	protected void checkAddress(String[] addresses) {
		if (addresses == null) {
			throw new BizException("连接RedisCache时连接地址不能为空");
		}
	}

	protected Set<HostAndPort> getHostAndPortSet(RedisConfig redisConfigVo) {
		Set<HostAndPort> hostAndPortSet = new HashSet();
		if (redisConfigVo.getAddresses() != null) {
			String[] addrs = redisConfigVo.getAddresses();
			for (int i = 0; i < addrs.length; i++) {
				HostAndPort hostAndPort = new HostAndPort(getHost(addrs[i]), getPort(addrs[i]));
				hostAndPortSet.add(hostAndPort);
			}
		} else {
			HostAndPort hostAndPort = new HostAndPort(redisConfigVo.getHost(), Integer.parseInt(redisConfigVo.getPort()));
			hostAndPortSet.add(hostAndPort);
		}
		return hostAndPortSet;
	}

	protected List<JedisShardInfo> getShardInfoList(RedisConfig redisConfigVo) {
		List<JedisShardInfo> shardsList = new ArrayList();
		if (redisConfigVo.getAddresses() != null) {
			String[] addrs = redisConfigVo.getAddresses();
			for (int i = 0; i < addrs.length; i++) {
				addShardInfo(getHost(addrs[i]), Integer.valueOf(getPort(addrs[i])), redisConfigVo.getAuthPwd(), shardsList);
			}
		} else {
			addShardInfo(redisConfigVo.getHost(), Integer.valueOf(Integer.parseInt(redisConfigVo.getPort())), redisConfigVo.getAuthPwd(), shardsList);
		}
		return shardsList;
	}

	protected void addShardInfo(String host, Integer port, String appSecret, List<JedisShardInfo> shardsList) {
		StringBuilder uriHost = new StringBuilder("redis://").append(host).append(":").append(port);
		if (this.dbIndex > 0) {
			uriHost.append("/").append(this.dbIndex);
		}
		JedisShardInfo jedisShardInfo = new JedisShardInfo(uriHost.toString());
		if (StringUtils.isNotEmpty(appSecret)) {
			jedisShardInfo.setPassword(appSecret);
		}
		jedisShardInfo.setConnectionTimeout(3000);
		jedisShardInfo.setSoTimeout(3000);
		shardsList.add(jedisShardInfo);
	}

	protected String getHost(String val) {
		return val.substring(0, val.indexOf(":"));
	}

	protected int getPort(String val) {
		return Integer.parseInt(val.substring(val.indexOf(":") + 1));
	}

	protected void setConfig(GenericObjectPoolConfig config) {
		config.setMaxIdle(this.redisConfigVo.getMaxIdle());
		config.setMaxTotal(this.redisConfigVo.getMaxTotal());
		config.setTestOnBorrow(false);
		config.setTestOnReturn(false);
		config.setMaxWaitMillis(this.redisConfigVo.getMaxWaitMillis());
	}

	protected final Jedis getJedis() {
		if (this.jedisPool == null) {
			return null;
		}
		Jedis jedis = this.jedisPool.getResource();
		return jedis;
	}

	protected final ShardedJedis getShardedJedis() {
		if (this.shardedJedisPool == null) {
			return null;
		}
		ShardedJedis shardedJedis = this.shardedJedisPool.getResource();
		return shardedJedis;
	}

	protected final JedisCluster getJedisCluster() {
		return this.jedisCluster;
	}

	/**
	 * 判断是否已经初始化
	 *
	 * @return
	 */
	@Override
	public Boolean checkHadInit(String initFlagKey , int expireSec){
		boolean flag = add(initFlagKey , INIT_FALG ,expireSec);
		return !flag;
	}

	@Override
	public Boolean set(String group, String key, Object value, Integer seconds) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		boolean flag = false;
		String result = "";

		String combineKey = combineKey(group, key);
		String jsonValue = JSON.toJSONString(value);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if(seconds != 0) {
				if (jds != null) {
					result = jds.setex(combineKey, seconds, jsonValue);
				} else if (shardedJedis != null) {
					result = shardedJedis.setex(combineKey, seconds, jsonValue);
				} else if (this.jedisCluster != null) {
					result = this.jedisCluster.setex(combineKey, seconds, jsonValue);
				}
			}else{
				if (jds != null) {
					result = jds.set(combineKey, jsonValue);
				} else if (shardedJedis != null) {
					result = shardedJedis.set(combineKey,jsonValue);
				} else if (this.jedisCluster != null) {
					result = this.jedisCluster.set(combineKey,jsonValue);
				}
			}
			if (RedisRespCodes.OK.equalsIgnoreCase(result)) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("设置缓存出错: key={}, value={}", combineKey, jsonValue, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("after setCache,key={},seconds={},value={},flag={}", combineKey, Integer.valueOf(seconds), jsonValue, Boolean.valueOf(flag));
		}
		return flag;
	}

	@Override
	public Boolean set(String key, Object value, Integer seconds){
		return set(this.group ,key , value ,seconds);
	}

	/**
	 * 原生mset方法不可设置过期时间 故使用循环写入  多数据(大于5个)写入禁止使用此方法
	 * @param dataMap  cache key:map key , cache value : map value
	 * @return
	 */
	@Override
	public void mset(Map<String , String> dataMap , Integer seconds) {
		if(MapUtils.isNotEmpty(dataMap)) {
			Jedis jds = null;
			ShardedJedis shardedJedis = null;
			JedisCluster jedisCluster = null;
			try {
				jds = this.getJedis();
				shardedJedis = this.getShardedJedis();
				jedisCluster = this.getJedisCluster();
				String combineKey = null;
				String value = null;
				List<String> keyValues = new ArrayList<>();
				List<String> keys = new ArrayList<>();
				List<String> values = new ArrayList<>();
				for (Map.Entry<String, String> data : dataMap.entrySet()) {
					combineKey = this.combineKey(group, data.getKey());
					value = data.getValue();
					keys.add(combineKey);
					values.add(value);
					keyValues.add(combineKey);
					keyValues.add(value);
				}

				String[] keyValuesArray = new String[keyValues.size()];
				if (jds != null) {
					jds.mset(keyValues.toArray(keyValuesArray));
					if (seconds > 0) {
						for (String key : keys) {
							jds.expire(key, seconds);
						}
					}
				} else if (shardedJedis != null) {
					for (int i = 0; i < keys.size(); i++) {
						if (seconds == 0) {
							shardedJedis.set(keys.get(i), values.get(i));
						} else {
							shardedJedis.setex(keys.get(i),seconds, values.get(i));
						}
					}
				} else if (jedisCluster != null) {
					jedisCluster.mset(keyValues.toArray(keyValuesArray));
					if (seconds > 0) {
						for (String key : keys) {
							jedisCluster.expire(key, seconds);
						}
					}
				}
			} catch (Exception e) {
				logger.error("mset数据出错 , data : {}", JSON.toJSONString(dataMap));
			} finally {
				this.shutdown(jds, shardedJedis ,jedisCluster);
			}
		}
	}

	@Override
	public Boolean setPersistCache(String group, String key, Object value) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		boolean flag = false;
		String result = "";
		String combineKey = combineKey(group, key);
		String jsonValue = JSON.toJSONString(value);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				jds.expire(combineKey, 1);
				jds.persist(combineKey);
				result = jds.set(combineKey, jsonValue);
			} else if (shardedJedis != null) {
				shardedJedis.expire(combineKey, 1);
				shardedJedis.persist(combineKey);

				result = shardedJedis.set(combineKey, jsonValue);
			} else if (this.jedisCluster != null) {
				this.jedisCluster.expire(combineKey, 1);
				this.jedisCluster.persist(combineKey);

				result = this.jedisCluster.set(combineKey, jsonValue);
			}
			if ("OK".equalsIgnoreCase(result)) {
				flag = true;
			}
			logger.debug("设置持久化缓存: key={}", key);
		} catch (Exception e) {
			logger.error("设置缓存出错: key={}, value={}", combineKey, jsonValue, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("after setPersistCache,key={},value={},flag={}", combineKey, jsonValue, Boolean.valueOf(flag));
		}
		return flag;
	}


	@Override
	public <T> T get(String group, String key, Class<T> clazz) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		T value = null;
		String json = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				json = jds.get(combineKey);
			} else if (shardedJedis != null) {
				json = shardedJedis.get(combineKey);
			} else if (this.jedisCluster != null) {
				json = this.jedisCluster.get(combineKey);
			}
			if (StringUtils.isNotEmpty(json)) {
				value = JSON.parseObject(json, clazz);
			}
		} catch (Exception e) {
			logger.error("读取缓存出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return value;
	}


	@Override
	public <T> T get(String key, Class<T> clazz) {
		return get(this.group , key , clazz);
	}

	/**
	 * 获取缓存 String结构  value为List对象
	 *
	 * @param group
	 * @param key
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> List<T> getList(String group, String key, Class<T> clazz) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		List<T> dataList = Lists.newArrayList();
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();

			String jsonData = null;
			if (jds != null) {
				jsonData = jds.get(combineKey);
			} else if (shardedJedis != null) {
				jsonData = shardedJedis.get(combineKey);
			} else if (this.jedisCluster != null) {
				jsonData = this.jedisCluster.get(combineKey);
			}
			if (checkRedisDataNotNull(jsonData)) {
				dataList = JSON.parseArray(jsonData , clazz);
			}
		} catch (Exception e) {
			logger.error("读取缓存出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return dataList;
	}

	/**
	 * 获取缓存 String结构  value为List对象
	 *
	 * @param key
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		return getList(this.group , key , clazz);
	}

	@Override
	public <T> List<T> mget(String group , Collection<String> keyList , Class<T> clazz){
		List<T> eoList = null;
		if(!CollectionUtils.isEmpty(keyList)) {
			Jedis jds = null;
			ShardedJedis shardedJedis = null;
			JedisCluster jedisCluster = null;

			String[] combineKeyArray = new String[keyList.size()];

			int index = 0;
			for(String key : keyList){
				combineKeyArray[index ++] = this.combineKey(group, key);
			}

			List<String> jsonDataList = null;
			try {
				jds = this.getJedis();
				shardedJedis = this.getShardedJedis();
				jedisCluster = this.getJedisCluster();
				if (jds != null) {
					jsonDataList = jds.mget(combineKeyArray);
				} else if (shardedJedis != null) {
					jsonDataList = new ArrayList<>();
					for(String cacheKey : combineKeyArray){
						jsonDataList.add(shardedJedis.get(cacheKey));
					}
				} else if (jedisCluster != null) {
					jsonDataList = jedisCluster.mget(combineKeyArray);
				}

				eoList = parsePOJO(jsonDataList , clazz);
			} catch (Exception e) {
				logger.error("设置map元素数据出错: keys={}, values={}", keyList, eoList, e);
				throw new BizException(e.getMessage());
			} finally {
				this.shutdown(jds, shardedJedis , jedisCluster);
			}
		}else{
			logger.warn("cache data size is 0.cache not exec");
		}
		return eoList;
	}

	@Override
	public <T> List<T> mget(Collection<String> keyList , Class<T> clazz){
		return mget(this.group , keyList , clazz);
	}

	@Override
	public Boolean del(String group, String key) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		boolean flag = false;
		Long del = Long.valueOf(0L);
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				del = jds.del(combineKey);
			} else if (shardedJedis != null) {
				del = shardedJedis.del(combineKey);
			} else if (this.jedisCluster != null) {
				del = this.jedisCluster.del(combineKey);
			}
			if (del.longValue() > RedisRespCodes.SUCCESS) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("读取缓存出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Boolean del(String key){
		return del(this.group , key);
	}

	@Override
	public Long incr(String group, String key, Integer expireTime) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long incr = Long.valueOf(0L);
		String combineKey = combineKey(group , key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				expire(expireTime, jds, combineKey);
				incr = jds.incr(combineKey);
			} else if (shardedJedis != null) {
				expire(expireTime, shardedJedis, combineKey);
				incr = shardedJedis.incr(combineKey);
			} else if (this.jedisCluster != null) {
				expire(expireTime, this.jedisCluster, combineKey);
				incr = this.jedisCluster.incr(combineKey);
			}
		} catch (Exception e) {
			logger.error("incr缓存出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return incr;
	}

	@Override
	public Long incr(String key, Integer expireTime) {
		return incr(this.group , key , expireTime);
	}

	@Override
	public Long incrBy(String group, String key, Long count, Integer expireTime) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long incr = Long.valueOf(0L);
		String combineKey = combineKey(group , key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				expire(expireTime, jds, combineKey);
				incr = jds.incrBy(combineKey, count);
			} else if (shardedJedis != null) {
				expire(expireTime, shardedJedis, combineKey);
				incr = shardedJedis.incrBy(combineKey, count);
			} else if (this.jedisCluster != null) {
				expire(expireTime, this.jedisCluster, combineKey);
				incr = this.jedisCluster.incrBy(combineKey, count);
			}
		} catch (Exception e) {
			logger.error("incr缓存出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return incr;
	}


	@Override
	public Long incrBy(String key, Long count, Integer expireTime) {
		return incrBy(this.group , key , count , expireTime);
	}

	@Override
	public <T> void hset(String group, String key, String field, T value, Integer expireTime) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineKey = combineKey(group, key);
		String jsonValue = JSON.toJSONString(value);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				jds.hset(combineKey, field, jsonValue);
				expire(expireTime, jds, combineKey);
			} else if (shardedJedis != null) {
				shardedJedis.hset(combineKey, field, jsonValue);
				expire(expireTime, shardedJedis, combineKey);
			} else if (this.jedisCluster != null) {
				this.jedisCluster.hset(combineKey, field, jsonValue);
				expire(expireTime, this.jedisCluster, combineKey);
			}
		} catch (Exception e) {
			logger.error("设置map元素数据出错: key={}, value={}", combineKey, jsonValue, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
	}

	@Override
	public <T> void hset(String key, String field, T value, Integer expireTime) {
		hset(this.group , key , field , value ,expireTime);
	}

	@Override
	public Boolean hmset(String group ,String key, Map<String, String> mapData) {
		Boolean flag = false;
		String  result = null;
		if(MapUtils.isNotEmpty(mapData)) {
			Jedis jds = null;
			ShardedJedis shardedJedis = null;
			JedisCluster jedisCluster = null;
			String combineKey = this.combineKey(group, key);
			try {
				jds = this.getJedis();
				shardedJedis = this.getShardedJedis();
				jedisCluster = this.getJedisCluster();
				if (jds != null) {
					result = jds.hmset(combineKey, mapData);
				} else if (shardedJedis != null) {
					result = shardedJedis.hmset(combineKey, mapData);
				} else if (jedisCluster != null) {
					result = jedisCluster.hmset(combineKey, mapData);
				}
				if ("OK".equalsIgnoreCase(result)) {
					flag = true;
				}
			} catch (Exception e) {
				logger.error("设置map元素数据出错: key={}, value={}", combineKey, JSON.toJSONString(mapData), e);
				throw new BizException(e.getMessage());
			} finally {
				this.shutdown(jds, shardedJedis , jedisCluster);
			}
		}else{
			logger.warn("cache data size is 0.cache not exec");
		}
		return flag;
	}

	@Override
	public Boolean hmset(String key, Map<String, String> mapData){
	    return hmset(this.group , key , mapData);
	}

	@Override
	public void hsetForList(String key , Collection<? extends BaseEo> eoList){
		Map<String , String> dataMap = new HashMap<>();
		for(BaseEo eo : eoList){
			dataMap.put(eo.getId().toString() , JSON.toJSONString(eo));
		}
		this.hmset(key , dataMap);
	}
	@Override
	public void hsetForList(String group , String key , Collection<? extends BaseEo> eoList){
		Map<String , String> dataMap = new HashMap<>();
		for(BaseEo eo : eoList){
			dataMap.put(eo.getId().toString() , JSON.toJSONString(eo));
		}
		this.hmset(group ,key , dataMap);
	}

	@Override
	public <T> T hget(String group, String key, String field, Class<T> clazz) {
		T pojo = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String json = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				json = jds.hget(combineKey, field);
			} else if (shardedJedis != null) {
				json = shardedJedis.hget(combineKey, field);
			} else if (this.jedisCluster != null) {
				json = this.jedisCluster.hget(combineKey, field);
			}
			pojo = (StringUtils.isNotEmpty(json) ? JSON.parseObject(json, clazz) : null);
		} catch (Exception e) {
			logger.error("取出map元素数据出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return pojo;
	}

	@Override
	public <T> T hget(String key, String field, Class<T> clazz){
		return hget(this.group , key ,field , clazz);
	}

	@Override
	public <T> List<T> hgetList(String group, String key, String field, Class<T> clazz) {
		List<T> tList = Lists.newArrayList();
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String json = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				json = jds.hget(combineKey, field);
			} else if (shardedJedis != null) {
				json = shardedJedis.hget(combineKey, field);
			} else if (this.jedisCluster != null) {
				json = this.jedisCluster.hget(combineKey, field);
			}

			if(checkRedisDataNotNull(json)) {
				tList = JSON.parseArray(json, clazz);
			}
		} catch (Exception e) {
			logger.error("取出map元素数据出错: key={}", key, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return tList;
	}

	@Override
	public <T> List<T> hgetList(String key, String field, Class<T> clazz) {
		return hgetList(this.group , key , field , clazz);
	}

	@Override
	public <K, V> Map<K, V> hgetMap(String group, String key, String field, TypeReference<Map<K, V>> typeReference) {
		Map<K,V> dataMap = Maps.newHashMap();
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String json = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				json = jds.hget(combineKey, field);
			} else if (shardedJedis != null) {
				json = shardedJedis.hget(combineKey, field);
			} else if (this.jedisCluster != null) {
				json = this.jedisCluster.hget(combineKey, field);
			}
			if(checkRedisDataNotNull(json)){
				dataMap = JSON.parseObject(json , typeReference);
			}
		} catch (Exception e) {
			logger.error("取出map元素数据出错: key={}", key, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return dataMap;
	}

	@Override
	public <K, V> Map<K, V> hgetMap(String key, String field, TypeReference<Map<K, V>> typeReference){
		return hgetMap(this.group , key , field , typeReference);
	}

	@Override
	public Boolean hdel(String group, String key, String[] cacheFields) {
		Boolean flag = true;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		long result = 0L;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				result = jds.hdel(combineKey, cacheFields).longValue();
			} else if (shardedJedis != null) {
				result = shardedJedis.hdel(combineKey, cacheFields).longValue();
			} else if (this.jedisCluster != null) {
				result = this.jedisCluster.hdel(combineKey, cacheFields).longValue();
			}
			flag =  result > 0L;
		} catch (Exception e) {
			logger.error("删除map元素数据出错: key={},cacheFields={}", key, cacheFields , e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Boolean hdel(String key, String[] cacheFields) {
		return hdel(this.group , key ,cacheFields);
	}

	@Override
	public <T> List<T> hgetAll(String group ,String cacheKey , Class<T> clazz){
		List<T> values = null;
		List<String> jsonDataList = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, cacheKey);
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			if (jds != null) {
				jsonDataList = jds.hvals(combineKey);
			} else if (shardedJedis != null) {
				jsonDataList = shardedJedis.hvals(combineKey);
			} else if (jedisCluster != null) {
				jsonDataList = jedisCluster.hvals(combineKey);
			}

			values = parsePOJO(jsonDataList , clazz);
		} catch (Exception e) {
			logger.error("获取数据出错: key={}", combineKey, e);
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
		return values;
	}

	/**
	 * 获取hash结构的所有值 value为单个对象(数量超过100慎用,会阻塞)
	 *
	 * @param cacheKey
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> List<T> hgetAll(String cacheKey, Class<T> clazz) {
		return hgetAll(this.group , cacheKey ,clazz);
	}

	@Override
	public <T> Map<String ,List<T>> hgetListData(String cacheKey , List<String> cacheFieldList , Class<T> clazz){
		return hgetListData(this.group , cacheKey , cacheFieldList , clazz);
	}

	@Override
	public <T> Map<String ,List<T>> hgetListData(String group , String cacheKey , List<String> cacheFieldList , Class<T> clazz){
		Map<String ,List<T>> dataMap = Maps.newHashMap();
		Map<String , String> listValMap = getValues(group ,cacheKey , cacheFieldList ,false);
		for(Map.Entry<String,String> entry : listValMap.entrySet()){
			dataMap.put(entry.getKey() ,JSON.parseArray(entry.getValue() , clazz));
		}
		return dataMap;
	}

	@Override
	public <T> Map<String ,T> hgetMapData(String group ,String cacheKey , List<String> cacheFieldList , Class<T> clazz){
		Map<String ,T> dataMap = Maps.newHashMap();
		Map<String , String> listValMap = getValues(group ,cacheKey , cacheFieldList ,false);
		for(Map.Entry<String,String> entry : listValMap.entrySet()){
			dataMap.put(entry.getKey() ,JSON.parseObject(entry.getValue() , clazz));
		}
		return dataMap;
	}

	@Override
	public <T> Map<String ,T> hgetMapData(String cacheKey , List<String> cacheFieldList , Class<T> clazz){
		return hgetMapData(this.group , cacheKey ,cacheFieldList ,clazz);
	}

	/**
	 * 获取cacheField和value的对应缓存
	 *
	 * @param cacheKey
	 * @param cacheFieldList
	 * @param isReturnNullVal  是否返回CacheField未有值得null值
	 * @return
	 */
	protected Map<String , String> getValues(String group , String cacheKey , List<String> cacheFieldList , Boolean isReturnNullVal){
		Map<String, String> resMap = Maps.newHashMap();
		if(CollectionUtils.isNotEmpty(cacheFieldList)) {
			Jedis jds = null;
			ShardedJedis shardedJedis = null;
			JedisCluster jedisCluster = null;
			String combineKey = this.combineKey(group, cacheKey);
			try {
				jds = this.getJedis();
				shardedJedis = this.getShardedJedis();
				jedisCluster = this.getJedisCluster();
				List<String> jsonDataList = null;

				String[] cacheFieldArray = new String[cacheFieldList.size()];
				cacheFieldList.toArray(cacheFieldArray);

				if (jds != null) {
					jsonDataList = jds.hmget(combineKey, cacheFieldArray);
				} else if (shardedJedis != null) {
					jsonDataList = shardedJedis.hmget(combineKey, cacheFieldArray);
				} else if (jedisCluster != null) {
					jsonDataList = jedisCluster.hmget(combineKey, cacheFieldArray);
				}


				if (CollectionUtils.isNotEmpty(jsonDataList)) {
					for (int i = 0; i < cacheFieldArray.length; i++) {
						String jsonData = jsonDataList.get(i);
						String cacheField = cacheFieldArray[i];
						if (!checkRedisDataNotNull(jsonData)) {
							//值为null时 是否返回null值
							if (!isReturnNullVal) {
								continue;
							}
						}
						resMap.put(cacheField, jsonData);
					}
				}
			} catch (Exception e) {
				logger.error("获取数据出错: key={}", combineKey, e);
				throw new BizException(e.getMessage());
			} finally {
				this.shutdown(jds, shardedJedis ,jedisCluster);
			}
		}
		return resMap;
	}

	@Override
	public <T> List<T> hgetListValues(String group , String cacheKey , String[] cacheFields , Class<T> clazz){
		List<T> values = null;
		List<String> jsonDataList = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, cacheKey);
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			if (jds != null) {
				jsonDataList = jds.hmget(combineKey ,cacheFields);
			} else if (shardedJedis != null) {
				jsonDataList = shardedJedis.hmget(combineKey , cacheFields);
			} else if (jedisCluster != null) {
				jsonDataList = jedisCluster.hmget(combineKey , cacheFields);
			}

			values = parseList(jsonDataList , clazz);
		} catch (Exception e) {
			logger.error("获取数据出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
		return values;
	}

	@Override
	public <T> List<T> hgetListValues(String cacheKey , String[] cacheFields , Class<T> clazz){
		return hgetListValues(this.group , cacheKey ,cacheFields ,clazz);
	}

	@Override
	public <T> List<T> hgetListValues(String group , String cacheKey , String cacheField , Class<T> clazz){
		List<T> values = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, cacheKey);
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			String jsonData = null;
			if (jds != null) {
				jsonData = jds.hget(combineKey ,cacheField);
			} else if (shardedJedis != null) {
				jsonData = shardedJedis.hget(combineKey , cacheField);
			} else if (jedisCluster != null) {
				jsonData = jedisCluster.hget(combineKey , cacheField);
			}

			values = checkRedisDataNotNull(jsonData) ? JSON.parseArray(jsonData , clazz) : Lists.newArrayList();
		} catch (Exception e) {
			logger.error("获取数据出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis , jedisCluster);
		}
		return values;
	}

	@Override
	public <T> List<T> hgetListValues(String cacheKey , String cacheField , Class<T> clazz){
		return hgetListValues(this.group , cacheKey ,cacheField ,clazz);
	}

	@Override
	public JSONObject hgetJSONObj(String group , String cacheKey , String cacheField) {
		JSONObject jsonObject = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, cacheKey);
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			String dataJson = null;
			if (jds != null) {
				dataJson = jds.hget(combineKey , cacheField);
			} else if (shardedJedis != null) {
				dataJson = shardedJedis.hget(combineKey,cacheField);
			} else if (jedisCluster != null) {
				dataJson = jedisCluster.hget(combineKey,cacheField);
			}

			if (checkRedisDataNotNull(dataJson)){
				jsonObject = JSON.parseObject(dataJson);
			}
		} catch (Exception e) {
			logger.error("获取元素数据出错: key={},", combineKey,  e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
		return jsonObject;
	}

	@Override
	public JSONObject hgetJSONObj(String cacheKey , String cacheField) {
		return hgetJSONObj(this.group , cacheKey ,cacheField);
	}

	@Override
	public JSONObject getJSONObj(String group , String cacheKey) {
		JSONObject jsonObject = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, cacheKey);
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			String dataJson = null;
			if (jds != null) {
				dataJson = jds.get(combineKey);
			} else if (shardedJedis != null) {
				dataJson = shardedJedis.get(combineKey);
			} else if (jedisCluster != null) {
				dataJson = jedisCluster.get(combineKey);
			}

			if (checkRedisDataNotNull(dataJson)){
				jsonObject = JSON.parseObject(dataJson);
			}
		} catch (Exception e) {
			logger.error("获取元素数据出错: key={},", combineKey,  e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
		return jsonObject;
	}

	@Override
	public JSONObject getJSONObj(String cacheKey){
		return getJSONObj(this.group , cacheKey);
	}

	@Override
	public String get(String cacheKey) {
		return get(this.group , cacheKey);
	}

	@Override
	public String get(String group , String cacheKey){
		String cacheData = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, cacheKey);
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();

			if (jds != null) {
				cacheData = jds.get(combineKey);
			} else if (shardedJedis != null) {
				cacheData = shardedJedis.get(combineKey);
			} else if (jedisCluster != null) {
				cacheData = jedisCluster.get(combineKey);
			}

			if (!checkRedisDataNotNull(cacheData)){
				cacheData = null;
			}
		} catch (Exception e) {
			logger.error("获取元素数据出错: key={},", combineKey,  e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
		return cacheData;
	}


	@Override
	public Long hincrBy(String key, String field, Long count) {
		return hincrBy(this.group, key, field, count);
	}

	@Override
	public Long hincrBy(String group, String key, String field, Long count) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		long result = 0L;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				result = jds.hincrBy(combineKey, field, count.longValue()).longValue();
			} else if (shardedJedis != null) {
				result = shardedJedis.hincrBy(combineKey, field, count.longValue()).longValue();
			} else if (this.jedisCluster != null) {
				result = this.jedisCluster.hincrBy(combineKey, field, count.longValue()).longValue();
			}
		} catch (Exception e) {
			logger.error("删除map元素数据出错: key={}", key, e);
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return Long.valueOf(result);
	}

	protected static final String REDIS_RESP_OK = "OK";

	@Override
	public Boolean add(String group , String key, Object value, Integer seconds) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		boolean flag = false;
		String combineKey = combineKey(group ,key);
		String jsonValue = JSON.toJSONString(value);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			SetParams setParams = new SetParams();
			setParams.nx();
			setParams.ex(seconds);
			if (jds != null) {
				String responCode = jds.set(combineKey, jsonValue, setParams);
				flag =  (responCode != null) && (REDIS_RESP_OK.equalsIgnoreCase(responCode));
			}
			if (shardedJedis != null) {
				String responCode = shardedJedis.set(combineKey, jsonValue, setParams);
				flag =  (responCode != null) && (REDIS_RESP_OK.equalsIgnoreCase(responCode));
			}
			if (this.jedisCluster != null) {
				String responCode = this.jedisCluster.set(combineKey, jsonValue, setParams);
				flag =  (responCode != null) && (REDIS_RESP_OK.equalsIgnoreCase(responCode));
			}
		} catch (Exception e) {
			logger.error("新增缓存出错: key={}, value={}", combineKey, jsonValue, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Boolean add(String key, Object value, Integer seconds){
		return add(this.group ,key ,value , seconds);
	}

	@Override
	public <T> void lpush(String group, String key, List<T> value, Integer seconds) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineKey = combineKey(group, key);
		try {
			if ((value != null) && (!value.isEmpty())) {
				jds = getJedis();
				shardedJedis = getShardedJedis();
				if (jds != null) {
					jds.del(combineKey);
					String[] obj = getList(value);
					jds.lpush(combineKey, obj);
					expire(seconds, jds, combineKey);
				} else if (shardedJedis != null) {
					shardedJedis.del(combineKey);
					String[] obj = getList(value);
					shardedJedis.lpush(combineKey, obj);
					expire(seconds, shardedJedis, combineKey);
				} else if (this.jedisCluster != null) {
					this.jedisCluster.del(combineKey);
					String[] obj = getList(value);
					this.jedisCluster.lpush(combineKey, obj);
					expire(seconds, this.jedisCluster, combineKey);
				}
			}
		} catch (Exception e) {
			logger.error("REDIS队列 顺序存储出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
	}

	@Override
	public <T> void lpush(String key, List<T> value, Integer seconds){
		lpush(this.group , key ,value ,seconds);
	}

	@Override
	public <T> void rpush(String group, String key, List<T> value, Integer seconds) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineKey = combineKey(group, key);
		try {
			if ((value != null) && (!value.isEmpty())) {
				jds = getJedis();
				shardedJedis = getShardedJedis();
				if (jds != null) {
					jds.del(combineKey);
					String[] obj = getList(value);
					jds.rpush(combineKey, obj);
					expire(seconds, jds, combineKey);
				} else if (shardedJedis != null) {
					shardedJedis.del(combineKey);
					String[] obj = getList(value);
					shardedJedis.rpush(combineKey, obj);
					expire(seconds, shardedJedis, combineKey);
				} else if (this.jedisCluster != null) {
					this.jedisCluster.del(combineKey);
					String[] obj = getList(value);
					this.jedisCluster.rpush(combineKey, obj);
					expire(seconds, this.jedisCluster, combineKey);
				}
			}
		} catch (Exception e) {
			logger.error("REDIS队列反向存储出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
	}

	@Override
	public <T> void rpush(String key, List<T> value, Integer seconds){
		rpush(key , value , seconds);
	}

	protected <T> String[] getList(List<T> list) {
		String[] array = null;
		if(CollectionUtils.isNotEmpty(list)){
			array = new String[list.size()];
			int index = 0;
			for(T t : list){
				array[index ++] = JSON.toJSONString(t);
			}
		}
		return array;
	}

	@Override
	public <T> List<T> lget(String group, String key, Integer from, Integer to, Class<T> clazz) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		List<T> tlist = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			List<String> list = null;
			if (jds != null) {
				list = jds.lrange(combineKey, from.intValue(), to.intValue());
			} else if (shardedJedis != null) {
				list = shardedJedis.lrange(combineKey, from.intValue(), to.intValue());
			} else if (this.jedisCluster != null) {
				list = this.jedisCluster.lrange(combineKey, from.intValue(), to.intValue());
			}
			if (CollectionUtils.isNotEmpty(list)) {
				tlist = new ArrayList();
				for (String jsonData : list) {
					if (checkRedisDataNotNull(jsonData)) {
						tlist.add(JSON.parseObject(jsonData, clazz));
					}
				}
			}
			return tlist;
		}catch(Exception e){
			throw new BizException(e.getMessage());
		}finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
	}

	@Override
	public <T> List<T> lget(String key, Integer from, Integer to, Class<T> clazz){
		return lget(key , from ,to ,clazz);
	}

	protected void shutdown(Jedis jds, ShardedJedis shardedJedis , JedisCluster jedisCluster) {
		if (null != jds) {
			jds.close();
		}
		if (null != shardedJedis) {
			shardedJedis.close();
		}
		if(null != jedisCluster){

		}
	}

	@Override
	public Boolean expire(String group, String key, int seconds) {
		Boolean flag = true;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();

			long result = 0L;
			if (jds != null) {
				result = jds.expire(combineKey, seconds).longValue();
			} else if (shardedJedis != null) {
				result = shardedJedis.expire(combineKey, seconds).longValue();
			} else if (this.jedisCluster != null) {
				result = this.jedisCluster.expire(combineKey, seconds).longValue();
			}
			flag = result > 0L;
		} catch (Exception e) {
			logger.error("设置过期时间出错: key={}", key, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Boolean expire(String key, int seconds){
		return expire(this.group , key ,seconds);
	}

	protected void expire(int seconds, Jedis jds, String combineKey) {
		if (seconds > 0) {
			jds.expire(combineKey, seconds);
		}
	}

	protected void expire(int seconds, ShardedJedis shardedJedis, String combineKey) {
		if (seconds > 0) {
			shardedJedis.expire(combineKey, seconds);
		}
	}

	protected void expire(int seconds, JedisCluster jedisCluster, String combineKey) {
		if (seconds > 0) {
			jedisCluster.expire(combineKey, seconds);
		}
	}

	@Override
	public Set<String> getKeys(String group, String pattern) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineKey = combineKey(group, pattern);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			Set localSet;
			if (jds != null) {
				return jds.keys(combineKey);
			}
			if (shardedJedis != null) {
				return getKeys(combineKey, shardedJedis);
			}
			if (this.jedisCluster != null) {
				return getKeys(combineKey , jedisCluster);
			}
		} catch (Exception e) {
			logger.error("获取缓存的所有key时出错: key={}", combineKey, e);
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return new HashSet();
	}

	@Override
	public Set<String> getKeys(String pattern) {
		return getKeys(group, pattern);
	}

	@Override
	public Long zadd(String group, String key, Map<String, Long> scoreMembers) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineKey = combineKey(group, key);
		try {
			Map<String, Double> map = getMap(scoreMembers);
			jds = getJedis();
			shardedJedis = getShardedJedis();
			Long localLong;
			if (jds != null) {
				return jds.zadd(combineKey, map);
			}
			if (shardedJedis != null) {
				return shardedJedis.zadd(combineKey, map);
			}
			if (this.jedisCluster != null) {
				return this.jedisCluster.zadd(combineKey, map);
			}
		} catch (Exception e) {
			logger.error("将一个或多个member元素及其score值加入到有序集key当中时出错: key:{},scoreMembers:{}", combineKey, scoreMembers, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return null;
	}

	@Override
	public Long zadd(String key, Map<String, Long> scoreMembers){
		return zadd(this.group , key , scoreMembers);
	}

	@Override
	public Long zadd(String group, String key, Map<String, Long> scoreMembers, int seconds) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long localLong = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			Map<String, Double> map = getMap(scoreMembers);
			if (jds != null) {
				jds.expire(combineKey, seconds);
				localLong = jds.zadd(combineKey, map);
			}
			if (shardedJedis != null) {
				shardedJedis.expire(combineKey, seconds);
				localLong = shardedJedis.zadd(combineKey, map);
			}
			if (this.jedisCluster != null) {
				this.jedisCluster.expire(combineKey, seconds);
				localLong = this.jedisCluster.zadd(combineKey, map);
			}
		} catch (Exception e) {
			logger.error("将一个或多个member元素及其score值加入到有序集key当中时出错: key:{},scoreMembers:{}", combineKey, scoreMembers, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return localLong;
	}

	@Override
	public Long zadd(String key, Map<String, Long> scoreMembers, int seconds) {
		return zadd(this.group , key ,scoreMembers ,seconds);
	}

	@Override
	public Set<String> zrange(String group, String key, Long start, Long end) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Set<String> localSet = Sets.newHashSet();
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				localSet = jds.zrange(combineKey, start.longValue(), end.longValue());
			}
			if (shardedJedis != null) {
				localSet = shardedJedis.zrange(combineKey, start.longValue(), end.longValue());
			}
			if (this.jedisCluster != null) {
				localSet = this.jedisCluster.zrange(combineKey, start.longValue(), end.longValue());
			}
		} catch (Exception e) {
			logger.error("指定key、区间内正序分页时出错: key:{},start:{},end:{}", combineKey, start, end, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return localSet;
	}

	@Override
	public Set<String> zrange(String key, Long start, Long end){
		return zrange(this.group , key , start ,end);
	}

	@Override
	public Set<String> zrevrange(String group, String key, Long start, Long end) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Set<String> dataSet = Sets.newHashSet();
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				dataSet = jds.zrevrange(combineKey, start.longValue(), end.longValue());
			}
			if (shardedJedis != null) {
				dataSet = shardedJedis.zrevrange(combineKey, start.longValue(), end.longValue());
			}
			if (this.jedisCluster != null) {
				dataSet = this.jedisCluster.zrevrange(combineKey, start.longValue(), end.longValue());
			}
		} catch (Exception e) {
			logger.error("指定key、区间内反序分页时出错: key:{},start:{},end:{}", combineKey, start, end, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return dataSet;
	}

	@Override
	public Set<String> zrevrange(String key, Long start, Long end){
		return zrevrange(this.group , key ,start ,end);
	}

	@Override
	public Long zrem(String group, String key, List<String> members) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long res = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				res = jds.zrem(combineKey, members.toArray(new String[0]));
			}
			if (shardedJedis != null) {
				res = shardedJedis.zrem(combineKey, members.toArray(new String[0]));
			}
			if (this.jedisCluster != null) {
				res = this.jedisCluster.zrem(combineKey, members.toArray(new String[0]));
			}
		} catch (Exception e) {
			logger.error("移除有序集key中的一个或多个成员时出错: key:{},members:{}", combineKey, members, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return res;
	}

	@Override
	public Long zrem(String key, List<String> members){
		return zrem(this.group , key , members);
	}

	@Override
	public Long zcard(String group, String key) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long res = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				res = jds.zcard(combineKey);
			}
			if (shardedJedis != null) {
				res = shardedJedis.zcard(combineKey);
			}
			if (this.jedisCluster != null) {
				res = this.jedisCluster.zcard(combineKey);
			}
		} catch (Exception e) {
			logger.error("指定key返回有序集key的基数时出错: key:{}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return res;
	}

	@Override
	public Long zcard(String key){
		return zcard(this.group , key);
	}

	@Override
	public Long zscore(String group, String key, String members) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long res = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			Double scroce = null;
			if (jds != null) {
				scroce = jds.zscore(combineKey, members);
			} else if (shardedJedis != null) {
				scroce = shardedJedis.zscore(combineKey, members);
			} else if (this.jedisCluster != null) {
				scroce = this.jedisCluster.zscore(combineKey, members);
			}
			res = scroce != null ? Long.valueOf(Math.round(scroce.doubleValue())) : null;
		} catch (Exception e) {
			logger.error("指定key查询有序集key中，成员member的score值时出错: key:{},members:{}", combineKey, members, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return res;
	}

	@Override
	public Long zscore(String key, String members){
		return zscore(this.group , key ,members);
	}

	@Override
	public Long zcount(String group, String key, Long min, Long max) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long res = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				res = jds.zcount(combineKey, min.longValue(), max.longValue());
			}
			if (shardedJedis != null) {
				res = shardedJedis.zcount(combineKey, min.longValue(), max.longValue());
			}
			if (this.jedisCluster != null) {
				res = this.jedisCluster.zcount(combineKey, min.longValue(), max.longValue());
			}
		} catch (Exception e) {
			logger.error("指定key有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员时出错: key:{},min:{},max:{}", combineKey, min, max, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return res;
	}
	@Override
	public Long zcount(String key, Long min, Long max){
		return zcount(this.group , key , min ,max);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor) {
		return zscan(this.group, key, cursor);
	}

	@Override
	public ScanResult<Tuple> zscan(String group, String key, String cursor) {
		ScanResult<Tuple> result = null;

		Jedis jds = getJedis();
		ShardedJedis shardedJedis = getShardedJedis();
		String combineKey = combineKey(group, key);
		try {
			ScanParams params = new ScanParams();
			if (jds != null) {
				result = jds.zscan(combineKey, cursor, params.count(Integer.valueOf(1000)));
			} else if (shardedJedis != null) {
				result = shardedJedis.zscan(combineKey, cursor, params.count(Integer.valueOf(1000)));
			} else if (this.jedisCluster != null) {
				result = this.jedisCluster.zscan(combineKey.getBytes(), cursor.getBytes(), params.count(Integer.valueOf(1000)));
			}
		} catch (Exception e) {
			logger.error("指定key:{}, zscan异常", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return result;
	}

	@Override
	public Map<Long, String> zrangeWithScores(String group, String key, Long start, Long end) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Set<Tuple> list = new HashSet();
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				list = jds.zrangeWithScores(combineKey, start.longValue(), end.longValue());
			} else if (shardedJedis != null) {
				list = shardedJedis.zrangeWithScores(combineKey, start.longValue(), end.longValue());
			} else if (this.jedisCluster != null) {
				list = this.jedisCluster.zrangeWithScores(combineKey, start.longValue(), end.longValue());
			}
		} catch (Exception e) {
			logger.error("指定key、区间内正序分页时出错，key:{},start:{},end:{}", combineKey, start, end, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return getMap(list);
	}

	@Override
	public Map<Long, String> zrangeWithScores(String key, Long start, Long end){
		return zrangeWithScores(this.group , key ,start ,end);
	}

	@Override
	public Map<Long, String> zrevrangeWithScores(String group, String key, Long start, Long end) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Set<Tuple> list = new HashSet();
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				list = jds.zrevrangeWithScores(combineKey, start.longValue(), end.longValue());
			} else if (shardedJedis != null) {
				list = shardedJedis.zrevrangeWithScores(combineKey, start.longValue(), end.longValue());
			} else if (this.jedisCluster != null) {
				list = this.jedisCluster.zrevrangeWithScores(combineKey, start.longValue(), end.longValue());
			}
		} catch (Exception e) {
			logger.error("指定key、区间内反序分页时出错，key:{},start:{},end:{}", combineKey, start, end, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return getMap(list);
	}

	@Override
	public Map<Long, String> zrevrangeWithScores(String key, Long start, Long end) {
		return null;
	}

	@Override
	public Map<String, Long> zrevrangeByScores(String group, String key, Long start, Long end) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Set<Tuple> list = new HashSet();
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				list = jds.zrevrangeWithScores(combineKey, start.longValue(), end.longValue());
			} else if (shardedJedis != null) {
				list = shardedJedis.zrevrangeWithScores(combineKey, start.longValue(), end.longValue());
			} else if (this.jedisCluster != null) {
				list = this.jedisCluster.zrevrangeWithScores(combineKey, start.longValue(), end.longValue());
			}
		} catch (Exception e) {
			logger.error("指定key、区间内反序分页时出错，key:{},start:{},end:{}", combineKey, start, end, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return getMapByScore(list);
	}

	@Override
	public Map<String, Long> zrevrangeByScores(String key, Long start, Long end) {
		return zrevrangeByScores(this.group, key, start, end);
	}

	@Override
	public Boolean setnx(String group, String key, String value) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Boolean flag = true;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			Long res = 1L;
			if (jds != null) {
				res = jds.setnx(combineKey, value);
			} else if (shardedJedis != null) {
				res = shardedJedis.setnx(combineKey, value);
			} else if (this.jedisCluster != null) {
				res = this.jedisCluster.setnx(combineKey, value);
			}

            flag = RedisRespCodes.SUCCESS.equals(res);

		} catch (Exception e) {
			logger.error("指定key SETNX时出错，key:{},value:{}", combineKey, value, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Boolean setnx(String key, String value){
		return setnx(this.group , key , value);
	}

	/**
	 * 批量删除缓存 百万级key时禁止使用
	 * 推荐使用 delKeysByScan
	 *
	 * @param pattern
	 * @return 删除个数
	 */
	@Override
	public Long delKeys(String pattern) {
		long count = 0;
		if(StringUtils.isNotBlank(pattern)) {
			Jedis jds = null;
			ShardedJedis shardedJedis = null;
			JedisCluster jedisCluster = null;
			try {
				jds = this.getJedis();
				shardedJedis = this.getShardedJedis();
				jedisCluster = this.getJedisCluster();
				Set<String> keySet = null;
				if (jds != null) {
					keySet = jds.keys(pattern);
					if (CollectionUtils.isNotEmpty(keySet)) {
						String[] delKeyArray = new String[keySet.size()];
						count = jds.del(keySet.toArray(delKeyArray));
					}
				} else if (shardedJedis != null) {
					keySet = getKeys(pattern, shardedJedis);
					if (CollectionUtils.isNotEmpty(keySet)) {
						for (String delKey : keySet) {
							count += shardedJedis.del(delKey);
						}
					}
				} else if (jedisCluster != null) {
					keySet = getKeys(pattern);
					if (CollectionUtils.isNotEmpty(keySet)) {
						String[] delKeyArray = new String[keySet.size()];
						count = jedisCluster.del(keySet.toArray(delKeyArray));
					}
				}
			} catch (Exception e) {
				logger.error("删除元素数据出错: key={},", pattern, e);
			} finally {
				this.shutdown(jds, shardedJedis , jedisCluster);
			}
		}
		return count;
	}

	/**
	 * 使用游标删除key
	 *
	 * @param pattern
	 * @return
	 */
	@Override
	public Long delKeysByScan(String pattern) {
		long delCount = 0;
		if(StringUtils.isNotBlank(pattern)) {
			Jedis jds = this.getJedis();
			ShardedJedis shardedJedis = this.getShardedJedis();
			JedisCluster jedisCluster = this.getJedisCluster();
			if(shardedJedis != null){
				throw new BizException("shardedJedis 不支持 scan方法");
			}

			String cursor = ScanParams.SCAN_POINTER_START;
			ScanParams scanParams = new ScanParams();
			if(pattern.endsWith(PATTERN_CHAR)) {
				scanParams.match(pattern);
			}else {
				scanParams.match(pattern + PATTERN_CHAR);
			}
			scanParams.match(pattern);
			scanParams.count(10000);

			try {
				List<String> keyList = null;

				if (jds != null) {
					do {
						//使用hscan命令获取500条数据，使用cursor游标记录位置，下次循环使用
						ScanResult<String> scanResult = null;
						if(jds != null) {
							scanResult =jds.scan(cursor, scanParams);
						}
						if(jedisCluster != null){
							scanResult = jedisCluster.scan(cursor, scanParams);
						}

						keyList = scanResult.getResult();
						if (CollectionUtils.isNotEmpty(keyList)) {
							String[] keyArray = new String[keyList.size()];
							jds.del(keyList.toArray(keyArray));
							delCount += keyArray.length;
						}
						// 返回0 说明遍历完成
						cursor = scanResult.getCursor();
					} while (!ScanParams.SCAN_POINTER_START.equals(cursor));
				}
			} catch (Exception e) {
				logger.error("读取缓存出错: key={}", pattern, e);
			} finally {
				this.shutdown(jds, shardedJedis , jedisCluster);
			}
		}
		return delCount;
	}

	/**
	 * 批量执行setnx
	 *
	 * @param dataMap
	 * @return
	 */
	@Override
	public Long msetnx(Map<String, String> dataMap) {
		Long result = 0L;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			String[] keysValues = new String[dataMap.size() * 2];

			String combineKey = null;
			int index = 0;
			for (Map.Entry<String, String> data : dataMap.entrySet()) {
				combineKey = this.combineKey(group, data.getKey());
				keysValues[index++] = combineKey;
				keysValues[index++] = data.getValue();
			}


			if (jds != null) {
				result = jds.msetnx(keysValues);
			} else if (shardedJedis != null) {
				result = 1L;
				for (Map.Entry<String, String> data : dataMap.entrySet()) {
					combineKey = this.combineKey(group, data.getKey());
					result = shardedJedis.setnx(combineKey , data.getValue());
					if(Long.valueOf(0).equals(result)){
						break;
					}
				}
			} else if (jedisCluster != null) {
				result = jedisCluster.msetnx(keysValues);
			}
		} catch (Exception e) {
			logger.error("mset数据出错 , data : {}", JSON.toJSONString(dataMap));
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis , jedisCluster);
		}
		return result;
	}

	/**
	 * 批量删除
	 *
	 * @param cacheKeyList
	 */
	@Override
	public void mdel(List<String> cacheKeyList) {
		if(CollectionUtils.isNotEmpty(cacheKeyList)) {
			Jedis jds = null;
			ShardedJedis shardedJedis = null;
			JedisCluster jedisCluster = null;
			List<String> combineKeyList = cacheKeyList.stream().map(key -> combineKey(this.group , key)).collect(Collectors.toList());
			String[] delKeyArray = new String[cacheKeyList.size()];
			try {
				jds = this.getJedis();
				shardedJedis = this.getShardedJedis();
				jedisCluster = this.getJedisCluster();
				if (jds != null) {
					jds.del(combineKeyList.toArray(delKeyArray));
				} else if (shardedJedis != null) {
					for (String delKey : combineKeyList) {
						shardedJedis.del(delKey);
					}
				} else if (jedisCluster != null) {
					jedisCluster.del(combineKeyList.toArray(delKeyArray));
				}
			} catch (Exception e) {
				logger.error("获取元素数据出错: key={},", cacheKeyList, e);
			} finally {
				this.shutdown(jds, shardedJedis , jedisCluster);
			}
		}
	}

	/**
	 * Test for existence of a specified field in a hash. <b>Time complexity:</b> O(1)
	 *
	 * @param key   key
	 * @param field field
	 * @return Return 1 if the hash stored at key contains the specified field. Return 0 if the key is
	 * not found or the field is not present.
	 */
	@Override
	public Boolean hexists(String key, String field) {
		return hexists(this.group , key , field);
	}


	@Override
	public Boolean hexists(String group, String key, String field) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;

		String combineKey = this.combineKey(group, key);
		jds = this.getJedis();
		shardedJedis = this.getShardedJedis();
		jedisCluster = this.getJedisCluster();
		try {
			if (jds != null) {
				return jds.hexists(combineKey, field);
			} else if (shardedJedis != null) {
				return shardedJedis.hexists(combineKey, field);
			} else if (jedisCluster != null) {
				return jedisCluster.hexists(combineKey, field);
			}
		} catch (Exception e) {
			logger.error("获取元素数据出错: key={},", key, e);
		} finally {
			this.shutdown(jds, shardedJedis , jedisCluster);
		}
		return false;
	}

	/**
	 * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
	 * returned. Note that even keys set with an empty string as value will return "1". Time
	 * complexity: O(1)
	 *
	 * @param key key
	 * @return Boolean reply, true if the key exists, otherwise false
	 */
	@Override
	public Boolean exists(String key) {
		return exists(this.group , key);
	}

	@Override
	public Boolean exists(String group, String key) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;

		String combineKey = this.combineKey(group, key);
		jds = this.getJedis();
		shardedJedis = this.getShardedJedis();
		jedisCluster = this.getJedisCluster();
		try {
			if (jds != null) {
				return jds.exists(combineKey);
			} else if (shardedJedis != null) {
				return shardedJedis.exists(combineKey);
			} else if (jedisCluster != null) {
				return jedisCluster.exists(combineKey);
			}
		}catch (Exception e) {
			logger.error("获取元素数据出错: key={},", key, e);
		} finally {
			this.shutdown(jds, shardedJedis , jedisCluster);
		}
		return false;
	}

	/**
	 * 获取列表缓存的长度
	 *
	 * @param cacheKey
	 * @return
	 */
	@Override
	public Long llen(String cacheKey) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, cacheKey);

		Long count = 0L;

		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			if (jds != null) {
				count = jds.llen(combineKey);
			} else if (shardedJedis != null) {
				count = shardedJedis.llen(combineKey);
			} else if (jedisCluster != null) {
				count = jedisCluster.llen(combineKey);
			}

		} catch (Exception e) {
			logger.error("读取缓存出错: key={}", combineKey, e);
		} finally {
			this.shutdown(jds, shardedJedis , jedisCluster);
		}

		return count;
	}

	/**
	 * 向队尾中插入一个字符value
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public void rpush(String key, String value) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, key);

		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			if (jds != null) {
				jds.rpush(combineKey, value);
			} else if (shardedJedis != null) {
				shardedJedis.rpush(combineKey, value);
			} else if (jedisCluster != null) {
				jedisCluster.rpush(combineKey, value);
			}
		} catch (Exception e) {
			logger.error("REDIS队列反向存储出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
	}

	/**
	 * 向队头中插入一个字符value
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public void lpush(String key, String value) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, key);

		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			if (jds != null) {
				jds.rpush(combineKey, value);
			} else if (shardedJedis != null) {
				shardedJedis.lpush(combineKey, value);
			} else if (jedisCluster != null) {
				jedisCluster.lpush(combineKey, value);
			}
		} catch (Exception e) {
			logger.error("REDIS队列反向存储出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
	}

	/**
	 * 根据count移除队列中匹配value的值
	 *
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	@Override
	public long lrem(String key, long count, String value) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, key);
		long rcount = 0L;
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			jedisCluster = this.getJedisCluster();
			if (jds != null) {
				rcount = jds.lrem(combineKey, count, value);
			} else if (shardedJedis != null) {
				rcount = shardedJedis.lrem(combineKey, count, value);
			} else if (jedisCluster != null) {
				rcount = jedisCluster.lrem(combineKey, count, value);
			}
		} catch (Exception e) {
			logger.error("REDIS队列反向存储出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
		return rcount;
	}

	/**
	 * 获取有序集合里member对应的排名
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	@Override
	public long zRank(String key, String member) {
		long result = -1;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		JedisCluster jedisCluster = null;
		String combineKey = this.combineKey(group, key);

		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			if (jds != null) {
				result = jds.zrank(key,member);
			} else if (shardedJedis != null) {
				result = shardedJedis.zrank(key,member);
			} else if (jedisCluster != null) {
				result = jedisCluster.zrank(key,member);
			}
		} catch (Exception e) {
			logger.error("查询有序集合排名出错: key={}", combineKey, e);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis ,jedisCluster);
		}
		return result;
	}

	protected Map<String, Double> getMap(Map<String, Long> scoreMembers) {
		Map<String, Double> map = new HashMap();
		if (MapUtils.isNotEmpty(scoreMembers)) {
			for (Map.Entry<String, Long> m : scoreMembers.entrySet()) {
				map.put(m.getKey(), Double.valueOf(m.getValue().longValue()));
			}
		}
		return map;
	}

	public Set<String> getKeys(String pattern, ShardedJedis shardedJedis) {
		Set<String> list = new HashSet();
		java.util.Collection<Jedis> allShards = shardedJedis.getAllShards();
		for (Jedis jedis : allShards) {
			Set<String> keys = jedis.keys(pattern);
			list.addAll(keys);
		}
		return list;
	}

	public Set<String> getKeys(String pattern , JedisCluster jedisCluster) {
		logger.debug("Start getting keys...");
		Set<String> keys = new HashSet();
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for (String k : clusterNodes.keySet()) {
			logger.debug("Getting keys from: {}", k);
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(connection.keys(pattern));
			} catch (Exception e) {
				logger.error("Getting keys error: {}", e);
			} finally {
				logger.debug("Connection closed.");
				connection.close();
			}
		}
		logger.debug("Keys gotten!");
		return keys;
	}

	@Override
	public Set<String> hkeys(String key) {
		return hkeys(this.group, key);
	}

	@Override
	public Set<String> hkeys(String group, String key) {
		logger.debug("start to hkeys------------------");
		String combineKey = combineKey(group, key);
		Set<String> keySet = null;
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (null != jds) {
				keySet = jds.hkeys(combineKey);
			} else if (null != shardedJedis) {
				keySet = shardedJedis.hkeys(combineKey);
			} else if (null != this.jedisCluster) {
				keySet = this.jedisCluster.hkeys(combineKey);
			}

		} catch (Exception e) {
			logger.error("指定key:{}，hkeys异常", combineKey, e);
		}finally {
			shutdown(jds , shardedJedis , jedisCluster);
		}
		return keySet;
	}

	@Override
	public Long getToLiveTime(String group, String key) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		long result = 0L;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				result = jds.ttl(combineKey).longValue();
			} else if (shardedJedis != null) {
				result = shardedJedis.ttl(combineKey).longValue();
			} else if (this.jedisCluster != null) {
				result = this.jedisCluster.ttl(combineKey).longValue();
			}
		} catch (Exception e) {
			logger.error("设置过期时间出错: key={}", key, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return result;
	}

	@Override
	public Long getToLiveTime(String key){
		return getToLiveTime(this.group , key);
	}

	/**
	 * 订阅消息
	 *
	 * @param group
	 * @param channel
	 * @param processor
	 */
	@Override
	public void subscribe(String group, String channel, IRedisSubProcessor<String> processor) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineChannel = combineKey(group, channel);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				jds.subscribe(ListenerConver.conver(processor), combineChannel);
			} else {
				if (shardedJedis != null) {
					throw new RuntimeException("shardedJedis不支持该方法！");
				}
				if (this.jedisCluster != null) {
					this.jedisCluster.subscribe(ListenerConver.conver(processor), combineChannel);
				}
			}
		} catch (Exception e) {
			logger.error("订阅消息异常: channel={}", combineChannel, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
	}

	/**
	 * 订阅消息
	 *
	 * @param channel
	 * @param processor
	 */
	@Override
	public void subscribe(String channel, IRedisSubProcessor<String> processor) {
		subscribe(this.group , channel , processor);
	}

	/**
	 * 发布消息
	 *
	 * @param group
	 * @param channel
	 * @param message
	 */
	@Override
	public void publish(String group, String channel, String message) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineChannel = combineKey(group, channel);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				jds.publish(combineChannel, message);
			} else {
				if (shardedJedis != null) {
					throw new RuntimeException("shardedJedis不支持该方法！");
				}
				if (this.jedisCluster != null) {
					this.jedisCluster.publish(combineChannel, message);
				}
			}
		} catch (Exception e) {
			logger.error("发布消息异常: channel={}", combineChannel, e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
	}

	@Override
	public void publish(String channel, String message){
		publish(this.group , channel ,message);
	}


	@Override
	public Boolean mset(String group, Map<String, String> dataMap) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String result = "";
		boolean flag = false;
		String[] combineKeysvalues = combineKeyValues(group, dataMap);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				result = jds.mset(combineKeysvalues);
			} else {
				if (shardedJedis != null) {
					throw new RuntimeException("shardedJedis不支持该方法！");
				}
				if (this.jedisCluster != null) {
					result = this.jedisCluster.mset(combineKeysvalues);
				}
			}
			if ("OK".equalsIgnoreCase(result)) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("mset异常: {}", e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Boolean pfadd(String group, String key, String[] elements) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		long result = 0L;
		boolean flag = false;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				result = jds.pfadd(combineKey, elements).longValue();
			} else if (shardedJedis != null) {
				result = shardedJedis.pfadd(combineKey, elements).longValue();
			} else if (this.jedisCluster != null) {
				result = this.jedisCluster.pfadd(combineKey, elements).longValue();
			}
			if (result == 1L) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("pfadd异常: {}", e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Long pfcount(String group, String key) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineKey = combineKey(group, key);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			long l;
			if (jds != null) {
				return jds.pfcount(combineKey);
			}
			if (shardedJedis != null) {
				return shardedJedis.pfcount(combineKey);
			}
			if (this.jedisCluster != null) {
				return this.jedisCluster.pfcount(combineKey);
			}
		} catch (Exception e) {
			logger.error("pfcount异常: {}", e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return 0L;
	}

	@Override
	public Long pfcount(String group, String[] keys) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String[] combineKeys = combineKeys(group, keys);
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				return jds.pfcount(combineKeys);
			}
			if (shardedJedis != null) {
				throw new RuntimeException("shardedJedis不支持该方法！");
			}
			if (this.jedisCluster != null) {
				return this.jedisCluster.pfcount(combineKeys);
			}
		} catch (Exception e) {
			logger.error("pfcount异常: {}", e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return 0L;
	}

	@Override
	public Boolean pfmerge(String group, String destKey, String[] sourceKeys) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		String combineDestKey = combineKey(group, destKey);
		String[] combineSourceKeys = combineKeys(group, sourceKeys);
		String result = "";
		boolean flag = false;
		try {
			jds = getJedis();
			shardedJedis = getShardedJedis();
			if (jds != null) {
				result = jds.pfmerge(combineDestKey, combineSourceKeys);
			} else {
				if (shardedJedis != null) {
					throw new RuntimeException("shardedJedis不支持该方法！");
				}
				if (this.jedisCluster != null) {
					result = this.jedisCluster.pfmerge(combineDestKey, combineSourceKeys);
				}
			}
			if ("OK".equalsIgnoreCase(result)) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("pfmerge异常: {}", e);
			throw new BizException(e.getMessage());
		} finally {
			shutdown(jds, shardedJedis, jedisCluster);
		}
		return flag;
	}

	@Override
	public Long zrevRank(String key, String member) {
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		Long value = null;
		String combineKey = this.combineKey(group, key);

		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			if (jds != null) {
				value = jds.zrevrank(combineKey, member);
			} else if (shardedJedis != null) {
				value = shardedJedis.zrevrank(combineKey, member);
			} else if (this.jedisCluster != null) {
				value = this.jedisCluster.zrevrank(combineKey, member);
			}

		} catch (Exception var13) {
			logger.error("读取缓存出错: key={}", combineKey, var13);
			throw new BizException(var13.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis, this.jedisCluster);
		}

		return value;
	}

	@Override
	public <T> T eval(String script, List<String> keys, List<String> args , Class<T> clazz){
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		T res = null;
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			Object obj = null;
			if (jds != null) {
				obj = jds.eval(script , keys , args);
			} else if (shardedJedis != null) {
				throw new BizException("shardedJedis not support eval!");
			} else if (this.jedisCluster != null) {
				obj = this.jedisCluster.eval(script , keys , args);
			}
			if(obj != null){
				res = JSON.parseObject(JSON.toJSONString(obj) , clazz);
			}
		} catch (Exception e) {
			logger.error("执行脚本出错: script={} , keys={} , args={}", script, keys , args);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis, this.jedisCluster);
		}
		return res;
	}

	@Override
	public <T> T evalSha(String shaCode, List<String> keys, List<String> args , Class<T> clazz){
		Jedis jds = null;
		ShardedJedis shardedJedis = null;
		T res = null;
		try {
			jds = this.getJedis();
			shardedJedis = this.getShardedJedis();
			Object obj = null;
			if (jds != null) {
				obj = jds.evalsha(shaCode , keys , args);
			} else if (shardedJedis != null) {
				throw new BizException("shardedJedis not support evalsha!");
			} else if (this.jedisCluster != null) {
				obj = this.jedisCluster.evalsha(shaCode , keys , args);
			}
			if(obj != null){
				res = JSON.parseObject(JSON.toJSONString(obj) , clazz);
			}
		} catch (Exception e) {
			logger.error("执行脚本出错: script={} , keys={} , args={}", shaCode, keys , args);
			throw new BizException(e.getMessage());
		} finally {
			this.shutdown(jds, shardedJedis, this.jedisCluster);
		}
		return res;
	}
}
