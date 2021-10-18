package org.opensourceframework.component.redis.lock.service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface ILockService {
	/**
	 * 获取分布式锁 waitTimeout默认0秒  自动释放默认1秒 
	 *
	 * @param lockKey     关键key      比如:orderNo
	 * @return
	 */
	Boolean lock(Serializable lockKey);

	/**
	 * 获取分布式锁 waitTimeout默认0秒  自动释放默认1秒
	 *
	 * @param tableName      实体名/表名   比如:OrderEo
	 * @param lockKey     关键key      比如:orderNo
	 * @return
	 */
	Boolean lock(String tableName ,Serializable lockKey);

	/**
	 * 获取分布式锁
	 *
	 * @param tableName         实体名/表名   比如:OrderEo
	 * @param lockKey          关键key      比如:orderNo
	 * @param waitTimeout      获取锁的等待时间
	 * @param leaseTime        自动释放锁的时间
	 * @param timeUnit         时间单位
	 * @return
	 */
	Boolean lock(String tableName ,Serializable lockKey , Integer waitTimeout, Integer leaseTime, TimeUnit timeUnit);

	/**
	 * 释放锁
	 *
	 * @param tableName      实体名/表名   比如:OrderEo
	 * @param lockKey        关键key      比如:orderNo
	 * @return
	 */
	Boolean unlock(String tableName ,Serializable lockKey);

	/**
	 * 是否已锁
	 *
	 * @param bizCode
	 * @param lockKey
	 * @return
	 */
	Boolean isLocked(String bizCode ,Serializable lockKey);

}
