package org.opensourceframework.demo.idempotent.biz.cache;

import org.opensourceframework.demo.idempotent.biz.dao.eo.DemoUserEo;
import org.opensourceframework.component.redis.cache.service.IRedisCacheService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 缓存使用示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class DemoUserCache {
	@Resource
	private IRedisCacheService redisCacheService;

	public void setCache(DemoUserEo demoUserEo){
		redisCacheService.set(demoUserEo.getId().toString() , demoUserEo , 0);
	}

	public void getCache(String pkId){
		redisCacheService.get(pkId , DemoUserEo.class);
	}
}
