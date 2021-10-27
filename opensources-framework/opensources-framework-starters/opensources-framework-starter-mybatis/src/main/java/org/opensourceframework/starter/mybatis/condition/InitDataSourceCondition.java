package org.opensourceframework.starter.mybatis.condition;

import org.opensourceframework.base.helper.reflet.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * datasource bean注入Spring条件
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class InitDataSourceCondition implements Condition {
	private static final Logger logger = LoggerFactory.getLogger(InitDataSourceCondition.class);
	private static final String BEAN_DATASOURCE = "dataSource";
	private static final String BEAN_DEFINITION_MAP = "beanDefinitionMap";

	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		//判断是否已经包含了dataSource
		try {
			Boolean isContain = conditionContext.getBeanFactory().containsBean(BEAN_DATASOURCE);
			if (isContain) {
				Map<String, BeanDefinition> beanDefinitionMap = (Map<String, BeanDefinition>) ReflectHelper.getFieldValue(conditionContext.getBeanFactory(), "beanDefinitionMap");
				Map.Entry<String, BeanDefinition> datasource = null;
				for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
					if (BEAN_DATASOURCE.equals(entry.getKey())) {
						datasource = entry;
						break;
					}
				}

				if (datasource != null) {
					beanDefinitionMap.remove(datasource.getKey(), datasource.getValue());
					ReflectHelper.setFieldValue(conditionContext.getBeanFactory(), BEAN_DEFINITION_MAP, beanDefinitionMap);
				}

			}
		}catch (Exception e){
			logger.error("替换datasource异常:{}" , e.getMessage());
		}
		return true;
	}
}
