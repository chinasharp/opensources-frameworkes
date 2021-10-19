package org.opensourceframework.demo.cache.cache.local;

import org.opensourceframework.demo.cache.entity.UserInfo;
import org.opensourceframework.starter.ehcache.EhcacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 本地缓存
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 * 注:使用本地缓存,需要配置ehcache.xml
 *     <cache  name="UserInfo"
 *             eternal="false"
 *             maxElementsInMemory="50000"
 *             overflowToDisk="false"
 *             diskPersistent="false"
 *             timeToIdleSeconds="0"
 *             timeToLiveSeconds="0"
 *             memoryStoreEvictionPolicy="LRU"/>
 * name为缓存对象的全路径类名
 */
@Component
public class UserLocalCache {
    private static final String USER_INFO_CACHE_KEY = "userInfo:";

    @Autowired
    private EhcacheService ehcacheService;

    public void setCache(UserInfo userInfo){
        ehcacheService.setCache(getCacheKey(userInfo) , userInfo);
    }

    public UserInfo getCache(Long userId){
        return ehcacheService.getCache(getCacheKey(userId) , UserInfo.class);
    }

    public void delCache(Long userId){
        ehcacheService.delCache(getCacheKey(userId) , UserInfo.class);
    }

    private String getCacheKey(Object key){
        return USER_INFO_CACHE_KEY.concat(key.toString());
    }
}
