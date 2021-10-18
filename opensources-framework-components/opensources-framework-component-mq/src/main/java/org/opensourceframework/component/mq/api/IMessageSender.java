package org.opensourceframework.component.mq.api;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionListener;

/**
 * 消息发送接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IMessageSender<T extends MessageVo> {
    /**
     * 发送一般消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @return
     */
    MessageResponse sendMessage(String topic, String tag, T msg);


    /**
     * 发送消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @param messageType  消息类型 1:一般消息 2:有序消息 3:事务消息
     * @return
     */
    MessageResponse sendMessage(String topic, String tag, T msg , Integer messageType);

    /**
     * 发送延时消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @param delayTimeLevel
     *               延时等级,级别分别对应延时时间Level 1:1秒,Level 2:5秒,Level 3:10秒,Level 4:30秒,Level 5:1分钟,
     *               依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h,delayTimeLevel=0,表示不延时发送
     * @return
     */
    MessageResponse sendDelayMessage(String topic, String tag, T msg, Integer delayTimeLevel);

    /**
     * 异步发送消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @param callback 回调函数
     * @return
     */
    void sendMessageAsync(String topic, String tag, T msg , SendCallback callback);


    /**
     * 异步发送延时消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @param delayTimeLevel
     *               延时等级,级别分别对应延时时间Level 1:1秒,Level 2:5秒,Level 3:10秒,Level 4:30秒,Level 5:1分钟,
     *               依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h,delayTimeLevel=0,表示不延时发送
     * @param callback 回调函数
     * @return
     */
    void sendDelayMessageAsync(String topic, String tag, T msg , Integer delayTimeLevel , SendCallback callback);

    /**
     * 异步发送延时消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @param delayTimeLevel
     *               延时等级,级别分别对应延时时间Level 1:1秒,Level 2:5秒,Level 3:10秒,Level 4:30秒,Level 5:1分钟,
     *               依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h,delayTimeLevel=0,表示不延时发送
     * @param callback 回调函数
     * @param timeout  发送超时时间
     */
    void sendDelayMessageAsync(String topic, String tag, MessageVo msg, Integer delayTimeLevel ,Long timeout , SendCallback callback);

    /**
     * 同步发送有序消息
     *
     * @param topic
     * @param tag
     * @param message
     * @return
     */
    MessageResponse sendOrderMessage(String topic, String tag, MessageVo message);

    /**
     * 发送顺序消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param bizCode 同一bizcode的消息 分配到一个MessageQueue
     * @return
     */
    MessageResponse sendOrderMessage(String topic , String tag, MessageVo message , String bizCode);

    /**
     * 延时发送顺序消息
     *
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
     * @return
     */
    MessageResponse sendOrderMessage(String topic , String tag, MessageVo message , String bizCode, Integer delayTimeLevel);

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
     * @return
     */
    MessageResponse sendOrderMessage(String topic, String tag, MessageVo message, String bizCode, Integer delayTimeLevel, Long timeout);

    /**
     * 异步顺序发送消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @param bizCode  均衡将消息发送到topic下的不同queue计算依据值,为空 则消息固定发送到topic下的第一个队列
     * @param callback 回调函数
     * @return
     */
    void sendOrderMessageAsync(String topic, String tag, T msg , String bizCode , SendCallback callback);

    /**
     * 异步顺序发送消息
     *
     * @param topic  主题名
     * @param tag    tag名
     * @param msg    消息体
     * @param delayTimeLevel
     *               延时等级,级别分别对应延时时间Level 1:1秒,Level 2:5秒,Level 3:10秒,Level 4:30秒,Level 5:1分钟,
     *               依次类推2m/3m/4m/5m/6m/7m/8m/9m/1Om/20m/30m/1h/2h,delayTimeLevel=0,表示不延时发送
     * @param bizCode  均衡将消息发送到topic下的不同queue计算依据值,为空 则消息固定发送到topic下的第一个队列
     * @param callback 回调函数
     * @return
     */
    void sendDelayOrderMessageAsync(String topic, String tag, T msg , String bizCode ,Integer delayTimeLevel , SendCallback callback);

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
    void sendOrderMessageAsync(String topic , String tag, T message, String bizCode, Integer delayTimeLevel ,  Long timeout , SendCallback sendCallback);

    /**
     * 发送事务消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param transactionListener   本地事务执行监听类
     * @param listenerArg           监听类中方法执行时需要回传的参数
     * @return
     */
    MessageResponse sendTransactionMessage(String topic, String tag , MessageVo message, TransactionListener transactionListener , Object listenerArg);

    /**
     * 发送事务消息 使用默认的本地事务执行监听类
     *
     * @param topic
     * @param tag
     * @param message
     * @return
     */
    MessageResponse sendTransactionMessage(String topic, String tag, MessageVo message);
}
