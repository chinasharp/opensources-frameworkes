#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.cache;

import ${package}.biz.dao.eo.DemoEo;
import ${groupId}.component.redis.cache.service.IRedisCacheService;
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
public class DemoCache {
	@Resource
	private IRedisCacheService redisCacheService;

	public void setCache(DemoEo demoUserEo){
		redisCacheService.set(demoUserEo.getId().toString() , demoUserEo , 0);
	}

	public void getCache(String pkId){
		redisCacheService.get(pkId , DemoEo.class);
	}
}
