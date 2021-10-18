package org.opensourceframework.component.kafka.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 序列化相关帮助类
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
public class MessageSerializeHelper {
    /**
     * 获得实现接口的泛型参数
     *
     * @return
     */
    public static Class getTForSuperInterface(Class clazz , Class defaultClass) {
        Class tClass = null;
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericInterfaces()[0];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if(actualTypeArguments[0] instanceof Class) {
            tClass = (Class) actualTypeArguments[0];
        }else {
            tClass = defaultClass;
        }
        return tClass;
    }

    /**
     * 获得继承父类的泛型参数
     *
     * @return
     */
    public static Class getTForSuperClass(Class clazz) {
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class tClass = (Class)actualTypeArguments[0];
        return tClass;
    }
}
