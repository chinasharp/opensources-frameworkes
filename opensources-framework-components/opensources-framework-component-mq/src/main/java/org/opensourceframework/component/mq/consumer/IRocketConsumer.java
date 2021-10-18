package org.opensourceframework.component.mq.consumer;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.config.SubscribeRelation;

/**
 * 消息订阅接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IRocketConsumer {
    /**
     * 订阅消息
     *
     * @param subscribeRelation
     * @return
     */
    MessageResponse subscribe(SubscribeRelation subscribeRelation);

    /**
     * 订阅消息
     *
     * @param mqConfig
     * @param subscribeRelation
     * @return
     */
    MessageResponse subscribe(MqConfig mqConfig, SubscribeRelation subscribeRelation);

    /**
     * 订阅消息
     *
     * @param topic
     * @param consumerId
     * @param tag
     * @param processorBean
     * @return
     */
    MessageResponse subscribe(String topic, String tag, String consumerId, String processorBean , Integer msgType);

    /**
     * 订阅消息 consumer为MqConfigVo的consumerId
     *
     * @param topic
     * @param tag
     * @param processorBean
     * @return
     */
    MessageResponse subscribe(String topic, String tag, String processorBean, Integer msgType);

    /**
     * 订阅多个tag消息  consumer为MqConfigVo的consumerId
     *
     * @param topic
     * @param tags
     * @param processorBean
     * @return
     */
    MessageResponse subscribe(String topic, String[] tags, String processorBean, Integer msgType);
}
