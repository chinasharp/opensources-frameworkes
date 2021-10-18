package org.opensourceframework.base.helper;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protobuf 协议序列化与反序列化
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
public class ProtobufSerializationHelper{
    private static final Logger logger = LoggerFactory.getLogger(ProtobufSerializationHelper.class);

    /**
     * protouf反序列化
     *
     * @param data
     * @param deserializeClass
     * @return
     */
    public static <T> T deserialize(byte[] data , Class<T> deserializeClass) {
        T t = null;
        try {
            if (data == null) {
                return null;
            } else {
                if (!deserializeClass.isAnnotationPresent(ProtobufClass.class)) {
                    throw new SerializationException("Missing @ProtobufClass annotation for serialized class!");
                }
                Codec<T> msgCodec = ProtobufProxy.create(deserializeClass);
                t = msgCodec.decode(data);
            }
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[]. stack trace:" + ExceptionUtils.getStackTrace(e));
        }
        return t;
    }

    /**
     * protouf序列化
     *
     * @param data
     * @param defSerializClass
     * @return
     */
    public static <T> byte[] serialize(T data, Class defSerializClass) {
        try {
            if (data == null) {
                return null;
            } else {
                if (data.getClass().isAnnotationPresent(ProtobufClass.class)) {
                    Codec<T> dataCodec = ProtobufProxy.create((Class<T>) data.getClass());
                    return dataCodec.encode(data);
                } else {
                    if (data instanceof String) {
                        Object msg = JSON.parseObject((String) data, defSerializClass);
                        Codec msgCodec = ProtobufProxy.create(defSerializClass);
                        return msgCodec.encode(msg);
                    } else {
                        throw new SerializationException("Serializer Data Need @interface:@ProtobufClass");
                    }
                }
            }
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageVo to byte[]. stack trace:" + ExceptionUtils.getStackTrace(e));
        }
    }
}
