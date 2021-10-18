package org.opensourceframework.starter.mybatis.base.vo;

/**
 * 缓存对象
 */
public class DataCache<T> {
    private Boolean hasData;
    private T data;

    public DataCache() {
    }

    public DataCache(Boolean hasData, T data) {
        this.hasData = hasData;
        this.data = data;
    }

    public Boolean getHasData() {
        return hasData;
    }

    public void setHasData(Boolean hasData) {
        this.hasData = hasData;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
