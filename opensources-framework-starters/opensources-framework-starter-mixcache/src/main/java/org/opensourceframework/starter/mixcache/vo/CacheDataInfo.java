package org.opensourceframework.starter.mixcache.vo;

import java.io.Serializable;

/**
 * 混合缓存 数据对象
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class CacheDataInfo<T> implements Serializable {
    private T data;
    private String cacheKey;

    public CacheDataInfo(T data, String cacheKey) {
        this.data = data;
        this.cacheKey = cacheKey;
    }

    public static CacheDataInfo newInstance(String cacheKey){
        return new CacheDataInfo(null , cacheKey);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
