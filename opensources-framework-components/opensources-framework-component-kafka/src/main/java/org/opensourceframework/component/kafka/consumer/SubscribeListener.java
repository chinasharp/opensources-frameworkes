package org.opensourceframework.component.kafka.consumer;

import org.opensourceframework.component.kafka.config.SubscribeRelation;
import org.opensourceframework.component.kafka.helper.KafkaConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息订阅监听
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
public class SubscribeListener {
    private final IKafkaConsumer kafkaConsumer;

    public SubscribeListener(IKafkaConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    private static final Logger logger = LoggerFactory.getLogger(SubscribeListener.class);

    public void start(String consumer) {
        SubscribeRelation subscribeRelation = KafkaConfigHelper.getSubscribeRelations(consumer);
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
        this.kafkaConsumer.subscribe(subscribeRelation);
    }
}
