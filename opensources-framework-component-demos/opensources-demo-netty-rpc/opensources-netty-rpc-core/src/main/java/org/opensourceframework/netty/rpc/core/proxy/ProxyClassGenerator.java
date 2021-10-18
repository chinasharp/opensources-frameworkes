package org.opensourceframework.netty.rpc.core.proxy;

import org.opensourceframework.base.helper.ReflectHelper;
import javassist.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用javassist代理生成类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/1/4 下午2:26
 */
public final class ProxyClassGenerator {
	private static final AtomicInteger counter = new AtomicInteger(1);

	/**
	 * 缓存
	 */
	private static final ConcurrentHashMap<Class<?>, Object> proxyInstanceCache = new ConcurrentHashMap<>();

	public static Object getProxyInstance(Class<?> interfaceClass, InvocationHandler invocationHandler) throws Exception {

		if (interfaceClass.isPrimitive()) {
			throw new IllegalArgumentException("Can not create wrapper for primitive type: " + interfaceClass);
		}

		//判断是否在缓存中
		if (proxyInstanceCache.containsKey(interfaceClass)) {
			return proxyInstanceCache.get(interfaceClass);
		}

		String name = interfaceClass.getName();
		ClassLoader classLoader = getClassLoader(interfaceClass);

		//生成类容器
		ClassPool pool = ClassPool.getDefault();

		//生成代理类名
		String className = buildClassName(interfaceClass);
		CtClass proxy = pool.makeClass(className);
		proxy.addInterface(pool.get(interfaceClass.getName()));

		//添加InvocationHandler变量
		CtField hf = CtField.make("private " + InvocationHandler.class.getName() + " handler;", proxy);
		proxy.addField(hf);

		//生成构造函数，参数为InvocationHandler
		//并给定义的handler 变量赋值
		CtConstructor constructor = new CtConstructor(new CtClass[]{pool.get(InvocationHandler.class.getName())}, proxy);
		constructor.setBody("this.handler = $1;");
		constructor.setModifiers(Modifier.PUBLIC);
		proxy.addConstructor(constructor);

		//无参构造函数添加
		proxy.addConstructor(CtNewConstructor.defaultConstructor(proxy));

		Method[] methods = interfaceClass.getMethods();
		// get all public method.
		boolean hasMethod = hasMethods(methods);
		if (hasMethod) {
			for (int methodIndex = 0 ; methodIndex < methods.length ; methodIndex++) {
				Method method = methods[methodIndex];
				//方法返回值类型
				Class<?> rt = methods[methodIndex].getReturnType();
				//方法参数类型
				Class<?>[] argsTypes = method.getParameterTypes();

				StringBuilder code = new StringBuilder();
				Class<?>[] aa = new Class[1];
				code.append(" Class[] argsTypes = new java.lang.Class[" + argsTypes.length + "]").append(";");
				code.append(" Object[] argsVals = new Object[" + argsTypes.length + "]").append(";");
				for(int argIndex = 0 ; argIndex < argsTypes.length ; argIndex++){
					code.append(" argsTypes[" + argIndex + "] = $").append(argIndex + 1).append(".getClass()").append(";");
					code.append(" argsVals[" + argIndex + "] = ($w)$").append(argIndex + 1).append(";");
				}

				code.append(" java.lang.reflect.Method method = ReflectHelper.getAccessibleMethod(this , \"" + method.getName() +"\" , argsTypes )").append(";");
				code.append(" Object invokeRes = handler.invoke(this, method , argsVals);");
				if (!Void.TYPE.equals(rt)) {
					code.append(" return ").append(asArgument(rt, "invokeRes")).append(";");
				}

				StringBuilder sb = new StringBuilder(1024);
				sb.append(modifierCode(method.getModifiers())).append(' ').append(getParameterType(rt)).append(' ').append(method.getName());
				sb.append('(');
				for (int i = 0; i < argsTypes.length; i++) {
					if (i > 0) {
						sb.append(',');
					}
					sb.append(getParameterType(argsTypes[i]));
					sb.append(" arg").append(i);
				}
				sb.append(')');
				//方法抛出异常
				Class<?>[] ets = method.getExceptionTypes();
				if (ets != null && ets.length > 0) {
					sb.append(" throws ");
					for (int i = 0; i < ets.length; i++) {
						if (i > 0) {
							sb.append(',');
						}
						sb.append(getParameterType(ets[i]));
					}
				}
				sb.append('{').append(code.toString()).append('}');
				System.out.println(sb.toString());
				CtMethod ctMethod = CtMethod.make(sb.toString() , proxy);
				proxy.addMethod(ctMethod);
			}

		}

		Class<?> proxyClass = proxy.toClass();
		Object proxyInstance = proxyClass.newInstance();
		ReflectHelper.setFieldValue(proxyInstance , "handler" , invocationHandler);
		return proxyInstance;
	}



