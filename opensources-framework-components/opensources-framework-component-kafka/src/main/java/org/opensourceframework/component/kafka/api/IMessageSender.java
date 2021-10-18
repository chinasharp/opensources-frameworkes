package org.opensourceframework.component.kafka.api;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.apache.kafka.clients.producer.Callback;

/**
 * TODO
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public interface IMessageSender<T extends MessageVo> {
    /**
     *
     * @param topic
     * @param partition
     * @param key
     * @param messageVo
     */
    void send(String topic , Integer partition , String key , MessageVo messageVo);

    /**
     *
     * @param topic
     * @param partition
     * @param key
     * @param timeOut
     * @param messageVo
     * @return
     */
    MessageResponse snycSend(String topic , Integer partition , String key , Integer timeOut , MessageVo messageVo);

    /**
     *
     * @param topic
     * @param partition
     * @param key
     * @param messageVo
     * @param callback
     * @return
     */
    MessageResponse asnycSend(String topic , Integer partition , String key  ,MessageVo messageVo , Callback callback);
}
