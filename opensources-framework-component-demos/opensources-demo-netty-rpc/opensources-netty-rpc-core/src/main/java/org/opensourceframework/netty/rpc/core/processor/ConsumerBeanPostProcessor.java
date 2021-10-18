package org.opensourceframework.netty.rpc.core.processor;

import org.opensourceframework.netty.rpc.core.annotation.RpcConsumer;
import org.opensourceframework.netty.rpc.core.proxy.ConsumerBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 在bean实例化之前 使用postProcessProperties 替换@RpcConsumer标注的属性为代理对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 下午2:51
 */
@Component
public class ConsumerBeanPostProcessor implements InstantiationAwareBeanPostProcessor , BeanFactoryAware {
	@Autowired
	private ConsumerBean consumerBean;
	public static ConfigurableListableBeanFactory beanFactory = null;

	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean,
			String beanName) throws BeansException {
		if(beanFactory != null) {
			Field[] fields = bean.getClass().getDeclaredFields();
			if (fields != null && fields.length > 0) {
				try {
					for (Field field : fields) {
						if (field.isAnnotationPresent(RpcConsumer.class)) {
							Type type = field.getGenericType();
							Class<?> proxyClass = ClassUtils.forName(type.getTypeName(), Thread.currentThread().getContextClassLoader());

							// 是否已经
							boolean isHad = beanFactory.containsBean(field.getName());
							Object proxyBean = null;
							if (!isHad) {
								consumerBean.setConsumerClass(proxyClass);
								proxyBean = consumerBean.getObject();
								beanFactory.registerSingleton(field.getName(), proxyBean);
							}else {
								proxyBean = beanFactory.getBean(field.getName());
							}

							ReflectionUtils.makeAccessible(field);
							field.set(bean, proxyBean);
						}
					}
				} catch (Throwable t) {
					throw new BeanCreationException(t.getMessage(), t);
				}
			}
		}
		return pvs;
	}


	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if(beanFactory instanceof ConfigurableListableBeanFactory) {
			ConsumerBeanPostProcessor.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
		}else{

		}
	}

	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}
}
