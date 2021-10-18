package org.opensourceframework.component.redis.bloomFilter.service.impl;

import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.base.helper.DateHelper;
import org.opensourceframework.component.redis.bloomFilter.service.IBloomFilterService;
import org.opensourceframework.component.redis.common.RedissonService;
import org.opensourceframework.component.redis.cache.config.RedisConfig;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

import java.util.Date;

/**
 * 布鲁过滤器实现类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class BloomFilterService extends RedissonService implements IBloomFilterService {
    private final RedisConfig redisConfig;
    private final RedissonClient redisson;
    public BloomFilterService(RedissonClient redisson , RedisConfig redisConfig){
        this.redisson = redisson;
        this.redisConfig = redisConfig;
    }

    @Override
    public void add(String key , Object data) {
        RBloomFilter bloomFilter = redisson.getBloomFilter(key);
        bloomFilter.tryInit(redisConfig.getExpectedInsertions() , redisConfig.getFalseProbability());
        bloomFilter.add(convertData(data));
    }

    @Override
    public Boolean exist(String key , Object data) {
        RBloomFilter bloomFilter = redisson.getBloomFilter(key);
        bloomFilter.tryInit(redisConfig.getExpectedInsertions() , redisConfig.getFalseProbability());
        boolean isExist = bloomFilter.contains(convertData(data));
        return isExist;
    }

    private String convertData(Object data){
        String putData = null;
        if(data instanceof String){
            putData = (String) data;
        }

        if(data instanceof Number){
            putData = data.toString();
        }

        if(data instanceof Date){
            putData = DateHelper.YYYYMMDDHHMMSS((Date) data);
        }

        if(putData == null){
            String className = data.getClass().getName();
            throw new BizException("BoolFilter 的值只能使用String、Number、Date 3种类型!不支持" + className);
        }
        return putData;
    }
}
