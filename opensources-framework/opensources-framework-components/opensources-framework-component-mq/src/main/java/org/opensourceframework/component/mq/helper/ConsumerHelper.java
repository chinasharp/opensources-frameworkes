package org.opensourceframework.component.mq.helper;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.RocketFactory;
import org.opensourceframework.component.mq.api.IMessageProcessor;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.exception.MQException;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * RocketMQ 消息消费者帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ConsumerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerHelper.class);

    public ConsumerHelper() {
    }

    private static String parseStrtoUppercase(String str) {
        return StringUtils.isEmpty(str) ? null : str.replace("-", "_").toUpperCase();
    }

    public static String parseConsumer(MqConfig mqConfig, String consumer, String dfault) {
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
     * @param mqConfig
     * @param consumerId
     * @param topic
     * @param tagList
     * @param procssorBean   消费MQ消息的Proessor实现类 bean id
     * @param messageType           消息类型 一般消息/有序消息/事务消息
     */
    public static void subscribe(MqConfig mqConfig, String consumerId , String topic, List<String> tagList, String procssorBean , Integer messageType){
        try{
            mqConfig.setConsumerId(consumerId);
            DefaultMQPushConsumer consumer = RocketFactory.createConsumer(mqConfig);
            if(StringUtils.isBlank(consumerId)){
                throw new MQException("consumerId is null");
            }
            if(messageType == null){
                messageType = MsgTypeConstant.CONCURRENTLY;
            }

            String subExpression = getSubExpression(tagList);
            consumer.subscribe(topic, subExpression);

            if(MsgTypeConstant.CONCURRENTLY == messageType.intValue() ){
                listenerOrderly(consumer , topic , procssorBean);
            }

            if(MsgTypeConstant.ORDERLY == messageType.intValue()){
                listenerConcurrently(consumer , topic , procssorBean);
            }

            consumer.start();
            logger.info("Connected to {}.{} subscribe to {}", mqConfig.getNameSrvAddr() , consumerId , topic);
        } catch (Exception exception) {
            logger.info("Failed to connect to {}", mqConfig.getNameSrvAddr(), exception);
        }
    }

    /**
     * 监听一般消息
     *
     * @param consumer
     */
    public static void listenerConcurrently(DefaultMQPushConsumer consumer ,String topic , String processorBean){
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                ConsumeConcurrentlyStatus consumeStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                try {
                    for( MessageExt message : list) {
                        logger.info("\nReceive message success! msgContent:{}, reconsumeTimes:{} , MessageId:{} , topic:{}",
                                JSON.toJSONString(SerializeHelper.deSerialize(message.getBody())), message.getReconsumeTimes(), message.getMsgId(), topic);
                        if (StringUtils.isNotBlank(processorBean)) {
                            IMessageProcessor processor = SpringHelper.getBean(processorBean);
                            consumeStatus = (ConsumeConcurrentlyStatus)ConsumerHelper.consumeMessage(message, processor , MsgTypeConstant.CONCURRENTLY);
                        }else {
                            logger.error("topic:{},not found consumer config!" , topic);
                            consumeStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        }
                        // 有消息消费失败 将全部置为失败
                        if(consumeStatus ==  ConsumeConcurrentlyStatus.RECONSUME_LATER){
                            break;
                        }
                        logger.info("\nReceive Concurrently message:{}, msgContent:{}, reconsumeTimes:{} , MessageId:{} , topic:{}",consumeStatus,
                                JSON.toJSONString(SerializeHelper.deSerialize(message.getBody())), message.getReconsumeTimes(), message.getMsgId(), topic);
                    }
                    return consumeStatus;
                } catch (Exception e) {
                    logger.error("消费消息出错," , e);
                    consumeStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return consumeStatus;
            }
        });
    }

    /**
     * 监听有序消息
     *
     * @param consumer
     * @param topic
     * @param processorBean
     */
    public static void listenerOrderly(DefaultMQPushConsumer consumer ,String topic , String processorBean){
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                ConsumeOrderlyStatus consumeStatus = ConsumeOrderlyStatus.SUCCESS;
                // 设置自动提交事务
                consumeOrderlyContext.setAutoCommit(true);
                try {
                    for (MessageExt message : list) {
                        if (StringUtils.isNotBlank(processorBean)) {
                            IMessageProcessor processor = SpringHelper.getBean(processorBean);
                            consumeStatus = (ConsumeOrderlyStatus)ConsumerHelper.consumeMessage(message, processor , MsgTypeConstant.ORDERLY);
                        }else{
                            logger.error("topic:{},not found consumer config!" , topic);
                            consumeStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                        }
                        // 有消息消费失败 将全部置为失败
                        if(consumeStatus ==  ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT){
                            break;
                        }
                        logger.info("\nReceive Orderly message:{}, msgContent:{}, reconsumeTimes:{} , MessageId:{} , topic:{}",consumeStatus,
                                JSON.toJSONString(SerializeHelper.deSerialize(message.getBody())), message.getReconsumeTimes(), message.getMsgId(), topic);
                    }
                    return consumeStatus;
                }catch (Exception e){
                    //如果消息处理有问题.返回一个状态,告诉mq暂停一下,稍后在继续拉消息
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
        });
    }


    private static Object consumeMessage(MessageExt message , IMessageProcessor processorBean , Integer msgType){
        Object consumeStatus = null;
        Object obj = SerializeHelper.deSerialize(message.getBody());
        if (obj instanceof MessageVo) {
            ((MessageVo) obj).setRetryCount(message.getReconsumeTimes());
            ((MessageVo) obj).setMessageId(message.getMsgId());
        }
        MessageResponse result = processorBean.process(obj);

        if (result == null || CommonCanstant.SUCCESS_CODE == result.getErrCode()) {
            if(msgType == MsgTypeConstant.CONCURRENTLY) {
                consumeStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            if(msgType == MsgTypeConstant.ORDERLY){
                consumeStatus = ConsumeOrderlyStatus.SUCCESS;
            }
        } else {
            logger.error("consumer message error! topic:{}", message.getTopic());
            if(msgType == MsgTypeConstant.CONCURRENTLY) {
                consumeStatus =  ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            if(msgType == MsgTypeConstant.ORDERLY){
                consumeStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return consumeStatus;
    }

    /**
     * 组装subExpression
     *
     * @param tagList
     * @return
     */
    private static String getSubExpression(Collection<String> tagList) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(tagList)) {
            for(String tag : tagList) {
                if (sb.length() > 0) {
                    sb.append("||");
                } else {
                    sb.append(tag);
                }
            }
        }
        return sb.toString();
    }
}
