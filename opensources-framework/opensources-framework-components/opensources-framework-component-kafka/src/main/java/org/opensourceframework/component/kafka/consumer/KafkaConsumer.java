package org.opensourceframework.component.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.kafka.KafkaFactory;
import org.opensourceframework.component.kafka.api.IKafkaMessageProcessor;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.config.SubscribeRelation;
import org.opensourceframework.component.kafka.helper.KafkaConfigHelper;
import org.opensourceframework.component.kafka.helper.KafkaSpringHelper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Kafka消费者实现类
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
public class KafkaConsumer implements IKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    protected KafkaConfig kafkaConfig;
    private static final KafkaConsumer kafkaConsumer = new KafkaConsumer();
    private org.apache.kafka.clients.consumer.KafkaConsumer consumer;

    private KafkaConsumer() {
    }

    public static KafkaConsumer getInstance() {
        return kafkaConsumer;
    }

    public void init(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    /**
     * @param subscribeRelation
     * @return
     */
    @Override
    public MessageResponse subscribe(SubscribeRelation subscribeRelation) {
        Assert.notNull(subscribeRelation, "subscribe error.topic is null");
        Assert.notNull(subscribeRelation.getConsumerId(), "subscribe error.consumerId is null");
        Assert.notNull(subscribeRelation.getTopicList(), "subscribe error.topic is null");
        this.toSubscribe(this.kafkaConfig, subscribeRelation);
        return MessageResponse.SUCCESS;
    }

    @Override
    public MessageResponse subscribe(String topic, String consumerId, String processorBean) {
        try {
            Assert.notNull(topic, "subscribe error.topic is null");
            List<String> topicList = Lists.newArrayList(topic);
            SubscribeRelation subscribeRelation = KafkaConfigHelper.createSubscribeRelation(topicList, consumerId, processorBean);
            this.toSubscribe(this.kafkaConfig, subscribeRelation);
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("订阅消息出错，topic：{} consumer:{}", topic, consumerId, e);
            throw new KafkaException(e.getMessage());
        }
    }

    @Override
    public MessageResponse subscribe(List<String> topicList, String consumerId, String processorBean) {
        try {
            Assert.notEmpty(topicList, "subscribe error.topicList empty");
            SubscribeRelation subscribeRelation = KafkaConfigHelper.createSubscribeRelation(topicList, consumerId, processorBean);
            this.toSubscribe(this.kafkaConfig, subscribeRelation);
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("订阅消息出错，topicList：{} consumer:{}", topicList, consumerId, e);
            throw new KafkaException(e.getMessage());
        }
    }

    @Override
    public MessageResponse subscribe(Pattern pattern, String consumerId, String processorBean) {
        try {
            Assert.isNull(pattern, "subscribe error.pattern is null");
            SubscribeRelation subscribeRelation = KafkaConfigHelper.createSubscribeRelation(pattern, consumerId, processorBean);
            this.toSubscribe(this.kafkaConfig, subscribeRelation);
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("订阅消息出错，pattern：{} consumer:{}", pattern, consumer, e);
            throw new KafkaException(e.getMessage());
        }
    }

    @Override
    public MessageResponse subscribe(KafkaConfig kafkaConfig, SubscribeRelation subscribeRelation) {
        this.toSubscribe(kafkaConfig, subscribeRelation);
        return MessageResponse.SUCCESS;
    }

    private void toSubscribe(KafkaConfig kafkaConfig, final SubscribeRelation subscribeRelation) {
        this.consumer = KafkaFactory.createConsumer(kafkaConfig, subscribeRelation.getConsumerId());

        this.consumer.subscribe(subscribeRelation.getTopicList());
        List<MessageVo> messageVoList = null;
        String processorBean = subscribeRelation.getProcessorBean();
        IKafkaMessageProcessor processor = KafkaSpringHelper.getBean(processorBean);
        try {
            while (true) {
                try {
                    ConsumerRecords<String, Object> records = this.consumer.poll(Duration.ofSeconds(1));
                    if (!records.isEmpty()) {
                        // 处理消息
                        messageVoList = Lists.newArrayList();
                        for (ConsumerRecord<String, Object> record : records) {
                            Object message = record.value();
                            MessageVo messageVo = null;
                            if (message instanceof String) {
                                messageVo = JSON.parseObject(message.toString(), MessageVo.class);
                            } else if (message instanceof MessageVo) {
                                messageVo = (MessageVo) message;
                            } else {
                                logger.error("not support deserializer type");
                                continue;
                            }
                            messageVoList.add(messageVo);
                        }

                        // 使用异步提交规避阻塞
                        processor.process(messageVoList);
                        this.consumer.commitAsync();
                    }
                } catch (Exception e) {
                    // 处理异常
                    logger.error("subscribe msg is error! Topic:{},Consumer:{},StackTrace:{}", subscribeRelation.getTopicList(),
                            subscribeRelation.getConsumerId(), ExceptionUtils.getStackTrace(e));
                    processor.exceptionhandle(messageVoList);
                }
            }
        } finally {
            try {
                // 最后一次提交使用同步阻塞式提交
                this.consumer.commitSync();
            } finally {
                this.consumer.close();
            }
        }
    }

    /**
     * 取消监听
     *
     * @return
     */
    public MessageResponse unsubscribe() {
        try {
            this.consumer.unsubscribe();
            return MessageResponse.SUCCESS;
        } catch (Exception e) {
            logger.error("取消订阅失败", e);
            throw new KafkaException(e.getMessage());
        }
    }
}
