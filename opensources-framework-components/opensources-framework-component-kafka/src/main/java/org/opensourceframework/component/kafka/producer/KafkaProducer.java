package org.opensourceframework.component.kafka.producer;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.kafka.KafkaFactory;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.constant.KafkaConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Kafka 消息生产者实现类
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
public class KafkaProducer implements IKafkaProducer{
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static final KafkaProducer kafkaProducer = new KafkaProducer();
    private org.apache.kafka.clients.producer.KafkaProducer producer;

    private KafkaProducer() {
    }

    public static KafkaProducer getInstance() {
        return kafkaProducer;
    }

    public void init(KafkaConfig config) {
        if(StringUtils.isBlank(config.getBootstrapServers())){
            throw new KafkaException("init kafka error.not found 'bootstrap.servers' config");
        }
        producer = KafkaFactory.createProducer(config);
    }

    /**
     * 发送消息 无须关心是否发送成功
     *
     * @param topic
     * @param messageVo
     */
    @Override
    public void send(String topic , Integer partition , String key , MessageVo messageVo) {
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , partition , key , JSON.toJSONString(messageVo));
        producer.send(record);
    }

    /**
     * 同步发送消息
     *
     * @param topic
     * @param key
     * @param messageVo
     * @return
     * @throws Exception
     */
    @Override
    public MessageResponse snycSend(String topic , Integer partition , String key , Integer timeOut , MessageVo messageVo){
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , partition , key , JSON.toJSONString(messageVo));
        RecordMetadata metadata = null;
        try {
            Future<RecordMetadata> future = producer.send(record);
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
     * @param messageVo
     * @param callback
     */
    @Override
    public MessageResponse asnycSend(String topic , Integer partition , String key  , MessageVo messageVo , Callback callback){
        ProducerRecord<String , String> record = new ProducerRecord<>(topic , partition , key , JSON.toJSONString(messageVo));
        try {
            Future<RecordMetadata> future = producer.send(record, callback);
        } catch (Exception e) {
            logger.error("kafka async send messgae is error.StackTrace:{}" , ExceptionUtils.getStackTrace(e));
            return MessageResponse.ERROR;
        }
        return MessageResponse.SUCCESS;
    }


}
