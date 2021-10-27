package org.opensourceframework.starter.mybatis.base.threadquery;

import org.opensourceframework.base.helper.ReflectHelper;
import org.opensourceframework.common.spring.SpringBeanHelper;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 子线程查询/更新hash子表
 *
 * @author 空见
 * @since 1.0.0
 * @date  2019/2/22 10:19 AM
 */
public class SubHashTableCallable implements Callable<Object> {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SubHashTableCallable.class);

	/**
	 * 执行类的执行接口或者实现类
	 */
	private Class<?> springBeanClazz;

	/**
	 * 执行类的执行方法名
	 */
	private String methodName;

	/**
	 * 对应方法的参数
	 */
	private Object[] parameters;

	/**
	 * 对应方法参数的类型
	 */
	private Class<?>[] parameterTypes;


	public SubHashTableCallable(Class<?> springBeanClazz, String methodName, Object[] parameters, Class<?>[] parameterTypes) {
		super();
		this.springBeanClazz = springBeanClazz;
		this.methodName = methodName;
		this.parameters = parameters;
		this.parameterTypes = parameterTypes;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		Object springBean = SpringBeanHelper.getBean(springBeanClazz);
		return ReflectHelper.invokeMethod(springBean , methodName ,parameterTypes , parameters);
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Class<?> getSpringBeanClazz() {
		return springBeanClazz;
	}

	public void setSpringBeanClazz(Class<?> springBeanClazz) {
		this.springBeanClazz = springBeanClazz;
	}
}
