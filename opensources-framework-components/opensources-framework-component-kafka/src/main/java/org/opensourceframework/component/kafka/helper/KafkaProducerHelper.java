package org.opensourceframework.component.kafka.helper;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.KafkaFactory;
import org.opensourceframework.component.kafka.constant.KafkaConstants;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Kafka 消息生产者帮助工具类
 *
 * @author yuce
 * */
public class KafkaProducerHelper {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerHelper.class);

    /**
     * 发送消息 没有返回
     *
     * @param kafkaConfig
     * @param topic
     * @param message
     */
    public static void send(KafkaConfig kafkaConfig , String topic , String message){
        KafkaProducer kafkaProducer = KafkaFactory.createProducer(kafkaConfig);
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , null , null , message);
        kafkaProducer.send(record);
    }

    /**
     * 发送消息 没有返回
     *
     * @param kafkaConfig
     * @param topic
     * @param messageVo
     */
    public static void send(KafkaConfig kafkaConfig , String topic , MessageVo messageVo){
        KafkaProducer kafkaProducer = KafkaFactory.createProducer(kafkaConfig);
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , null , null , JSON.toJSONString(messageVo));
        kafkaProducer.send(record);
    }

    /**
     * 发送消息 没有返回
     *
     * @param topic
     * @param partition
     * @param key
     * @param messageVo
     */
    public static void send(KafkaConfig kafkaConfig , String topic , Integer partition , String key , MessageVo messageVo){
        KafkaProducer kafkaProducer = KafkaFactory.createProducer(kafkaConfig);
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , null , null , JSON.toJSONString(messageVo));
        kafkaProducer.send(record);
    }

    /**
     * 同步发送消息 返回发送结果
     *
     * @param topic
     * @param partition
     * @param key
     * @param timeOut
     * @param messageVo
     * @return
     */
    public static MessageResponse snycSend(KafkaConfig kafkaConfig , String topic , Integer partition , String key , Integer timeOut , MessageVo messageVo){
        KafkaProducer kafkaProducer = KafkaFactory.createProducer(kafkaConfig);
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , partition , key , JSON.toJSONString(messageVo));
        RecordMetadata metadata = null;
        try {
            Future<RecordMetadata> future = kafkaProducer.send(record);
            if(timeOut == null){
                timeOut = KafkaConstants.DEF_SEND_MSG_TIME_OUT;
            }
            metadata = future.get(timeOut, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("kafka sync send messgae is error.StackTrace:{}" , ExceptionUtils.getStackTrace(e));
        }
        if(metadata != null){
            return MessageResponse.SUCCESS;
        }else {
            return MessageResponse.ERROR;
        }
    }

    /**
     * 异步发送消息
     *
     * @param topic
     * @param partition
     * @param key
     * @param messageVo
     * @param callback
     * @return
     */
    public static MessageResponse asnycSend(KafkaConfig kafkaConfig ,String topic , Integer partition , String key  ,MessageVo messageVo , Callback callback){
        KafkaProducer kafkaProducer = KafkaFactory.createProducer(kafkaConfig);
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , partition , key , JSON.toJSONString(messageVo));
        try {
            Future<RecordMetadata> future = kafkaProducer.send(record, callback);
        } catch (Exception e) {
            logger.error("kafka async send messgae is error.StackTrace:{}" , ExceptionUtils.getStackTrace(e));
            return MessageResponse.ERROR;
        }
        return MessageResponse.SUCCESS;
    }

}
