package org.opensourceframework.component.mq.helper;

import com.google.common.collect.Lists;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.config.SubscribeRelation;
import org.opensourceframework.component.mq.config.TopicConfig;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * consumer 与 topic的订阅关系帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SubscribeRelationHelper {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeRelationHelper.class);
    private static final String KEY_SPLIT_CHAR = ":";
    /**
     *存放消息的消费配置
     *
     */
    public static final Map<String , SubscribeRelation> subscribeRelationMap = new ConcurrentHashMap<>();

    /**
     * Map结构 Map<topic:tag , consumerId> topicMap  记录topic:tag 与consumenr的对应关系
     */
    public static final Map<String, String> topicRefConsumerMap = new ConcurrentHashMap<String, String>();

    public SubscribeRelationHelper() {
    }

    private static MqConfig getMQConfigVo() {
        return SpringHelper.getBean(MqConfig.class);
    }

    private static TopicConfig getTopicConfigVo() {
        return SpringHelper.getBean(TopicConfig.class);
    }

    private static String getEnvModule() {
        return SpringHelper.getApplicationContext().getEnvironment().getProperty("spring.application.name");
    }

    private static String getDefaultConsumer() {
        return SpringHelper.getApplicationContext().getEnvironment().getProperty("opensourceframework.starter.mq.default.consumer");
    }

    /**
     * 添加(consumerId , (topic  , (tag , consumer)))
     *
     * @param topic
     * @param consumerId
     * @param tag
     * @param processorBean
     * @param messageType
     */
    public static SubscribeRelation addSubscribeRelation(String topic, String tag, String consumerId, String processorBean, Integer messageType) {
        if(messageType == null){
            messageType = MsgTypeConstant.CONCURRENTLY;
        }
        String topicRefConsumerKey = buildTopicMapKey(topic, tag);
        topicRefConsumerMap.put(topicRefConsumerKey, consumerId);

        SubscribeRelation subscribeRelation = subscribeRelationMap.get(consumerId);
        if(subscribeRelation == null){
            subscribeRelation = new SubscribeRelation();
            subscribeRelation.setMessageType(messageType);
            subscribeRelation.setConsumerId(consumerId);
            subscribeRelation.setProcessorBean(processorBean);
            subscribeRelationMap.put(consumerId , subscribeRelation);
        }else{
            if(subscribeRelation.getTopicTags() == null){
                subscribeRelation.setTopicTags(Lists.newArrayList());
            }
        }

        SubscribeRelation.TopicTag topicTag = subscribeRelation.createTopicTag(topic);
        topicTag.getTagSet().add(tag);
        subscribeRelation.getTopicTags().add(topicTag);

        return subscribeRelation;
    }


    /**
     * 是否已经订阅
     *
     * @param topic
     * @param tag
     * @param consumer
     * @return
     */
    public static Boolean isAdded(String topic, String tag, String consumer){
        Boolean isAdded = false;
        String topicRefConsumerKey = buildTopicMapKey(topic ,tag);
        String val = topicRefConsumerMap.get(topicRefConsumerKey);
        if(StringUtils.isNotBlank(val)){
            isAdded = true;
        }
        return isAdded;
    }

    /**
     * 使用默认的consumer
     *
     * @param topic
     * @param tag
     * @param processor
     * @param messageType
     */
    public static void addSubscribeRelation(String topic, String tag, String processor , Integer messageType) {
        addSubscribeRelation(topic, tag, getDefaultConsumer(), processor , messageType);
    }

    /**
     * 使用注入的topic 和默认的 consumer
     *
     * @param tag
     * @param processor
     */
    public static void addSubscribeRelation(String tag, String processor) {
        addSubscribeRelation(getTopicConfigVo().getTopic(), tag, getDefaultConsumer(), processor , MsgTypeConstant.CONCURRENTLY);
    }

    /**
     * 创建消费订阅关系值对象
     *
     * @param topic
     * @param tags
     * @param consumerId
     * @param processorBean
     * @param msgType
     * @return
     */
    public static SubscribeRelation createSubscribeRelation(String topic, String[] tags, String consumerId, String processorBean, Integer msgType){
        SubscribeRelation subscribeRelation = new SubscribeRelation();
        subscribeRelation.setMessageType(msgType);
        subscribeRelation.setConsumerId(consumerId);
        subscribeRelation.setProcessorBean(processorBean);

        SubscribeRelation.TopicTag topicTag = subscribeRelation.createTopicTag(topic);
        topicTag.getTagSet().addAll(Arrays.asList(tags));
        subscribeRelation.getTopicTags().add(topicTag);
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

    public static String parseConsumer(MqConfig mqConfig, String consumer, String dfault) {
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

    private static String buildTopicMapKey(String topic , String tag){
        return topic.concat(KEY_SPLIT_CHAR).concat(tag);
    }
}
