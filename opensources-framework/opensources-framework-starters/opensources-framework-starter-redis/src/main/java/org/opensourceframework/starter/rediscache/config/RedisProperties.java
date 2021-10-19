package org.opensourceframework.starter.rediscache.config;

import org.opensourceframework.component.redis.cache.config.RedisConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Redis服务器链接配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.redis.registryvo")
public class RedisProperties extends RedisConfig {
	public RedisProperties() {
	}
}
