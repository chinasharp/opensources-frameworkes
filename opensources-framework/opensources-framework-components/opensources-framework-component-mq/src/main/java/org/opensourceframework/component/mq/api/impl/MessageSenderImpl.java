package org.opensourceframework.component.mq.api.impl;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.producer.transaction.DefaultTransactionListener;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.constant.MQConstant;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.opensourceframework.component.mq.helper.ProducerHelper;
import org.opensourceframework.component.mq.producer.IRocketProducer;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * MQ发送实现
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MessageSenderImpl implements IMessageSender<MessageVo> {
    private static final Logger logger = LoggerFactory.getLogger(MessageSenderImpl.class);
    private final IRocketProducer producer;
    private final String profileActive;
    private final MqConfig mqConfig;
    private final DefaultTransactionListener defaultTransactionListener;


    public MessageSenderImpl(IRocketProducer producer , String profileActive , DefaultTransactionListener defaultTransactionListener , MqConfig mqConfig) {
        this.producer = producer;
        this.profileActive = profileActive;
        this.mqConfig = mqConfig;
        this.defaultTransactionListener = defaultTransactionListener;
    }

    @Override
    public MessageResponse sendDelayMessage(String topic, String tag, MessageVo message, Integer delayTimeLevel) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        return this.producer.sendConcurrentlyMessage(topic, tag, message, delayTimeLevel , MQConstant.DEF_SEND_TIME_OUT);
    }

    @Override
    public MessageResponse sendMessage(String topic, String tag, MessageVo message) {
        validateParam(topic , tag , message);
        return this.sendMessage(topic, tag, message , MsgTypeConstant.CONCURRENTLY);
    }

    @Override
    public MessageResponse sendMessage(String topic, String tag, MessageVo message, Integer messageType) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        return this.producer.sendMessage(topic , tag , message , message.getBizCode() , 0, MQConstant.DEF_SEND_TIME_OUT , messageType);
    }

    @Override
    public void sendMessageAsync(String topic, String tag, MessageVo message , SendCallback callback) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("异步发送MQ消息,topic:{}, tag:{},message:{}", topic, tag, JSON.toJSONString(message));
        this.producer.sendAsyncConcurrentlyMessage(topic, tag, message , callback);
    }

    @Override
    public void sendDelayMessageAsync(String topic, String tag, MessageVo message, Integer delayTimeLevel ,SendCallback callback) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("异步发送MQ消息,topic:{}, tag:{},message:{}", topic, tag, JSON.toJSONString(message));
        sendDelayMessageAsync(topic, tag, message, delayTimeLevel , null , callback);
    }

    @Override
    public void sendDelayMessageAsync(String topic, String tag, MessageVo message, Integer delayTimeLevel ,Long timeout , SendCallback callback) {
        validateParam(topic , tag , message);
        logger.info("异步发送MQ消息,topic:{}, tag:{},message:{}", topic, tag, JSON.toJSONString(message));
        this.producer.sendAsyncConcurrentlyMessage(topic, tag, message, delayTimeLevel , timeout , callback);
    }

    @Override
    public void sendOrderMessageAsync(String topic, String tag, MessageVo message, String bizCode ,SendCallback callback) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("异步发送MQ消息,topic:{}, tag:{},message:{}", topic, tag, JSON.toJSONString(message));
        this.producer.sendAsyncOrderMessage(topic , tag , message , bizCode , callback);
    }

    @Override
    public void sendDelayOrderMessageAsync(String topic, String tag, MessageVo message , String bizCode , Integer delayTimeLevel , SendCallback callback){
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("异步发送MQ消息,topic:{}, tag:{},message:{}", topic, tag, JSON.toJSONString(message));
        this.producer.sendAsyncOrderMessage(topic , tag , message , bizCode , delayTimeLevel , callback);
    }

    @Override
    public void sendOrderMessageAsync(String topic, String tag, MessageVo message, String bizCode, Integer delayTimeLevel, Long timeout, SendCallback sendCallback) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("异步发送MQ消息,topic:{}, tag:{},message:{}", topic, tag, JSON.toJSONString(message));
        this.producer.sendAsyncOrderMessage(topic , tag , message , bizCode , delayTimeLevel , timeout , sendCallback);
    }

    @Override
    public MessageResponse sendTransactionMessage(String topic, String tag , MessageVo message, TransactionListener transactionListener , Object listenerArg) {
        topic = handleEnv(topic);
        validateParam(topic , tag , message);
        return ProducerHelper.sendTransactionMessage(mqConfig, topic , tag , message ,transactionListener , listenerArg);
    }

    @Override
    public MessageResponse sendTransactionMessage(String topic, String tag , MessageVo message) {
        topic = handleEnv(topic);
        validateParam(topic , tag , message);
        return ProducerHelper.sendTransactionMessage(mqConfig, topic , tag , message ,defaultTransactionListener , null);
    }

    @Override
    public MessageResponse sendOrderMessage(String topic, String tag, MessageVo message) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("发送MQ有序消息,topic:{}, tag:{} , bizCode:{} , message:{}", topic, tag, null, JSON.toJSONString(message));
        return this.producer.sendOrderMessage(topic , tag , message , null , 0 , MQConstant.DEF_SEND_TIME_OUT);
    }

    @Override
    public MessageResponse sendOrderMessage(String topic, String tag, MessageVo message, String bizCode) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("发送MQ有序消息,topic:{}, tag:{} , bizCode:{} , message:{}", topic, tag, bizCode, JSON.toJSONString(message));
        return this.producer.sendOrderMessage(topic , tag , message , bizCode , 0 , MQConstant.DEF_SEND_TIME_OUT);
    }

    @Override
    public MessageResponse sendOrderMessage(String topic, String tag, MessageVo message, String bizCode, Integer delayTimeLevel) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("发送MQ有序消息,topic:{}, tag:{} , bizCode:{} , message:{}", topic, tag, bizCode, JSON.toJSONString(message));
        return this.producer.sendOrderMessage(topic , tag , message , bizCode , delayTimeLevel, MQConstant.DEF_SEND_TIME_OUT);
    }

    @Override
    public MessageResponse sendOrderMessage(String topic, String tag, MessageVo message, String bizCode, Integer delayTimeLevel, Long timeout) {
        topic = handleEnv(topic);
        tag = handleEnv(tag);
        validateParam(topic , tag , message);
        logger.info("发送MQ有序消息,topic:{}, tag:{} , bizCode:{} , message:{}", topic, tag, bizCode, JSON.toJSONString(message));
        return this.producer.sendOrderMessage(topic , tag , message , bizCode , delayTimeLevel , timeout);
    }

    private void validateParam(String topic , String tag , MessageVo message){
        Assert.notNull(topic , "topic is null");
        Assert.notNull(tag , "tag is null");
        Assert.notNull(message , "message is null");
    }

    private String handleEnv(String topic){
        if(StringUtils.isNotBlank(profileActive)){
            topic = topic.concat("_").concat(profileActive);
        }
        return topic.toUpperCase();
    }
}
