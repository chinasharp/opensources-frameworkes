package org.opensourceframework.component.dao.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mapper调用扩展
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MapperInvocationHandler implements InvocationHandler {
	private Object target;
	private Object backUpTarget;

	public MapperInvocationHandler() {
	}

	public MapperInvocationHandler(BaseMapper target, BaseMapper backUpTarget) {
		this.target = target;
		this.backUpTarget = backUpTarget;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Method[] methods = this.target.getClass().getDeclaredMethods();
		for (Method me : methods) {
			if (me.getName().equals(method.getName())) {
				return method.invoke(this.target, args);
			}
		}
		return method.invoke(this.backUpTarget, args);
	}
}