	/**
	 * 数组类型返回 String[]
	 *
	 * @param c
	 * @return
	 */
	public static String getParameterType(Class<?> c) {
		//数组类型
		if (c.isArray()) {
			StringBuilder sb = new StringBuilder();
			do {
				sb.append("[]");
				c = c.getComponentType();
			} while (c.isArray());
			return c.getName() + sb.toString();
		}
		return c.getName();
	}


	/**
	 * 访问权限获取
	 *
	 * @param mod
	 * @return
	 */
	private static String modifierCode(int mod) {
		if (Modifier.isPublic(mod)) {
			return "public";
		}
		if (Modifier.isProtected(mod)) {
			return "protected";
		}
		if (Modifier.isPrivate(mod)) {
			return "private";
		}
		return "";
	}


	private static String asArgument(Class<?> clazz, String name) {
		if (clazz.isPrimitive()) {
			if (clazz == Boolean.TYPE) {
				return "((Boolean)" + name + ").booleanValue()";
			}
			if (clazz == Byte.TYPE) {
				return "((Byte)" + name + ").byteValue()";
			}
			if (clazz == Character.TYPE) {
				return "((Character)" + name + ").charValue()";
			}
			if (clazz == Double.TYPE) {
				return "((Number)" + name + ").doubleValue()";
			}
			if (clazz == Float.TYPE) {
				return "((Number)" + name + ").floatValue()";
			}
			if (clazz == Integer.TYPE) {
				return "((Number)" + name + ").intValue()";
			}
			if (clazz == Long.TYPE) {
				return "((Number)" + name + ").longValue()";
			}
			if (clazz == Short.TYPE) {
				return "((Number)" + name + ").shortValue()";
			}
			throw new RuntimeException("Unknown primitive type: " + clazz.getName());
		}
		return "(" + getName(clazz) + ")" + name;
	}

	/**
	 * get name.
	 * java.lang.Object[][].class => "java.lang.Object[][]"
	 *
	 * @param c class.
	 * @return name.
	 */
	public static String getName(Class<?> c) {
		if (c.isArray()) {
			StringBuilder sb = new StringBuilder();
			do {
				sb.append("[]");
				c = c.getComponentType();
			}
			while (c.isArray());

			return c.getName() + sb.toString();
		}
		return c.getName();
	}

	private static String buildClassName(Class<?> interfaceClass){
		return String.format("%s$Proxy%d", interfaceClass.getName(), counter.getAndIncrement());
	}

	private static boolean hasMethods(Method[] methods) {
		if (methods == null || methods.length == 0) {
			return false;
		}
		for (Method m : methods) {
			if (m.getDeclaringClass() != Object.class) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get class loader
	 *
	 * @param clazz
	 * @return class loader
	 */
	public static ClassLoader getClassLoader(Class<?> clazz) {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = clazz.getClassLoader();
			if (cl == null) {
				// getClassLoader() returning null indicates the bootstrap ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the caller can live with null...
				}
			}
		}

		return cl;
	}

}
