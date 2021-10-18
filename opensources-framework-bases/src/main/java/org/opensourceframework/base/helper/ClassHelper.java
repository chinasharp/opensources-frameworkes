package org.opensourceframework.base.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class 帮助类
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
public class ClassHelper {
    private final static Logger logger = LoggerFactory.getLogger(ClassHelper.class);
    /**
     * 获得类的泛型参数 自身类为实现接口的类
     *
     * @param clazz
     * @param defaultClass
     * @return
     */
    public static Class getGenericClassForSuperInterface(Class clazz , Class defaultClass) {
        Class genericClass = null;
        try {
            ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericInterfaces()[0];
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if(actualTypeArguments[0] instanceof Class) {
                genericClass = (Class) actualTypeArguments[0];
            }else {
                genericClass = defaultClass;
            }
        } catch (Exception e) {
            if(logger.isDebugEnabled()) {
                logger.debug("not found GenericClass for {} , Please confirm whether it is an interface implementation class.", clazz.getName());
            }
            genericClass = defaultClass;
        }
        return genericClass;
    }

    /**
     * 获得类的泛型参数 自身类为继承类
     *
     * @param clazz
     * @param defaultClass
     * @return
     */
    public static Class getGenericClassForSuperClass(Class clazz , Class defaultClass) {
        Class genericClass = null;
        try {
            ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if(actualTypeArguments[0] instanceof  Class) {
                genericClass = (Class) actualTypeArguments[0];
            }else {
                genericClass = defaultClass;
            }
        } catch (Exception e) {
            if(logger.isDebugEnabled()) {
                logger.error("not found GenericClass for {} , Please confirm whether it is an extends class.", clazz.getName());
            }
            genericClass = defaultClass;
        }
        return genericClass;
    }

    /**
     * 获得类的泛型参数 兼容接口实现类和继承类
     *
     * @param clazz
     * @param defaultClass
     * @return
     */
    public static Class getGenericClass(Class clazz , Class defaultClass){
        Class genericClass = getGenericClassForSuperClass(clazz , null);
        if(genericClass == null){
            genericClass = getGenericClassForSuperInterface(clazz , null);
        }
        if(genericClass == null){
            genericClass = defaultClass;
        }
        return genericClass;
    }
}
