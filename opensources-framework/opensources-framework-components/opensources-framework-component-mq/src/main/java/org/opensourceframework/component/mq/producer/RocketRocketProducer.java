package org.opensourceframework.component.mq.producer;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.microservice.ServiceContext;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.component.mq.BaseService;
import org.opensourceframework.component.mq.RocketFactory;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.exception.MQException;
import org.opensourceframework.component.mq.helper.SerializeHelper;
import org.opensourceframework.component.mq.constant.MQConstant;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RocketRocketProducer extends BaseService implements IRocketProducer {
    private static final Logger logger = LoggerFactory.getLogger(RocketRocketProducer.class);
    private static final RocketRocketProducer rocketProducer = new RocketRocketProducer();
    private DefaultMQProducer producer;

    private RocketRocketProducer() {
    }

    public static RocketRocketProducer getInstance() {
        return rocketProducer;
    }

    public void init(MqConfig mqConfig) {
        super.mqConfig = mqConfig;
        this.start(mqConfig);
    }

    @Override
    protected void start(MqConfig mqConfig) {
        try {
            this.producer = RocketFactory.buildDefaultProducer(mqConfig);
        } catch (Exception exception) {
            logger.error("创建Producer时出错,nameSrvAddr:{},accessKey:{},secretKey:{}", mqConfig.getNameSrvAddr(), mqConfig.getAccessKey(), mqConfig.getSecretKey(), exception);
            this.asyncWaitAndReconnect(mqConfig);
        }
    }

    @Override
    public MessageResponse sendMessage(String topic, String tag, Object message, String bizCode, Integer delayTimeLevel, Long timeOut, Integer messageType) {
        if (messageType == null) {
            messageType = MsgTypeConstant.CONCURRENTLY;
        }
        if (MsgTypeConstant.CONCURRENTLY == messageType) {
            return sendConcurrentlyMessage(topic, tag, message, delayTimeLevel, timeOut);
        }

        if (MsgTypeConstant.ORDERLY == messageType) {
            return sendOrderMessage(topic, tag, message, bizCode, delayTimeLevel, timeOut);
        }
        return null;
    }

    @Override
    public MessageResponse sendConcurrentlyMessage(String topic, String tag, Object message, Integer delayTimeLevel, Long timeout) {
        return this.sendMessageKernel(topic, tag, message, delayTimeLevel, timeout);
    }

    private MessageResponse sendMessageKernel(String topic, String tag, Object message, Integer delayTimeLevel, Long timeout) {
        String messageId = null;
        try {
            if (this.producer != null) {
                Message msg = new Message(topic, tag, SerializeHelper.serialize(message));
                msg.setKeys("MSG_ID_" + this.getMessageId(message));
                if (delayTimeLevel != null && delayTimeLevel > 0) {
                    msg.setDelayTimeLevel(delayTimeLevel);
                } else {
                    msg.setDelayTimeLevel(0);
                }

                if (timeout == null) {
                    timeout = MQConstant.DEF_SEND_TIME_OUT;
                }

                String requestId = ServiceContext.getContext().getRequestId();
                if (StringUtils.isNotBlank(requestId)) {
                    msg.putUserProperty(MQConstant.MQ_LOG_KEY, ServiceContext.getContext().getRequestId());
                }

                SendResult result = this.producer.send(msg, timeout);
                messageId = result.getMsgId();
                logger.info("发送MQ消息成功,topic:{},tag:{},msgKey:{},msgId:{},data:{}", topic, tag, msg.getKeys(), messageId, JSON.toJSONString(message));
            } else {
                this.asyncWaitAndReconnect(this.mqConfig);
            }

            return new MessageResponse(messageId);
        } catch (Exception exception) {
            logger.error("发送消息出错，topicName：{} tag:{}", topic, tag, exception);
            throw new MQException("10001", exception);
        }
    }

    @Override
    public void sendAsyncConcurrentlyMessage(String topic, String tag, Object message, Integer delayTimeLevel, Long timeout, SendCallback callback) {
        try {
            if (this.producer != null) {
                Message msg = new Message(topic, tag, SerializeHelper.serialize(message));
                if (delayTimeLevel != null && delayTimeLevel > 0) {
                    msg.setDelayTimeLevel(delayTimeLevel);
                } else {
                    msg.setDelayTimeLevel(0);
                }
                msg.setKeys("DELAY_MSG_ID_" + this.getMessageId(message));
                if (timeout == null) {
                    timeout = MQConstant.DEF_SEND_TIME_OUT;
                }

                this.producer.send(msg, callback, timeout);
            } else {
                this.asyncWaitAndReconnect(this.mqConfig);
            }
        } catch (Exception exception) {
            logger.error("异步发送延时消息出错,topic:{}, tag:{}", topic, tag, exception);
            throw new MQException("10001", exception);
        }
    }

    @Override
    public void sendAsyncConcurrentlyMessage(String topic, String tag, Object message, SendCallback callback) {
        try {
            if (this.producer != null) {
                Message msg = new Message(topic, tag, SerializeHelper.serialize(message));
                msg.setKeys("MSG_ID_" + this.getMessageId(message));
                this.producer.send(msg, callback);
            } else {
                this.asyncWaitAndReconnect(this.mqConfig);
            }
        } catch (Exception exception) {
            logger.error("异步发送延时消息出错,topic:{}, tag:{}", topic, tag, exception);
            throw new MQException("10001", exception);
        }
    }

    @Override
    public void sendAsyncOrderMessage(String topic, String tag, Object message, String bizCode, SendCallback sendCallback) {
        sendAsyncOrderMessage(topic, tag, message, bizCode, 0, sendCallback);
    }

    @Override
    public void sendAsyncOrderMessage(String topic, String tag, Object message, String bizCode, Integer delayTimeLevel, SendCallback sendCallback) {
        try {
            if (this.producer != null) {
                Message msg = new Message(topic, tag, SerializeHelper.serialize(message));
                if (delayTimeLevel != null && delayTimeLevel > 0) {
                    msg.setKeys("ASYNC_DELAYORDER_MSG_ID_" + this.getMessageId(message));
                } else {
                    msg.setKeys("ASYNC_ORDER_MSG_ID_" + this.getMessageId(message));
                }
                sendOrderMsg(msg, bizCode, delayTimeLevel, null, sendCallback);
            } else {
                this.asyncWaitAndReconnect(this.mqConfig);
            }
        } catch (Exception exception) {
            logger.error("异步发送顺序消息出错,topic:{}, tag:{}", topic, tag, exception);
            throw new MQException("10001", exception);
        }
    }

    @Override
    public void sendAsyncOrderMessage(String topic, String tag, Object message, String bizCode, Integer delayTimeLevel, Long timeout, SendCallback sendCallback) {
        try {
            if (this.producer != null) {
                Message msg = new Message(topic, tag, SerializeHelper.serialize(message));
                if (delayTimeLevel != null && delayTimeLevel > 0) {
                    msg.setKeys("ASYNC_DELAYORDER_MSG_ID_" + this.getMessageId(message));
                } else {
                    msg.setKeys("ASYNC_ORDER_MSG_ID_" + this.getMessageId(message));
                }
                sendOrderMsg(msg, bizCode, delayTimeLevel, timeout, sendCallback);
            } else {
                this.asyncWaitAndReconnect(this.mqConfig);
            }
        } catch (Exception exception) {
            logger.error("异步发送顺序消息出错,topic:{}, tag:{}", topic, tag, exception);
            throw new MQException("10001", exception);
        }
    }

    public MessageResponse sendOrderMsg(Message msg, String bizCode, Integer delayTimeLevel, Long timeout, SendCallback sendCallback) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String messageId = null;
        if (delayTimeLevel != null && delayTimeLevel > 0) {
            msg.setDelayTimeLevel(delayTimeLevel);
        } else {
            msg.setDelayTimeLevel(0);
        }

        if (timeout == null) {
            timeout = MQConstant.DEF_SEND_TIME_OUT;
        }
        if (this.producer != null) {
            MessageQueueSelector queueSelector = new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object bizCode) {
                    if (bizCode == null) {
                        return list.get(0);
                    } else {
                        int value = bizCode.hashCode();
                        if (value < 0) {
                            value = Math.abs(value);
                        }

                        value %= list.size();
                        return list.get(value);
                    }
                }
            };


            // 使用bizCode的hash值与 List<MessageQueue>的大小取模 得到发送到那个MessageQueue
            SendResult result = null;
            if (sendCallback != null) {
                producer.send(msg, queueSelector, bizCode, sendCallback, timeout);
                return MessageResponse.SUCCESS;
            } else {
                result = producer.send(msg, queueSelector, bizCode, timeout);
                return new MessageResponse(result.getMsgId());
            }
        } else {
            this.asyncWaitAndReconnect(this.mqConfig);
            return MessageResponse.FAIL;
        }
    }

    @Override
    public MessageResponse sendOrderMessage(String topic, String tag, Object message, String bizCode, Integer delayTimeLevel, Long timeout) {
        Message msg = new Message(topic, tag, SerializeHelper.serialize(message));
        msg.setKeys("ORDER_MSG_ID_" + this.getMessageId(message));
        if (delayTimeLevel == null) {
            delayTimeLevel = 0;
        }
        if (delayTimeLevel > 0) {
            msg.setDelayTimeLevel(delayTimeLevel);
        }

        if (timeout == null) {
            timeout = MQConstant.DEF_SEND_TIME_OUT;
        }

        String requestId = ServiceContext.getContext().getRequestId();
        if (StringUtils.isNotBlank(requestId)) {
            msg.putUserProperty(MQConstant.MQ_LOG_KEY, ServiceContext.getContext().getRequestId());
        }
        try {
            MessageResponse response = sendOrderMsg(msg, bizCode, delayTimeLevel, timeout, null);
            logger.info("发送MQ消息成功,topic:{},tag:{},msgKey:{},resp:{}", topic, tag, msg.getKeys(), JSON.toJSONString(response));
            return response;
        } catch (Exception exception) {
            logger.error("发送消息出错，topicName：{} tag:{}", topic, tag, exception);
            throw new MQException("10001", exception);
        }

    }

}
