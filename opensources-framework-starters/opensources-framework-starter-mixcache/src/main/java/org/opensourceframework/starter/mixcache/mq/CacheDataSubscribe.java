package org.opensourceframework.starter.mixcache.mq;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.annotation.MQSubscribe;
import org.opensourceframework.component.mq.api.IMessageProcessor;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.opensourceframework.component.redis.cache.service.IRedisCacheService;
import org.opensourceframework.starter.mixcache.contant.CacheMessageContant;
import org.opensourceframework.starter.mixcache.vo.CacheDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 需要缓存处理的数据异步订阅
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Component
@MQSubscribe(topic = "CACHE_DATA_TOPIC" , tag = "CACHE_DATA_TAG" , consumer = "GID_CACHE_DATA" , messageType = MsgTypeConstant.CONCURRENTLY)
public class CacheDataSubscribe implements IMessageProcessor<MessageVo> {
    @Autowired
    private IRedisCacheService redisCacheService;

    @Override
    public MessageResponse process(MessageVo messageVo) {
        String content = messageVo.getMsgContent();
        CacheDataInfo cacheData = JSON.parseObject(content , CacheDataInfo.class);
        redisCacheService.set(cacheData.getCacheKey() , cacheData.getData() , CacheMessageContant.CACHE_EXPIRE_TIME);
        return MessageResponse.SUCCESS;
    }
}