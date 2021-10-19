package org.opensourceframework.starter.nacos.processor;

import org.opensourceframework.starter.nacos.helper.ReflectHelper;
import org.opensourceframework.starter.nacos.registry.springcloud.NacosRegistration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通过Bean前置处理扩展NacosRegistration
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class NacosBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {
	private static final String NACOS_REGISTRATION_BEAN = "nacosRegistration";

	private static final String  DUBBO_SERVICE_METADATA = "dubboMetadataUtils";

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		//反射获取 beanDefinitionMap
		Map<String, BeanDefinition> beanDefinitionMap
				= (Map<String, BeanDefinition>) ReflectHelper.getFieldValue(beanDefinitionRegistry,"beanDefinitionMap");
		for(Map.Entry<String,BeanDefinition> entry : beanDefinitionMap.entrySet()){
			BeanDefinition beanDefinition = entry.getValue();
			if(NACOS_REGISTRATION_BEAN.equals(entry.getKey())){
				beanDefinitionRegistry.removeBeanDefinition(entry.getKey());

				beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(NacosRegistration.class).getBeanDefinition();
				beanDefinitionRegistry.registerBeanDefinition(entry.getKey() , beanDefinition);
				break;
			}
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}
}
