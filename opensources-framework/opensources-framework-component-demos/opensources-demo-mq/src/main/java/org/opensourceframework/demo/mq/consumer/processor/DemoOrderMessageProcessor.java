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
@Component(value = "demoOrderMessageProcessor")
@MQSubscribe(consumer = MqContants.ORDER_CONSUMER, topic = MqContants.ORDER_TOPIC , tag = MqContants.TAG  ,messageType = MsgTypeConstant.ORDERLY)
public class DemoOrderMessageProcessor implements IMessageProcessor<MessageVo> {
    private static Logger logger = LoggerFactory.getLogger(DemoOrderMessageProcessor.class);
    @Override
    public MessageResponse process(MessageVo messageVo) {
        logger.info("\n>>>>>>>Subscribe MQ Message. messgae:{}" , JSON.toJSONString(messageVo));
        return MessageResponse.SUCCESS;
    }
}
