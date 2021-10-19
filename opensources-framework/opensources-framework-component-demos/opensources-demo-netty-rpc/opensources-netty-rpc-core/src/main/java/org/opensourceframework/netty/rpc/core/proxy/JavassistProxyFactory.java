package org.opensourceframework.netty.rpc.core.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * Javassist 代理工厂
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/1/4 下午3:32
 */
public class JavassistProxyFactory implements ProxyFactory {
	/**
	 * @param interfaceClass
	 * @param handler
	 * @return
	 * @throws Throwable
	 */
	@Override
	public <T> T getProxy(Class<T> interfaceClass, InvocationHandler handler) throws Throwable {
		T proxy = (T)ProxyClassGenerator.getProxyInstance(interfaceClass , handler);
		return proxy;
	}
}
