package org.opensourceframework.component.mq.consumer;

import org.opensourceframework.component.mq.config.SubscribeRelation;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.opensourceframework.component.mq.helper.SubscribeRelationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息订阅监听
 *
 * @author yu.ce@foxmail.com @since 1.0.0
 *
 */
public class SubscribeListener {
    private final IRocketConsumer consumer;

    public SubscribeListener(IRocketConsumer consumer) {
        this.consumer = consumer;
    }

    private static final Logger logger = LoggerFactory.getLogger(SubscribeListener.class);

    public void start(String consumer) {
        SubscribeRelation subscribeRelation = SubscribeRelationHelper.getSubscribeRelations(consumer);
        if (subscribeRelation != null) {
            this.subscribe(subscribeRelation);
        } else {
            logger.info("不需要监听MQ消息");
        }
    }

    /**
     * 订阅消息
     *
     * @param subscribeRelation
     */
    private void subscribe(SubscribeRelation subscribeRelation) {
        this.consumer.subscribe(subscribeRelation);
    }

    /**
     * 有序消息订阅
     *
     * @param topic
     * @param tag
     * @param consumer
     * @param processor
     */
    public void orderlySubscribe(String topic , String tag , String consumer ,  String processor){
        SubscribeRelation subscribeRelation = SubscribeRelationHelper.addSubscribeRelation(topic, tag, consumer , processor , MsgTypeConstant.ORDERLY);
        this.consumer.subscribe(subscribeRelation);
    }

    /**
     * 一般消息订阅
     *
     * @param topic
     * @param tag
     * @param consumer
     * @param processor
     */
    public void concurrentlySubscribe(String topic , String tag , String consumer ,  String processor){
        SubscribeRelation subscribeRelation = SubscribeRelationHelper.addSubscribeRelation(topic, tag, consumer , processor , MsgTypeConstant.CONCURRENTLY);
        this.consumer.subscribe(subscribeRelation);
    }
}
