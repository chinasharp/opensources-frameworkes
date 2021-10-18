package org.opensourceframework.component.mq.api;

import org.opensourceframework.base.rest.MessageResponse;

/**
 * 消息订阅接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IMessageProcessor<T> {
    /**
     * 处理消息
     *
     * @param t
     * @return
     */
    MessageResponse process(T t);
}
