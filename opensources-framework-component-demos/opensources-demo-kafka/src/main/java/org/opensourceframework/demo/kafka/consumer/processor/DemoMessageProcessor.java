package org.opensourceframework.demo.kafka.consumer.processor;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.kafka.contant.KafkaContants;
import org.opensourceframework.component.kafka.annotation.KafkaSubscribe;
import org.opensourceframework.component.kafka.api.IKafkaMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 使用KafkaSubscribe注解 订阅kafka消息例子
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Component(value = "demoMessageProcessor")
@KafkaSubscribe(consumer = KafkaContants.CONSUMER, topic = KafkaContants.TOPIC)
public class DemoMessageProcessor implements IKafkaMessageProcessor{
    private static final Logger logger = LoggerFactory.getLogger(DemoMessageProcessor.class);
    /**
     * 消息业务处理方法
     *
     * @param messageVo
     */
    @Override
    public void process(List<MessageVo> messageVo) {
        logger.info("\n User KafkaSubscribe Annotation Process message. data:{}" , JSON.toJSONString(messageVo));
    }

    /**
     * 异常处理方法
     *
     * @param messageVo
     */
    @Override
    public void exceptionhandle(List<MessageVo> messageVo) {
        logger.info("User KafkaSubscribe Annotation Process message exception. data:{}" , JSON.toJSONString(messageVo));
    }
}
