package org.opensourceframework.starter.ehcache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Ehcache 自动装载类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/15 下午4:11
 */
@Configuration
@EnableCaching
public class EhcacheAutoConfiguration {
	@Bean
	public EhcacheService ehcacheService(){
		EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
		ClassPathResource resource = new ClassPathResource("ehcache.xml");
		factoryBean.setConfigLocation(resource);
		factoryBean.setShared(true);
		return new EhcacheService(factoryBean.getObject());
	}
}
