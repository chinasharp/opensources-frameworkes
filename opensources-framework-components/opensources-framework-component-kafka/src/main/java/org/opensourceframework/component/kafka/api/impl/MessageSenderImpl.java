package org.opensourceframework.component.kafka.api.impl;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.kafka.api.IMessageSender;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.producer.IKafkaProducer;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.springframework.util.Assert;

/**
 * TODO
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class MessageSenderImpl implements IMessageSender<MessageVo> {
    private final IKafkaProducer kafkaProducer;
    private final KafkaConfig kafkaConfig;
    private final String profileActive;

    public MessageSenderImpl(IKafkaProducer kafkaProducer , String profileActive , KafkaConfig kafkaConfig){
        this.kafkaProducer = kafkaProducer;
        this.kafkaConfig = kafkaConfig;
        this.profileActive = profileActive;
    }

    /**
     *
     * @param topic
     * @param partition
     * @param key
     * @param messageVo
     */
    @Override
    public void send(String topic , Integer partition , String key , MessageVo messageVo){
        topic = handleEnv(topic);
        validateParam(topic , messageVo);
        kafkaProducer.send(topic , partition , key ,messageVo);
    }

    /**
     *
     * @param topic
     * @param partition
     * @param key
     * @param timeOut
     * @param messageVo
     * @return
     */
    @Override
    public MessageResponse snycSend(String topic, Integer partition, String key, Integer timeOut, MessageVo messageVo){
        topic = handleEnv(topic);
        validateParam(topic , messageVo);
        return kafkaProducer.snycSend(topic , partition , key , timeOut , messageVo);
    }

    /**
     *
     * @param topic
     * @param partition
     * @param key
     * @param messageVo
     * @param callback
     * @return
     */
    @Override
    public MessageResponse asnycSend(String topic , Integer partition , String key  ,MessageVo messageVo , Callback callback){
        topic = handleEnv(topic);
        validateParam(topic , messageVo);
        return kafkaProducer.asnycSend(topic , partition , key, messageVo , callback);
    }

    private void validateParam(String topic , MessageVo message){
        Assert.notNull(topic , "topic is null");
        Assert.notNull(message , "message is null");
    }

    private String handleEnv(String topic){
        if(StringUtils.isNotBlank(profileActive)){
            topic = topic.concat("_").concat(profileActive);
        }
        return topic.toUpperCase();
    }
}
