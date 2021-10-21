package org.opensourceframework.starter.nacos.processor;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
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

import java.util.*;

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

		// DubboProtocolConfig设置host属性 解决没配置host属性 debug模式出现connect refuse错误
		beanDefinition = beanDefinitionRegistry.getBeanDefinition(PROTOCOL_DUBBO_CONFIG);
		if(beanDefinition != null){
			Map<String , Object> attributeMap = (Map)beanDefinition.getAttribute("configurationProperties");
			Map newAttributeMap = new HashMap();
			attributeMap.forEach((k ,v)->{
				newAttributeMap.put(k , v);
			});
			newAttributeMap.put("host" , IpAddressHelper.resolveLocalIp());

			// 单机同时运行多个dubbo应用时 避免端口冲突
			Object port = newAttributeMap.get("port");
			if(port == null || Objects.equals(-1 , port)){
				newAttributeMap.put("port" , RandomUtils.nextInt(20880 , 21880));
			}
			beanDefinition.setAttribute("configurationProperties" , Collections.unmodifiableMap(newAttributeMap));
			System.out.println(newAttributeMap);
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}
}
