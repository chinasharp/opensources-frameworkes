package org.opensourceframework.netty.rpc.core.proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 消费者通用bean
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/1/5 下午1:48
 */
@Component
public class ConsumerBean implements FactoryBean {
	private Class<?> consumerClass;

	@Override
	public Object getObject() throws Exception {
		return get();
	}

	private Object get() throws Exception{
		JavassistProxyFactory proxyFactory = new JavassistProxyFactory();
		ProxyInvocationHandler handler = new ProxyInvocationHandler(consumerClass);
		try {
			return proxyFactory.getProxy(consumerClass , handler);
		} catch (Throwable throwable) {
			throw new Exception(throwable);
		}
	}

	@Override
	public Class<?> getObjectType() {
		return consumerClass;
	}

	public Class<?> getConsumerClass() {
		return consumerClass;
	}

	public void setConsumerClass(Class<?> consumerClass) {
		this.consumerClass = consumerClass;
	}
}
