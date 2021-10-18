package org.opensourceframework.starter.mixcache;

import org.opensourceframework.component.redis.cache.service.impl.RedisCacheService;
import org.opensourceframework.starter.ehcache.EhcacheAutoConfiguration;
import org.opensourceframework.starter.ehcache.EhcacheService;
import org.opensourceframework.starter.mixcache.config.MixCacheConfig;
import org.opensourceframework.starter.mixcache.mq.CacheDataAsyncSend;
import org.opensourceframework.starter.rediscache.RedisCacheAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ehcache 自动装载类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/15 下午4:11
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties({MixCacheConfig.class})
@AutoConfigureAfter(value = {EhcacheAutoConfiguration.class , RedisCacheAutoConfiguration.class})
public class MixCacheAutoConfiguration {
	@Autowired
	private MixCacheConfig mixCacheConfig;

	@Autowired
	private RedisCacheService remoteCache;

	@Autowired
	private EhcacheService localCache;

	@Autowired
	private CacheDataAsyncSend cacheDataAsyncSend;

	@Bean
	public MixCacheService mixCacheService(MixCacheConfig mixCacheConfig, RedisCacheService remoteCache, EhcacheService localCache, CacheDataAsyncSend cacheDataAsyncSend){
		return new MixCacheService(mixCacheConfig , remoteCache , localCache , cacheDataAsyncSend);
	}
}
