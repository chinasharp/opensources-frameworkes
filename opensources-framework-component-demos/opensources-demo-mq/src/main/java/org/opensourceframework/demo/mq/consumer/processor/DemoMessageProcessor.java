package org.opensourceframework.demo.mq.consumer.processor;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.mq.contant.MqContants;
import org.opensourceframework.component.mq.annotation.MQSubscribe;
import org.opensourceframework.component.mq.api.IMessageProcessor;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 一般消息处理类
 *
 * @author yuce
 * */
@Component(value = "demoMessageProcessor")
@MQSubscribe(consumer = MqContants.CONSUMER, topic = MqContants.TOPIC , tag = MqContants.TAG  ,messageType = MsgTypeConstant.CONCURRENTLY)
public class DemoMessageProcessor implements IMessageProcessor<MessageVo> {
    private static Logger logger = LoggerFactory.getLogger(DemoMessageProcessor.class);
    @Override
    public MessageResponse process(MessageVo messageVo) {
        logger.info("\n>>>>>>>Subscribe MQ Message. messgae:{}" , JSON.toJSONString(messageVo));
        return MessageResponse.SUCCESS;
    }
}
