package org.opensourceframework.demo.kafka.producer;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.kafka.contant.KafkaContants;
import org.opensourceframework.component.kafka.api.IMessageSender;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 使用IMessageSender 发送Kafka消息例子
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
@Component
public class MessageSendService implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendService.class);
    @Autowired
    private IMessageSender<MessageVo> messageSender;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 1.发送消息,不关心是否发送成功
        sendMessage();

        // 2.发送同步消息,等待发送结果
        sendMessageSync();

        // 3.异步发送消息
        sendMessageAsync();
    }

    private void sendMessage() {
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("send message");
        messageSender.send(KafkaContants.TOPIC, null, null, messageVo);
    }

    private MessageResponse sendMessageSync() {
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("sync send message");
        MessageResponse messageResponse = messageSender.snycSend(KafkaContants.TOPIC, null, null, null, messageVo);
        return messageResponse;
    }

    private MessageResponse sendMessageAsync() {
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgContent("async send message");
        MessageResponse messageResponse = messageSender.asnycSend(KafkaContants.TOPIC, null, null, messageVo, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                logger.info("Callback: Send Message Success!");
            }
        });
        return messageResponse;
    }
}
