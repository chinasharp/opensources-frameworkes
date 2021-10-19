package org.opensourceframework.component.mq.helper;

import org.opensourceframework.component.mq.exception.MQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * MQ消息序列化与反序列化
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SerializeHelper {
    private static final Logger logger = LoggerFactory.getLogger(SerializeHelper.class);

    public SerializeHelper() {
    }

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream output = null;

        try {
            baos = new ByteArrayOutputStream(1024);
            output = new ObjectOutputStream(baos);
            output.writeObject(obj);
        } catch (IOException e) {
            logger.error("SerializeCode 转换byte[]时出错", e);
            throw new MQException("SerializeCode 转换byte[]时出错", e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                    baos.close();
                } catch (IOException e) {
                }
            }

        }

        return baos != null ? baos.toByteArray() : null;
    }

    public static Object deSerialize(byte[] in) {
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream input = null;

        try {
            bais = new ByteArrayInputStream(in);
            input = new ObjectInputStream(bais);
            obj = input.readObject();
        } catch (Throwable throwable) {
            logger.error("SerializeCode 转换byte[]出错", throwable);
            throw new MQException("SerializeCode 转换object时出错", throwable);
        } finally {
            if (input != null) {
                try {
                    input.close();
                    bais.close();
                } catch (IOException e) {
                }
            }

        }

        return obj;
    }
}
