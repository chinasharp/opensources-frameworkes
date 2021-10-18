package org.opensourceframework.demo.mq.producer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.base.helper.DateHelper;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.mq.contant.MqContants;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 各类发送例子
 *
 */
@Component
public class MessageSendService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendService.class);

    @Autowired
    private IMessageSender messageSender;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 1.发送一般消息
        sendMessage();

        // 2.发送延时消息
        sendDelayMessage();

        // 3.异步发送一般消息
        sendMessageAsync();

        // 4.异步发送延时消息
        sendDelayMessageAsync();

        // 5.发送顺序消息
        sendOrderMessage();

        // 6.发送延时顺序消息
        sendDelayOrderMessage();

        // 7.异步发送顺序消息
        sendOrderMessageAsync();

        // 8.异步发送延时消息
        sendDelayOrderMessageAsync();
    }

    /**
     * 同步发送一般消息
     *
     * @return
     */
    private void sendMessage(){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("send common message");
        MessageResponse response = messageSender.sendMessage(MqContants.TOPIC , MqContants.TAG , messageVo);
        logger.info("sendMessage success. resp:{}" , JSON.toJSONString(response));
    }

    /**
     * 同步发送延时消息
     *
     * @return
     */
    private void sendDelayMessage(){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("send delay message");
        MessageResponse response = messageSender.sendDelayMessage(MqContants.TOPIC , MqContants.TAG , messageVo , MqContants.DELAY_TIME_LV);
        logger.info("sendDelayMessage success. resp:{}" , JSON.toJSONString(response));
    }

    /**
     * 异步发送一般消息
     *
     * @return
     */
    private void sendMessageAsync(){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("aysnc send common message");
        messageSender.sendMessageAsync(MqContants.TOPIC, MqContants.TAG, messageVo, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("sendMessage success. resp:{}" , JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("sendMessage exception. resp:{}" , JSON.toJSONString(throwable));
            }
        });
    }

    /**
     * 异步发送延时消息
     *
     * @return
     */
    private void sendDelayMessageAsync(){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("aysnc send delay message");
        Date sendTime = new Date();
        Map<String , String> dataMap = Maps.newHashMap();
        dataMap.put("sendTime" , DateHelper.formatDate(sendTime , DateHelper.YMD24H_DATA));
        messageVo.setExtendMap(dataMap);

        messageSender.sendDelayMessageAsync(MqContants.TOPIC, MqContants.TAG, messageVo, MqContants.DELAY_TIME_LV ,new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("sendDelayMessageAsync success. resp:{}" , JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("sendDelayMessageAsync exception. resp:{}" , JSON.toJSONString(throwable));
            }
        });
    }

    /**
     * 发送顺序消息
     */
    private void sendOrderMessage(){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("send order message");
        // 定义bizCode 将会根据bizCode % 16(topic下默认的队列总数) 的模值 , 将消息发送对应的队列 一般是用于业务下同一细分业务的数据发送到同一队列
        // 为空,则会固定发送到Topic下队列列表中的第一个队列
        Long bizCode = SnowFlakeId.nextId(0L , 0L);
        MessageResponse response = messageSender.sendOrderMessage(MqContants.ORDER_TOPIC , MqContants.TAG , messageVo , bizCode.toString());
        logger.info("sendOrderMessage success. resp:{}" , JSON.toJSONString(response));
    }

    /**
     * 同步发送延时顺序消息
     *
     * @return
     */
    private void sendDelayOrderMessage(){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("send delay order message");
        // 定义bizCode 将会根据bizCode % 16(topic下默认的队列总数) 的模值 , 将消息发送对应的队列 一般是用于业务下同一细分业务的数据发送到同一队列
        // 为空,则会固定发送到Topic下队列列表中的第一个队列
        Long bizCode = SnowFlakeId.nextId(0L , 0L);
        MessageResponse response = messageSender.sendOrderMessage(MqContants.ORDER_TOPIC , MqContants.TAG , messageVo ,bizCode.toString() , MqContants.DELAY_TIME_LV);
        logger.info("sendDelayOrderMessage success. resp:{}" , JSON.toJSONString(response));
    }

    /**
     * 异步发送一般消息
     *
     * @return
     */
    private void sendOrderMessageAsync(){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("aysnc send order message");
        Long bizCode = SnowFlakeId.nextId(0L , 0L);
        messageSender.sendOrderMessageAsync(MqContants.ORDER_TOPIC, MqContants.TAG, messageVo, bizCode.toString() , new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("sendOrderMessageAsync success. resp:{}" , JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("sendOrderMessageAsync exception. resp:{}" , JSON.toJSONString(throwable));
            }
        });
    }

    /**
     * 异步发送延时消息
     *
     * @return
     */
    private void sendDelayOrderMessageAsync(){
        MessageVo messageVo = new MessageVo();

        Date sendTime = new Date();
        Map<String , String> dataMap = Maps.newHashMap();
        dataMap.put("sendTime" , DateHelper.formatDate(sendTime , DateHelper.YMD24H_DATA));
        messageVo.setExtendMap(dataMap);

        messageVo.setMsgContent("aysnc send delay order message");
        Long bizCode = SnowFlakeId.nextId(0L , 0L);
        // 发送超时时间 默认值为30秒
        Long timeOut = 5000L;
        messageSender.sendOrderMessageAsync(MqContants.ORDER_TOPIC, MqContants.TAG, messageVo, bizCode.toString() , MqContants.DELAY_TIME_LV , timeOut , new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("sendDelayOrderMessageAsync success. resp:{}" , JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("sendDelayOrderMessageAsync exception. resp:{}" , JSON.toJSONString(throwable));
            }
        });
    }


}
