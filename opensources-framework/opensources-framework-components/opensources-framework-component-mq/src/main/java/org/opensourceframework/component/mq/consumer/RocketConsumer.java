package org.opensourceframework.component.mq.consumer;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.component.mq.BaseService;
import org.opensourceframework.component.mq.RocketFactory;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.config.SubscribeRelation;
import org.opensourceframework.component.mq.exception.MQException;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.opensourceframework.component.mq.helper.ConsumerHelper;
import org.opensourceframework.component.mq.helper.SubscribeRelationHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;

/**
 * RocketMQ 消费者
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RocketConsumer extends BaseService implements IRocketConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RocketConsumer.class);
    private static final String CONSUMER_TYPE_SUBSCRIBE = "subscribe";
    private static final RocketConsumer rocketConsumer = new RocketConsumer();
    private DefaultMQPushConsumer consumer;

    private RocketConsumer() {
    }

    public static RocketConsumer getInstance() {
        return rocketConsumer;
    }

    public void init(MqConfig mqConfig) {
        super.mqConfig = mqConfig;
    }

    @Override
    public MessageResponse subscribe(String topic,  String tag, String consumerId, String processorBean, Integer msgType){
        try {
            SubscribeRelation subscribeRelation = SubscribeRelationHelper.createSubscribeRelation(topic , new String[]{tag} , consumerId , processorBean , msgType);
            this.toSubscribe(this.mqConfig, subscribeRelation, CONSUMER_TYPE_SUBSCRIBE);
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("订阅消息出错，topic：{} consumer:{}", topic, tag, e);
            throw new MQException(e.getMessage());
        }
    }

    @Override
    public MessageResponse subscribe(String topic, String tag, String processorBean, Integer msgType){
        try {
             return this.subscribe(topic, tag, this.mqConfig.getConsumerId() , processorBean, msgType);
        } catch (Exception e) {
            logger.error("订阅消息出错，topic：{} consumer:{}", topic, tag, e);
            throw new MQException(e.getMessage());
        }
    }

    @Override
    public MessageResponse subscribe(String topic, String[] tags, String processorBean, Integer msgType){
        if(this.mqConfig == null){
            logger.error("not found mq config!");
            throw new MQException("not found mq config");
        }
        long startTime = System.currentTimeMillis();
        logger.info("consumer subscribe start. consumerId:{},topic:{},tag:{}", this.mqConfig.getConsumerId(), topic, tags);
        try {
            SubscribeRelation subscribeRelation = SubscribeRelationHelper.createSubscribeRelation(topic , tags ,this.mqConfig.getConsumerId() , processorBean ,msgType);
            this.toSubscribe(this.mqConfig, subscribeRelation ,CONSUMER_TYPE_SUBSCRIBE);
            logger.info("consumer subscribe success. cast times:{} , consumerId:{},topic:{},tag:{}", System.currentTimeMillis() - startTime,topic,this.mqConfig.getConsumerId(), tags);
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("订阅消息出错,topic：{} ", topic, e);
            logger.error(e.getMessage(), e);
            throw new MQException(e.getMessage());
        }
    }

    @Override
    public MessageResponse subscribe(SubscribeRelation subscribeRelation) {
        this.toSubscribe(this.mqConfig, subscribeRelation, CONSUMER_TYPE_SUBSCRIBE);
        return MessageResponse.SUCCESS;
    }

    @Override
    public MessageResponse subscribe(MqConfig mqConfig, SubscribeRelation subscribeRelation) {
        this.toSubscribe(mqConfig, subscribeRelation, CONSUMER_TYPE_SUBSCRIBE);
        return MessageResponse.SUCCESS;
    }

    private void toSubscribe(MqConfig mqConfig, final SubscribeRelation subscribeRelation , String type) {
        try {
            String consumerId = subscribeRelation.getConsumerId();
            mqConfig.setConsumerId(consumerId);

            this.consumer = RocketFactory.createConsumer(mqConfig);

            if(StringUtils.isBlank(consumerId)){
                throw new MQException("consumerId is null");
            }
            Iterator<SubscribeRelation.TopicTag> iterator = subscribeRelation.getTopicTags().iterator();

            Integer messageType = subscribeRelation.getMessageType();
            if(messageType == null){
                messageType = MsgTypeConstant.CONCURRENTLY;
            }
            while(iterator.hasNext()) {
                SubscribeRelation.TopicTag topicTag = iterator.next();
                String topic = topicTag.getTopic();
                Set<String> tagSet = topicTag.getTagSet();
                if (CONSUMER_TYPE_SUBSCRIBE.equals(type)) {
                    String subExpression = this.getSubExpression(tagSet.toArray());
                    this.consumer.subscribe(topic, subExpression);

                    String processorBean = subscribeRelation.getProcessorBean();
                    if(MsgTypeConstant.CONCURRENTLY == messageType ){
                        ConsumerHelper.listenerConcurrently(this.consumer , topic , processorBean);
                    }

                    if(MsgTypeConstant.ORDERLY == messageType ){
                        ConsumerHelper.listenerOrderly(this.consumer , topic , processorBean);
                    }

                } else {
                    this.consumer.unsubscribe(topic);
                }
            }
            //启动消费者
            this.consumer.start();
            logger.info("Connected to {}", mqConfig.getNameSrvAddr());
        } catch (Exception exception) {
            logger.info("Failed to connect to {}", mqConfig.getNameSrvAddr(), exception);
        }
    }

    /**
     * 取消指定的topic监听
     *
     * @param topic
     * @return
     */
    public MessageResponse unsubscribe(String topic) {
        try {
            this.consumer.unsubscribe(topic);
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("取消订阅失败,topic：{}", topic, e);
            throw new MQException(e.getMessage());
        }
    }

    /**
     *
     * @param mqConfig
     * @param topic
     * @param consumerId
     * @param tag
     * @param processorBean
     * @param msgType
     * @param consumeType
     */
    public void toSubscribe(MqConfig mqConfig, String topic, String tag, String consumerId, String processorBean, Integer msgType , String consumeType) {
        SubscribeRelation subscribeRelation = new SubscribeRelation();
        subscribeRelation.setConsumerId(consumerId);
        subscribeRelation.setMessageType(msgType);
        subscribeRelation.setProcessorBean(processorBean);

        SubscribeRelation.TopicTag topicTag = subscribeRelation.createTopicTag(topic);
        topicTag.getTagSet().add(tag);

        this.toSubscribe(mqConfig, subscribeRelation, consumeType);
    }
}
