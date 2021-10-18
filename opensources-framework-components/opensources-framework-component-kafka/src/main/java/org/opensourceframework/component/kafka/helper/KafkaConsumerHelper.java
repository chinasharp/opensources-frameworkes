package org.opensourceframework.component.kafka.helper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.config.SubscribeRelation;
import org.opensourceframework.component.kafka.KafkaFactory;
import org.opensourceframework.component.kafka.api.IKafkaMessageProcessor;
import org.opensourceframework.component.kafka.constant.KafkaConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;

/**
 * Kafka 消息消费者帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class KafkaConsumerHelper {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerHelper.class);

    private static String parseStrtoUppercase(String str) {
        return StringUtils.isEmpty(str) ? null : str.replace("-", "_").toUpperCase();
    }

    public static String parseConsumer(String consumer, String dfault) {
        String csf = StringUtils.isEmpty(consumer) ? dfault : consumer;
        csf = parseStrtoUppercase(csf);
        if (!StringUtils.isEmpty(csf) && !csf.startsWith("GID_") && !csf.startsWith("GID_")) {
            csf = "GID_" + csf;
        }
        return csf;
    }

    /**
     * 订阅topic消息
     *
     * @param kafkaConfig
     * @param consumerId
     * @param topicList
     * @param processor   消费MQ消息的Proessor实现类
     */
    public static void subscribe(KafkaConfig kafkaConfig, String consumerId , List<String> topicList, IKafkaMessageProcessor processor){
        Assert.notNull(consumerId , "consumerId is null");
        Assert.notEmpty(topicList , "topicList is empty");
        Assert.notNull(processor , "processor is null");
        try{
            consumerId = parseConsumer(consumerId  , KafkaConstants.DEF_CONSUMER_ID);
            KafkaConsumer kafkaConsumer = KafkaFactory.createConsumer(kafkaConfig , consumerId);
            kafkaConsumer.subscribe(topicList);
            List<MessageVo> messageVoList = null;
            try {
                while (true) {
                    try {
                        ConsumerRecords<String, Object> records = kafkaConsumer.poll(Duration.ofSeconds(1));
                        if (!records.isEmpty()) {
                            // 处理消息
                            messageVoList = Lists.newArrayList();
                            for (ConsumerRecord<String, Object> record : records) {
                                Object message = record.value();
                                MessageVo messageVo = null;
                                if(message instanceof String) {
                                   messageVo = JSON.parseObject(message.toString(), MessageVo.class);
                                }else if(message instanceof MessageVo){
                                    messageVo = (MessageVo) message;
                                }else {
                                    logger.error("not support deserializer type");
                                    continue;
                                }
                                messageVoList.add(messageVo);
                            }

                            // 使用异步提交规避阻塞
                            processor.process(messageVoList);
                            kafkaConsumer.commitAsync();
                        }
                    }catch (Exception e){
                        // 处理异常
                        logger.error("subscribe msg is error! Topic:{},Consumer:{},StackTrace:{}" , topicList , consumerId , ExceptionUtils.getStackTrace(e));
                        processor.exceptionhandle(messageVoList);
                    }
                }
            } finally {
                try {
                    // 最后一次提交使用同步阻塞式提交
                    kafkaConsumer.commitSync();
                } finally {
                    kafkaConsumer.close();
                }
            }
        } catch (Exception exception) {
            logger.info("Failed to connect to {}", kafkaConfig.getBootstrapServers(), exception);
        }
    }

    /**
     * 订阅消息
     *
     * @param kafkaConfig
     * @param subscribeRelation
     */
    public static void subscribe(KafkaConfig kafkaConfig, final SubscribeRelation subscribeRelation , IKafkaMessageProcessor processor){
        subscribe(kafkaConfig , subscribeRelation.getConsumerId() , subscribeRelation.getTopicList() , processor);
    }

    /**
     * 取消监听
     *
     * @return
     */
    public static MessageResponse unsubscribe(KafkaConfig kafkaConfig, String consumerId) {
        Assert.notNull(consumerId , "consumerId is null");
        try {
            consumerId = parseConsumer(consumerId  , KafkaConstants.DEF_CONSUMER_ID);
            KafkaConsumer kafkaConsumer = KafkaFactory.createConsumer(kafkaConfig , consumerId);
            kafkaConsumer.unsubscribe();
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("取消订阅失败" , e);
            throw new KafkaException(e.getMessage());
        }
    }

}
