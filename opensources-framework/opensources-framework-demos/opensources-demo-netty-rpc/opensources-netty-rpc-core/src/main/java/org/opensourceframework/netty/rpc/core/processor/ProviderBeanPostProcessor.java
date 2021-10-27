package org.opensourceframework.netty.rpc.core.processor;


import org.opensourceframework.netty.rpc.core.annotation.RpcProvider;
import org.opensourceframework.netty.rpc.core.manager.ProviderManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Rpc 服务生成者 注解解析
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/29 下午6:15
 */
@Component
public class ProviderBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		boolean isRpcProvider = bean.getClass().isAnnotationPresent(RpcProvider.class);
		ProviderManager providerManager = ProviderManager.instance();

		if(isRpcProvider){
			providerManager.putProviderBean(bean);
		}
		return bean;
	}

}
