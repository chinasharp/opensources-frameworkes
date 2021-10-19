package org.opensourceframework.component.kafka.serialization;

import org.opensourceframework.base.helper.ProtobufSerializationHelper;
import org.opensourceframework.base.vo.MessageVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * Protobuf 反序列化
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class ProtobufDeserializer implements Deserializer<MessageVo> {
    /**
     * Deserialize a record value from a byte array into a value or object.
     *
     * @param topic topic associated with the data
     * @param data  serialized bytes; may be null; implementations are recommended to handle null by returning a value or null rather than throwing
     *             an exception.
     * @return deserialized typed data; may be null
     */
    @Override
    public MessageVo deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            } else {
                return ProtobufSerializationHelper.deserialize(data , MessageVo.class);
            }
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[]. stack trace:" + ExceptionUtils.getStackTrace(e));
        }
    }
}
