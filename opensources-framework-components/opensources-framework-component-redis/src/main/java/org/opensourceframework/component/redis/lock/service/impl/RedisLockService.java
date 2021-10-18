package org.opensourceframework.component.redis.lock.service.impl;

import org.opensourceframework.component.redis.common.RedissonService;
import org.opensourceframework.component.redis.lock.service.ILockService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁redission实现
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 */
public class RedisLockService extends RedissonService implements ILockService {
	private static final Logger logger = LoggerFactory.getLogger(RedisLockService.class);
	private static final String LOCK_PREFIX = "lock:";
	private static final String DEF_TABLE_NAME = "baseEo";
	private static final String SPLIT_CHAR = ":";
	private final RedissonClient redisson;

	public RedisLockService(RedissonClient redisson){
		this.redisson = redisson;
	}

	/**
	 * 获取分布式锁 waitTimeout默认0秒  自动释放默认1秒
	 *
	 * @param lockKey     关键key      比如:orderNo
	 * @return
	 */
	@Override
	public Boolean lock(Serializable lockKey) {
		return lock(DEF_TABLE_NAME, lockKey, 0, 1, TimeUnit.SECONDS);
	}


	/**
	 * 获取分布式锁 waitTimeout默认0秒  自动释放默认1秒
	 *
	 * @param tableName      实体名/表名   比如:OrderEo
	 * @param lockKey     关键key      比如:orderNo
	 * @return
	 */
	@Override
	public Boolean lock(String tableName, Serializable lockKey) {
		if(StringUtils.isBlank(tableName)){
			tableName = DEF_TABLE_NAME;
		}
		return lock(tableName, lockKey, 0, 1, TimeUnit.SECONDS);
	}

	/**
	 * 获取分布式锁
	 *
	 * @param tableName      实体名/表名   比如:OrderEo
	 * @param lockKey     关键key      比如:orderNo
	 * @param waitTimeout 获取锁的等待时间
	 * @param leaseTime   自动释放锁的时间
	 * @param timeUnit    时间单位
	 * @return
	 */
	@Override
	public Boolean lock(String tableName, Serializable lockKey, Integer waitTimeout, Integer leaseTime,
			TimeUnit timeUnit) {
		String redisLockKey = buildLockKey(tableName, lockKey);
		RLock rLock = this.redisson.getLock(redisLockKey);
		Boolean isLocked = true;
		try {
			isLocked = rLock.tryLock(waitTimeout, leaseTime, timeUnit);
		} catch (Exception e) {
			isLocked = false;
			logger.error("get redis lock error.msg:{}" , e.getMessage());
		}
		return isLocked;
	}

	/**
	 * 释放锁
	 *
	 * @param tableName
	 * @param lockKey
	 * @return
	 */
	@Override
	public Boolean unlock(String tableName, Serializable lockKey) {
		Boolean flag = true;
		if(lockKey == null){
			flag = false;
		}else {
			if (StringUtils.isBlank(tableName)) {
				tableName = DEF_TABLE_NAME;
			}
			try {
				RLock rLock = this.redisson.getLock(buildLockKey(tableName, lockKey));
				rLock.unlock();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 是否已锁
	 *
	 * @param tableName
	 * @param lockKey
	 * @return
	 */
	@Override
	public Boolean isLocked(String tableName, Serializable lockKey) {
		String redisLockKey = buildLockKey(tableName, lockKey);
		RLock rLock = this.redisson.getLock(redisLockKey);
		return rLock.isLocked();
	}

	private String buildLockKey(String tableName , Serializable lockKey){
		return LOCK_PREFIX.concat(tableName).concat(SPLIT_CHAR).concat(lockKey.toString());
	}
}
