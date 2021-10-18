package org.opensourceframework.demo.helper.protobuf;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.helper.ProtobufSerializationHelper;
import org.opensourceframework.base.vo.MessageVo;

/**
 * Protobuf 序列化与反序列化
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
public class ProtobufDemo {
    public static void main(String[] args) {
        User user = new User(1L , "test" , "test_address" , 20);

        byte[] bytes = ProtobufSerializationHelper.serialize(user , MessageVo.class);

        user = ProtobufSerializationHelper.deserialize(bytes , User.class);

        System.out.printf(JSON.toJSONString(user));

    }
}
