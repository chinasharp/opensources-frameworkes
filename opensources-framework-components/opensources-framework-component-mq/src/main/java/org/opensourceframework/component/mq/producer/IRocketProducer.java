package org.opensourceframework.component.mq.producer;

import org.opensourceframework.base.rest.MessageResponse;
import org.apache.rocketmq.client.producer.SendCallback;

/**
 * 消息生产者接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IRocketProducer {
    /**
     * 发送消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param bizCode  有序消息时 使用该参数 无序消息传null即可
     * @param delayTimeLevel 级别分别对应下面的延时时间<br>
     *        Level 1:1秒<br>
     *        Level 2:5秒<br>
     *        Level 3:10秒<br>
     *        Level 4:30秒<br>
     *        Level 5:1分钟<br>
     *        依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h<br>
     * @param timeout  发送超时时间
     * @param messageType
     * @return
     */
    MessageResponse sendMessage(String topic, String tag, Object message , String bizCode ,Integer delayTimeLevel , Long timeout , Integer messageType);

    /**
     * 发送延时无序消息
     * @param topic
     * @param tag
     * @param message
     * @param delayTimeLevel 级别分别对应下面的延时时间<br>
     *        Level 1:1秒<br>
     *        Level 2:5秒<br>
     *        Level 3:10秒<br>
     *        Level 4:30秒<br>
     *        Level 5:1分钟<br>
     *        依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h<br>
     * @param timeout  发送超时时间
     * @return
     */
    MessageResponse sendConcurrentlyMessage(String topic, String tag, Object message, Integer delayTimeLevel, Long timeout);

    /**
     * 异步发送无序消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param callback
     * @return
     */
    void sendAsyncConcurrentlyMessage(String topic , String tag, Object message,SendCallback callback);

    /**
     * 异步发送无序消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param delayTimeLevel
     * @param timeout
     * @param sendCallback
     */
    void sendAsyncConcurrentlyMessage(String topic , String tag, Object message, Integer delayTimeLevel ,  Long timeout , SendCallback sendCallback);


    /**
     * 延时发送顺序消息
     * @param topic
     * @param tag
     * @param message
     * @param bizCode 同一bizcode的消息 分配到一个MessageQueue
     * @param delayTimeLevel 级别分别对应下面的延时时间<br>
     *        Level 1:1秒<br>
     *        Level 2:5秒<br>
     *        Level 3:10秒<br>
     *        Level 4:30秒<br>
     *        Level 5:1分钟<br>
     *        依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h<br>
     * @param timeout  发送超时时间
     * @return
     */
    MessageResponse sendOrderMessage(String topic, String tag, Object message, String bizCode, Integer delayTimeLevel, Long timeout);

    /**
     * 异步发送顺序消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param sendCallback
     * @return
     */
    void sendAsyncOrderMessage(String topic , String tag, Object message, String bizCode, SendCallback sendCallback);

    /**
     * 异步发送顺序消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param bizCode
     * @param delayTimeLevel
     * @param sendCallback
     */
    void sendAsyncOrderMessage(String topic , String tag, Object message, String bizCode, Integer delayTimeLevel , SendCallback sendCallback);

    /**
     * 异步发送顺序消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param bizCode
     * @param delayTimeLevel 级别分别对应下面的延时时间<br>
     *        Level 1:1秒<br>
     *        Level 2:5秒<br>
     *        Level 3:10秒<br>
     *        Level 4:30秒<br>
     *        Level 5:1分钟<br>
     *        依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h<br>
     * @param timeout  发送超时时间
     * @param sendCallback
     */
    void sendAsyncOrderMessage(String topic , String tag, Object message, String bizCode, Integer delayTimeLevel ,  Long timeout , SendCallback sendCallback);
}
