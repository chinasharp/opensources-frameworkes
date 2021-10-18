package org.opensourceframework.demo.storm.mq;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.storm.spout.MsgSpout;
import org.opensourceframework.component.mq.annotation.MQSubscribe;
import org.opensourceframework.component.mq.api.IMessageProcessor;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 消息处理类
 *
 * @author yuce
 * */
@Component(value = "stormMsgProcessor")
@MQSubscribe(consumer = "GID_STORM_MSG" , topic = "MQ_STORM_MSG_TOPIC" , tag = "MQ_STORM_MSG_TAG"  ,messageType = MsgTypeConstant.CONCURRENTLY)
public class StormMsgProcessor implements IMessageProcessor<MessageVo> {
    private static Logger logger = LoggerFactory.getLogger(StormMsgProcessor.class);
    @Override
    public MessageResponse process(MessageVo messageVo) {
        logger.info("Subscribe MQ Message. bizCode：{}" , messageVo.getBizCode());
        try {
            MsgSpout.msgQueue.put(messageVo.getMsgContent());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return MessageResponse.ERROR;
        }
        return MessageResponse.SUCCESS;
    }
}
