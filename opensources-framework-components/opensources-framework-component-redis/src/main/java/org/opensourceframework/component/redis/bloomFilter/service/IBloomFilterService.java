package org.opensourceframework.component.redis.bloomFilter.service;

/**
 * 布鲁过滤器Service接口类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public interface IBloomFilterService<T> {
    /**
     * 添加数据
     *
     * @param key
     * @param data
     */
    void add(String key , Object data);

    /**
     * 判断是否存在
     *
     * @param key
     * @param data
     * @return
     */
    Boolean exist(String key , Object data);
}
