package org.opensourceframework.starter.rediscache;

import org.opensourceframework.component.redis.bloomFilter.service.impl.BloomFilterService;
import org.opensourceframework.component.redis.cache.config.RedisConfig;
import org.opensourceframework.component.redis.cache.service.impl.RedisCacheService;
import org.opensourceframework.component.redis.common.RedissonService;
import org.opensourceframework.component.redis.lock.service.impl.RedisLockService;
import org.opensourceframework.starter.rediscache.config.RedisProperties;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Redis相关功能:缓存、分布式锁、布鲁过滤器等Auto Config
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@EnableConfigurationProperties({RedisProperties.class})
public class RedisCacheAutoConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(RedisCacheAutoConfiguration.class);
	@Autowired
	private RedisConfig redisConfig;

	@Value("${spring.application.name}")
	private String group;

	@Bean
	public RedisCacheService redisCacheService(){
		return new RedisCacheService(group , redisConfig);
	}

	@Bean("redissonClient")
	public RedissonClient redissonClient(){
		return RedissonService.buildRedissonClient(redisConfig);
	}

    @Bean
	public RedisLockService redisLockService(@Qualifier("redissonClient") RedissonClient redissonClient){
		return new RedisLockService(redissonClient);
	}

	@Bean
	public BloomFilterService bloomFilterService(@Qualifier("redissonClient") RedissonClient redissonClient , RedisConfig redisConfig) {
		return new BloomFilterService(redissonClient , redisConfig);
	}

	public RedisConfig getRedisConfig() {
		return redisConfig;
	}

	public void setRedisConfig(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
