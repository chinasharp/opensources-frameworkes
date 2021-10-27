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
 * sqlSessionFactory bean注入Spring条件 规避引入的jar中有重复定义sqlSessionFactory而引发启动报错
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class InitSessionFactoryCondition implements Condition {
	private static final Logger logger = LoggerFactory.getLogger(InitSessionFactoryCondition.class);
	private static final String BEAN_SQLSESSION_FACTORY = "sqlSessionFactory";
	private static final String BEAN_DEFINITION_MAP = "beanDefinitionMap";

	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		//判断是否已经包含了dataSource
		try {
			Boolean isContain = conditionContext.getBeanFactory().containsBean(BEAN_SQLSESSION_FACTORY);
			if (isContain) {
				Map<String, BeanDefinition> beanDefinitionMap = (Map<String, BeanDefinition>) ReflectHelper.getFieldValue(conditionContext.getBeanFactory(), "beanDefinitionMap");
				Map.Entry<String, BeanDefinition> sqlSessionFactory = null;
				for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
					if (BEAN_SQLSESSION_FACTORY.equals(entry.getKey())) {
						sqlSessionFactory = entry;
						break;
					}
				}

				if (sqlSessionFactory != null) {
					beanDefinitionMap.remove(sqlSessionFactory.getKey(), sqlSessionFactory.getValue());
					ReflectHelper.setFieldValue(conditionContext.getBeanFactory(), BEAN_DEFINITION_MAP, beanDefinitionMap);
				}

			}
		}catch (Exception e){
			logger.error("替换sqlSessionFactory异常:{}" , e.getMessage());
		}
		return true;
	}
}
