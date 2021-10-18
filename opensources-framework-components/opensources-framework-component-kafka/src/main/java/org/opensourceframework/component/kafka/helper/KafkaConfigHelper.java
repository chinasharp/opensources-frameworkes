package org.opensourceframework.component.kafka.helper;

import com.google.common.collect.Lists;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.config.SubscribeRelation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Kafka 配置工具类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class KafkaConfigHelper {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConfigHelper.class);

    /**
     *存放消息的消费配置
     *
     */
    public static final Map<String , SubscribeRelation> subscribeRelationMap = new ConcurrentHashMap<>();

    /**
     * Map结构 Map<topic:tag , consumerId> topicMap  记录topic:tag 与consumenr的对应关系
     */
    public static final Map<String, String> topicRefConsumerMap = new ConcurrentHashMap<String, String>();
    public static final Map<String, String> patternTopicRefConsumerMap = new ConcurrentHashMap<String, String>();

    /**
     * 创建消费订阅关系值对象
     *
     * @param topicList
     * @param consumerId
     * @param processorBean
     * @return
     */
    public static SubscribeRelation createSubscribeRelation(List<String> topicList , String consumerId, String processorBean){
        SubscribeRelation subscribeRelation = new SubscribeRelation();
        subscribeRelation.setConsumerId(consumerId);
        subscribeRelation.setProcessorBean(processorBean);
        subscribeRelation.getTopicList().addAll(topicList);
        return subscribeRelation;
    }

    /**
     * 创建消费订阅关系值对象
     *
     * @param pattern
     * @param consumerId
     * @param processorBean
     * @return
     */
    public static SubscribeRelation createSubscribeRelation(Pattern pattern , String consumerId, String processorBean){
        SubscribeRelation subscribeRelation = new SubscribeRelation();
        subscribeRelation.setConsumerId(consumerId);
        subscribeRelation.setProcessorBean(processorBean);
        subscribeRelation.setPattern(pattern);
        return subscribeRelation;
    }

    /**
     * 添加(consumerId , topic)
     *
     * @param topic
     * @param consumerId
     * @param pattern
     * @param processorBean
     */
    public static SubscribeRelation addSubscribeRelation(String topic, String pattern, String consumerId, String processorBean) {
        if(StringUtils.isNotBlank(topic)) {
            topicRefConsumerMap.put(topic, consumerId);
        }
        if(StringUtils.isNotBlank(pattern)){
            patternTopicRefConsumerMap.put(pattern , consumerId);
        }

        SubscribeRelation subscribeRelation = subscribeRelationMap.get(consumerId);
        if(subscribeRelation == null){
            subscribeRelation = new SubscribeRelation();
            subscribeRelation.setConsumerId(consumerId);
            subscribeRelation.setProcessorBean(processorBean);
            subscribeRelationMap.put(consumerId , subscribeRelation);
        }else{
            if(subscribeRelation.getTopicList() == null){
                subscribeRelation.setTopicList(Lists.newArrayList());
            }
        }

        subscribeRelation.getTopicList().add(topic);

        if(StringUtils.isNotBlank(pattern)) {
            Pattern p = Pattern.compile(pattern);
            subscribeRelation.setPattern(p);
        }

        return subscribeRelation;
    }

    /**
     * 获取consumerId 对应的订阅关系配置
     *
     * @param consumerId
     * @return
     */
    public static SubscribeRelation getSubscribeRelations(String consumerId) {
        SubscribeRelation subscribeRelation = subscribeRelationMap.get(consumerId);
        return subscribeRelation;
    }

    public static String parseConsumer(KafkaConfig kafkaConfig, String consumer, String dfault) {
        consumer  = StringUtils.isEmpty(consumer) ? dfault : consumer;
        consumer = parseStrtoUppercase(consumer);
        if (!StringUtils.isEmpty(consumer) && !consumer.startsWith("GID_")) {
            consumer = "GID_" + consumer;
        }
        return consumer;
    }

    private static String parseStrtoUppercase(String str) {
        return StringUtils.isEmpty(str) ? null : str.replace("-", "_").toUpperCase();
    }
}
