package org.opensourceframework.starter.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class EhcacheService {
    private final CacheManager cacheManager;

    public EhcacheService(CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }

    public <T> Boolean setCache(String cacheKey , T info){
        if(StringUtils.isBlank(cacheKey) || info == null){
            return false;
        }
        Boolean isSuccess = true;
        try {
            Cache cache = getEhCache(info.getClass());
            Element element = new Element(cacheKey, info);
            cache.put(element);
        } catch (Exception e) {
            isSuccess = false;
        }
        return isSuccess;
    }

    public <T> T getCache(String cacheKey , Class<T> clazz) {
        T entity = null;
        if (StringUtils.isBlank(cacheKey) || clazz == null) {
            return null;
        } else{
            try {
                Cache cache = getEhCache(clazz);
                Element element = cache.get(cacheKey);
                entity = (T) element.getObjectValue();
            } catch (Exception e) {
                entity = null;
            }
        }
        return entity;
    }

    public <T> Boolean delCache(String cacheKey , Class<T> clazz){
        Boolean isSuccess = true;
        try {
            Cache cache = getEhCache(clazz);
            cache.remove(cacheKey);
        } catch (Exception e) {
            isSuccess = false;
        }
        return isSuccess;
    }

    private Cache getEhCache(Class<?> clazz){
        return cacheManager.getCache(clazz.getSimpleName());
    }
}
