package org.opensourceframework.starter.mixcache.contant;

/**
 * TODO
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class CacheMessageContant {
    /**
     * MessageVo 中 msgContent 对应的 实体对象类
     */
    public static final String DATA_MAP_CLASS_KEY = "contentClass";

    /**
     * 缓存key 在 MessageVo.dataMap中的key值
     */
    public static final String DATA_MAP_CACHE_KEY = "cacheKey";

    /**
     * 默认缓存1小时
     */
    public static final int CACHE_EXPIRE_TIME = 3600;
}
