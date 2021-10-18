package org.opensourceframework.starter.mixcache;

import org.opensourceframework.component.redis.cache.service.impl.RedisCacheService;
import org.opensourceframework.starter.ehcache.EhcacheService;
import org.opensourceframework.starter.mixcache.config.MixCacheConfig;
import org.opensourceframework.starter.mixcache.contant.CacheMessageContant;
import org.opensourceframework.starter.mixcache.contant.CacheOpEnum;
import org.opensourceframework.starter.mixcache.mq.CacheDataAsyncSend;
import org.opensourceframework.starter.mixcache.vo.CacheDataInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 混合缓存  本地缓存+分布式缓存
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
public class MixCacheService {
    private final MixCacheConfig mixCacheConfig;

    private final RedisCacheService remoteCache;

    private final EhcacheService localCache;

    private final CacheDataAsyncSend cacheDataAsyncSend;

    public MixCacheService(MixCacheConfig mixCacheConfig, RedisCacheService remoteCache, EhcacheService localCache, CacheDataAsyncSend cacheDataAsyncSend) {
        this.mixCacheConfig = mixCacheConfig;
        this.remoteCache = remoteCache;
        this.localCache = localCache;
        this.cacheDataAsyncSend = cacheDataAsyncSend;
    }

    public <T> void setCache(String cacheKey, T data) {
        if (data == null || StringUtils.isBlank(cacheKey)) {
            return;
        }

        //复制对象,避免使用本地缓存和分布式缓存同时使用时,对象不一致
        T cacheObj = ObjectUtils.clone(data);
        if (mixCacheConfig.getLocalCacheFlag()) {
            //将cacheObj写本地缓存
            localCache.setCache(cacheKey, data);
        }
        if (mixCacheConfig.getRemoteCacheFlag()) {
            // 将cacheObj写分布式缓存
            // 1、本地缓存和分布式缓存都有开启,业务可允许延时 本地缓存和分布式缓存最终一致可使用异步方式写入
            if (mixCacheConfig.getCacheEnableAsync()) {
                // 异步方式写入分布式缓存
                cacheDataAsyncSend.sendMessage(new CacheDataInfo(data, cacheKey), CacheOpEnum.ADD);
            }
            // 2、如果业务不允许延时,本地缓存和分布式缓存必须实时的一致 或者 只用分布式缓存开启,需要同步方式写入
            if (!mixCacheConfig.getLocalCacheFlag() || (mixCacheConfig.getLocalCacheFlag() && mixCacheConfig.getCacheEnableAsync())) {
                // 同步方式写入分布式缓存
                remoteCache.set(cacheKey, data, CacheMessageContant.CACHE_EXPIRE_TIME);
            }
        } else {
            return;
        }
    }

    public <T> T getCache(String cacheKey, Class<T> tClass) {
        T data = null;
        if (StringUtils.isNotBlank(cacheKey)) {
            if (mixCacheConfig.getLocalCacheFlag()) {
                //获取本地缓存
                data = localCache.getCache(cacheKey, tClass);
            }
            if (mixCacheConfig.getRemoteCacheFlag() && data == null) {
                //获取分布式缓存
                data = remoteCache.get(cacheKey, tClass);
            }
        }
        return data;
    }

    public <T> void delCache(String cacheKey, Class<T> tClass) {
        if (mixCacheConfig.getLocalCacheFlag()) {
            //将cacheObj写本地缓存,堆外或者堆类
            localCache.delCache(cacheKey, tClass);
        }
        if (mixCacheConfig.getRemoteCacheFlag()) {
            // 将cacheObj写分布式缓存
            // 1、本地缓存和分布式缓存都有开启,业务可允许延时 本地缓存和分布式缓存最终一致可使用异步方式写入
            if (mixCacheConfig.getCacheEnableAsync()) {
                // 异步方式写入分布式缓存
                CacheDataInfo cacheDataInfo = CacheDataInfo.newInstance(cacheKey);
                cacheDataAsyncSend.sendMessage(cacheDataInfo, CacheOpEnum.DELETE);
            }
            // 2、如果业务不允许延时,本地缓存和分布式缓存必须实时的一致 或者 只用分布式缓存开启,需要同步方式写入
            if (!mixCacheConfig.getLocalCacheFlag() || (mixCacheConfig.getLocalCacheFlag() && mixCacheConfig.getCacheEnableAsync())) {
                // 同步方式写入分布式缓存
                remoteCache.del(cacheKey);
            }
        } else {
            return;
        }
    }
}
