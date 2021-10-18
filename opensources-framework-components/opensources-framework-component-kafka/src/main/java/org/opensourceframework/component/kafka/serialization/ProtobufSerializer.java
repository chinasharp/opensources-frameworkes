package org.opensourceframework.component.kafka.serialization;

import org.opensourceframework.base.helper.ProtobufSerializationHelper;
import org.opensourceframework.base.vo.MessageVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protobuf 序列化
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class ProtobufSerializer<T> implements Serializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(ProtobufSerializer.class);
    /**
     * Convert {@code data} into a byte array.
     *
     * @param topic topic associated with data
     * @param data  typed data
     * @return serialized bytes
     */
    @Override
    public byte[] serialize(String topic, T data) {
        try {
            if (data == null) {
                return null;
            } else {
                return ProtobufSerializationHelper.serialize(data , MessageVo.class);
            }
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageVo to byte[]. stack trace:" + ExceptionUtils.getStackTrace(e));
        }
    }
}
