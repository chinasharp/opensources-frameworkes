package org.opensourceframework.component.mq.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.RocketFactory;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.exception.MQException;
import org.opensourceframework.component.mq.constant.MQConstant;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * RocketMQ 消息生产者帮助工具类
 *
 * @author yuce
 * */
public class ProducerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ProducerHelper.class);

    /**
     * 发送一般消息
     *
     * @param mqConfig
     * @param topic
     * @param tag
     * @param message
     * @return
     */
    public static MessageResponse sendConcurrentlyMsg(MqConfig mqConfig, String topic, String tag, MessageVo message) {
        return sendMessage(mqConfig, topic, tag, message, 0, MQConstant.DEF_SEND_TIME_OUT, MsgTypeConstant.CONCURRENTLY, null, null);
    }

    /**
     * 发送顺序消息
     *
     * @param mqConfig
     * @param topic
     * @param tag
     * @param message
     * @return
     */
    public static MessageResponse sendOrderlyMsg(MqConfig mqConfig, String topic, String tag, MessageVo message) {
        return sendMessage(mqConfig, topic, tag, message, 0, MQConstant.DEF_SEND_TIME_OUT, MsgTypeConstant.ORDERLY, null, null);
    }


    private static Message buildMessage(String topic, String tag, Object message, Integer delayTimeLevel, Integer messageType) {
        if (messageType == null) {
            messageType = MsgTypeConstant.CONCURRENTLY;
        }
        Message msg = new Message(topic, tag, SerializeHelper.serialize(message));
        if (MsgTypeConstant.CONCURRENTLY == messageType) {
            msg.setKeys(MQConstant.COMMON_MSG_ID_PREFIX + getMessageId(message));
        }
        if (MsgTypeConstant.ORDERLY == messageType) {
            msg.setKeys(MQConstant.ORDER_MSG_ID_PREFIX + getMessageId(message));
        }
        if (MsgTypeConstant.TRANSACTION == messageType) {
            msg.setKeys(MQConstant.TRANS_MSG_ID_PREFIX + getMessageId(message));
        }

        if (delayTimeLevel > 0) {
            msg.setDelayTimeLevel(delayTimeLevel);
        } else {
            msg.setDelayTimeLevel(0);
        }
        return msg;
    }

    /**
     * 发送消息
     *
     * @param mqConfig  MQ配置信息
     * @param topic
     * @param tag
     * @param message
     * @param timeout     为空则为默认值  30000L
     * @param messageType 默认值 1 一般消息
     * @return
     */
    public static MessageResponse sendMessage(MqConfig mqConfig, String topic, String tag, Object message, Integer delayTimeLevel, Long timeout
            , Integer messageType, TransactionListener transactionListener, Object listenerArg) throws MQException {
        MessageResponse msgResponse = null;
        DefaultMQProducer producer = null;
        String messageId = null;
        try {
            producer = RocketFactory.buildProducer(mqConfig, transactionListener);
            if (producer != null) {
                Message msg = buildMessage(topic, tag, message, delayTimeLevel, messageType);

                if (timeout == null) {
                    timeout = MQConstant.DEF_SEND_TIME_OUT;
                }

                SendResult result = null;
                if (MsgTypeConstant.CONCURRENTLY == messageType) {
                    result = producer.send(msg, timeout);
                    messageId = result.getMsgId();
                    msgResponse = new MessageResponse(messageId);
                }
                if (MsgTypeConstant.ORDERLY == messageType) {
                    // 使用bizCode的hash值与 List<MessageQueue>的大小取模 得到发送到那个MessageQueue
                    Object bizCode = null;
                    if (message instanceof MessageVo) {
                        bizCode = ((MessageVo) message).getBizCode();
                    }
                    result = producer.send(msg, new MessageQueueSelector() {
                        @Override
                        public MessageQueue select(List<MessageQueue> list, Message message, Object bizCode) {
                            if (bizCode == null) {
                                return list.get(0);
                            } else {
                                Integer value = null;
                                try {
                                    value = Integer.valueOf(bizCode.toString());
                                } catch (NumberFormatException e) {
                                    value = bizCode.hashCode();
                                }

                                if (value < 0) {
                                    value = Math.abs(value);
                                }
                                value = value % list.size();
                                logger.info("val :{}", value);
                                return list.get(value);
                            }
                        }
                    }, bizCode, timeout);
                    messageId = result.getMsgId();
                    msgResponse = new MessageResponse(messageId);
                }

                if (MsgTypeConstant.TRANSACTION == messageType) {
                    TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, listenerArg);
                    // 事务消息发送成功，返回注册成功，因为是事务消息发送接口是同步调用调用，该方法调用结束后，可以根据对状态进行判断业务是否成功
                    if (sendResult.getSendStatus().equals(SendStatus.SEND_OK) && !sendResult.getLocalTransactionState().equals(LocalTransactionState.ROLLBACK_MESSAGE)) {
                        msgResponse = new MessageResponse(CommonCanstant.SUCCESS_CODE , "事务成功且消息发送成功");
                    } else {
                        String resMsg = sendResult.getLocalTransactionState().equals(LocalTransactionState.ROLLBACK_MESSAGE) ? "业务异常" : "系统繁忙";
                        msgResponse = new MessageResponse(CommonCanstant.FAILURE_CODE, resMsg);
                    }
                }
                logger.info("发送MQ消息成功,topic:{} , tag:{} , msgKey:{} , msgId:{} , data:{}", topic, tag, msg.getKeys(), messageId, JSON.toJSONString(message));
            }
        } catch (Exception exception) {
            logger.error("发送消息出错，topic：{} tag:{} , data:{} , exception:{}", topic, tag, JSON.toJSONString(message), exception);
            throw new MQException("10001", exception);
        }

        return msgResponse;
    }

    /**
     * 发送事务消息
     *
     * @param mqConfig
     * @param topic
     * @param tag
     * @param message
     * @param transactionListener
     * @param listenerArg
     * @return
     */
    public static MessageResponse sendTransactionMessage(MqConfig mqConfig, String topic, String tag, Object message,
                                                         TransactionListener transactionListener, Object listenerArg) {
        String listenerName = transactionListener.getClass().getSimpleName();
        String producerId = buildTransactionProducerId(topic , listenerName);
        if (StringUtils.isBlank(producerId)) {
            producerId = MQConstant.DEF_TRANSACTION_PRODUCER_ID;
        }

        MqConfig temp = new MqConfig();
        BeanHelper.copyProperties(temp , mqConfig);
        temp.setProducerId(producerId);
        return sendMessage(temp, topic, tag, message, 0, MQConstant.DEF_SEND_TIME_OUT, MsgTypeConstant.TRANSACTION, transactionListener,
                listenerArg);
    }

    public static String buildTransactionProducerId(String topic , String listenerName){
        String producerId = null;
        if(StringUtils.isNotBlank(listenerName)){
            int index = listenerName.indexOf("$");
            if(index > 0){
                listenerName = listenerName.substring(0 , index);
            }
        }
        if(StringUtils.isNotBlank(topic)){
            if(topic.indexOf(MQConstant.TOPIC_SUFFIX) > 0){
                producerId = topic.replace(MQConstant.TOPIC_SUFFIX , MQConstant.TRANSACTION_PRODUCER_SUFFIX);
            }else{
                producerId = topic.concat(MQConstant.MQ_CONFIG_SPLIT_CHAR).concat(MQConstant.TRANSACTION_PRODUCER_SUFFIX);
            }

            producerId = listenerName.toUpperCase().concat(MQConstant.MQ_CONFIG_SPLIT_CHAR).concat(producerId);
            producerId = MQConstant.PRODUCER_ID_PREFIX.concat(producerId);

        }else {
            producerId = MQConstant.DEF_TRANSACTION_PRODUCER_ID;
            String producerIdPrefix = MQConstant.PRODUCER_ID_PREFIX.concat(listenerName.toUpperCase()).concat(MQConstant.MQ_CONFIG_SPLIT_CHAR);
            producerId = producerId.replace(MQConstant.PRODUCER_ID_PREFIX , producerIdPrefix);
        }
        return producerId;
    }

    /**
     * 获取MessageId
     *
     * @param message
     * @return
     */
    private static String getMessageId(Object message) {
        String messageId = null;
        try {
            JSONObject jsonObject = JSON.parseObject(message.toString());
            Object obj = jsonObject.getString("messageId");
            if (obj != null) {
                messageId = obj.toString();
            }
        } catch (Exception e) {
        }

        if (messageId == null) {
            messageId = SnowFlakeId.nextId(null, null).toString();
        }
        return messageId;
    }


}
