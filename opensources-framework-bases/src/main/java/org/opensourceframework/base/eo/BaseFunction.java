package org.opensourceframework.base.eo;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 说明:
 * 1、继承Serializable接口的函数式接口支持序列化
 * 2、jdk1.8开始，凡是继承了Serializable的函数式接口的实例都可以获取一个属于它的SerializedLambda实例,并且通过它获取到方法的名称
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@FunctionalInterface
public interface BaseFunction<T , R> extends Function<T , R> , Serializable {
}
