package org.opensourceframework.demo.cache.cache.remote;

import org.opensourceframework.demo.cache.entity.UserInfo;
import org.opensourceframework.component.redis.cache.service.impl.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分布式缓存
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Component
public class UserRemoteCache {
    private static final String USER_INFO_CACHE_KEY = "userInfo:";
    @Autowired
    private RedisCacheService redisCacheService;

    public void setCache(UserInfo userInfo){
        redisCacheService.set(getCacheKey(userInfo.getId()) , userInfo , 0);
    }

    public UserInfo getCache(Long userId){
        return redisCacheService.get(getCacheKey(userId) , UserInfo.class);
    }

    public void delCache(Long userId){
        redisCacheService.del(getCacheKey(userId));
    }

    private String getCacheKey(Object key){
        return USER_INFO_CACHE_KEY.concat(key.toString());
    }
}
