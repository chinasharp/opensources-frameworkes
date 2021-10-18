package org.opensourceframework.starter.mixcache.mq;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.opensourceframework.starter.mixcache.contant.CacheOpEnum;
import org.opensourceframework.starter.mixcache.vo.CacheDataInfo;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 需要缓存处理的数据异步发送
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Component
public class CacheDataAsyncSend {
    private static final Logger logger = LoggerFactory.getLogger(CacheDataAsyncSend.class);
    private static final String CACHE_DATA_TOPIC = "CACHE_DATA_TOPIC";
    private static final String CACHE_DATA_TAG = "CACHE_DATA_TAG";

    @Autowired
    private IMessageSender messageSender;

    /**
     * 发送要异步写入缓存的消息
     *
     * @param cacheData
     */
    public void sendMessage(CacheDataInfo cacheData , CacheOpEnum cacheOpEnum){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent(JSON.toJSONString(cacheData));

        messageSender.sendMessageAsync(CACHE_DATA_TOPIC, CACHE_DATA_TAG, messageVo, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("send cache message success");
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("send cache message error");
            }
        });
    }
}
