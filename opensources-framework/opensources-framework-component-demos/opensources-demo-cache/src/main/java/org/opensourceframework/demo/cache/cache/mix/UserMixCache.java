package org.opensourceframework.demo.cache.cache.mix;

import org.opensourceframework.demo.cache.entity.UserInfo;
import org.opensourceframework.starter.mixcache.MixCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 本地缓存+分布式的混合缓存
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
@Component
public class UserMixCache {
    private static final String USER_INFO_CACHE_KEY = "userInfo:";

    @Autowired
    private MixCacheService mixCacheService;

    public void setCache(UserInfo userInfo) {
        if(userInfo == null || userInfo.getId() == null){
            return;
        }
        mixCacheService.setCache(getCacheKey(userInfo.getId()) , userInfo);
    }

    public UserInfo getCache(Long userId) {
        UserInfo userInfo = mixCacheService.getCache(getCacheKey(userId) , UserInfo.class);
        return userInfo;
    }

    public void delCache(Long userId){
        mixCacheService.delCache(getCacheKey(userId) , UserInfo.class);
    }

    private String getCacheKey(Object key){
        return USER_INFO_CACHE_KEY.concat(key.toString());
    }

}
