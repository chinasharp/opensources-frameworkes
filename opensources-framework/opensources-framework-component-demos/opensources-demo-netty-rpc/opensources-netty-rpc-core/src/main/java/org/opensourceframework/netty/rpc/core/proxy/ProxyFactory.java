package org.opensourceframework.netty.rpc.core.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * 代理对象生成工厂
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/1/4 下午3:33
 */
public interface ProxyFactory {
	/**
	 *
	 * @param target
	 * @param handler
	 * @param <T>
	 * @return
	 * @throws Throwable
	 */
	<T> T getProxy(Class<T> target, InvocationHandler handler) throws Throwable;
}
