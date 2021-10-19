package org.opensourceframework.starter.nacos.processor;

import org.opensourceframework.starter.nacos.helper.IpAddressHelper;
import org.opensourceframework.starter.nacos.helper.ReflectHelper;
import org.opensourceframework.starter.nacos.registry.springcloud.NacosRegistration;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	private static final String PROTOCOL_DUBBO_CONFIG = "dubbo";

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		// 替换nacosRegistration 增加元数据信息,展示所有的具体服务请求path
		BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(NACOS_REGISTRATION_BEAN);
		if(beanDefinition != null) {
			beanDefinitionRegistry.removeBeanDefinition(NACOS_REGISTRATION_BEAN);
			beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(NacosRegistration.class).getBeanDefinition();
			beanDefinitionRegistry.registerBeanDefinition(NACOS_REGISTRATION_BEAN, beanDefinition);
		}

		// 给protocolConfig设置host属性 解决没配置host属性出现的connect refuse错误
		beanDefinition = beanDefinitionRegistry.getBeanDefinition(PROTOCOL_DUBBO_CONFIG);
		if(beanDefinition != null){
			Map<String , Object> attributeMap = (Map)beanDefinition.getAttribute("configurationProperties");
			Map newAttributeMap = new HashMap();
			attributeMap.forEach((k ,v)->{
				newAttributeMap.put(k , v);
			});
			newAttributeMap.put("host" , IpAddressHelper.resolveLocalIp());
			beanDefinition.setAttribute("configurationProperties" , Collections.unmodifiableMap(newAttributeMap));
			System.out.println(newAttributeMap);
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}
}
