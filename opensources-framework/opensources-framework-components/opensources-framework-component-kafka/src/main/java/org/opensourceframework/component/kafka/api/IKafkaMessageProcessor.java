package org.opensourceframework.component.kafka.api;

import org.opensourceframework.base.vo.MessageVo;

import java.util.List;

/**
 * 消息业务处理类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public interface IKafkaMessageProcessor {
    /**
     * 消息业务处理方法
     */
    void process(List<MessageVo> messageVo);

    /**
     * 异常处理方法
     *
     * @param messageVo
     */
    void exceptionhandle(List<MessageVo> messageVo);
}
