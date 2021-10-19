package org.opensourceframework.starter.kafka.service;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.kafka.producer.IKafkaProducer;
import org.apache.kafka.clients.producer.Callback;

/**
 * Kafka消息发送接口
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class KafkaSendService {
    private final IKafkaProducer kafkaProducer;

    public KafkaSendService(IKafkaProducer kafkaProducer){
        this.kafkaProducer = kafkaProducer;
    }

    /**
     *
     * @param topic
     * @param partition
     * @param key
     * @param messageVo
     */
    public void send(String topic , Integer partition , String key , MessageVo messageVo){
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
    public MessageResponse snycSend(String topic , Integer partition , String key , Integer timeOut , MessageVo messageVo){
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
    public MessageResponse asnycSend(String topic , Integer partition , String key  ,MessageVo messageVo , Callback callback){
        return kafkaProducer.asnycSend(topic , partition , key, messageVo , callback);
    }
}
