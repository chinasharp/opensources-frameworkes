package org.opensourceframework.component.kafka.consumer;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.config.SubscribeRelation;

import java.util.List;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
public interface IKafkaConsumer {
    /**
     *
     * @param subscribeRelation
     * @return
     */
    MessageResponse subscribe(SubscribeRelation subscribeRelation);

    /**
     *
     * @param topic
     * @param consumerId
     * @param processorBean
     * @return
     */
    MessageResponse subscribe(String topic, String consumerId, String processorBean);

    /**
     *
     * @param topicList
     * @param consumerId
     * @param processorBean
     * @return
     */
    MessageResponse subscribe(List<String> topicList, String consumerId, String processorBean);

    /**
     *
     * @param pattern
     * @param consumerId
     * @param processorBean
     * @return
     */
    MessageResponse subscribe(Pattern pattern, String consumerId, String processorBean);

    /**
     *
     * @param kafkaConfig
     * @param subscribeRelation
     * @return
     */
    MessageResponse subscribe(KafkaConfig kafkaConfig, SubscribeRelation subscribeRelation);
}
