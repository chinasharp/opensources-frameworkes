package org.opensourceframework.base.helper.reflet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ReflectHelper {
	private static final Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

	public ReflectHelper() {
	}

	public static List<Field> getAllFields(Class clz) {
		ArrayList fields = new ArrayList();

		do {
			fields.addAll(Lists.newArrayList(clz.getDeclaredFields()));
			clz = clz.getSuperclass();
		} while (!Object.class.equals(clz));

		return fields;
	}

	public static Field getField(Class clz, String fileName) throws Exception {
		Field field = null;
		Class orgClz = clz;

		do {
			try {
				field = clz.getDeclaredField(fileName);
			} catch (NoSuchFieldException e) {
			}

			clz = clz.getSuperclass();
		} while (field == null && !Object.class.equals(clz));

		if (field != null) {
			return field;
		} else {
			throw new Exception("查找class:" + orgClz.getName() + "的field:" + fileName + " 失败");
		}
	}

	public static Object invokeGetterMethod(Object obj, String propertyName) {
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[0], new Object[0]);
	}


	public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
		invokeSetterMethod(obj, propertyName, value, null);
	}


	public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
	}


	public static Object getFieldValue(Object obj, String fieldName) {
		Field field = getAccessibleField(obj, fieldName);
		Object result = null;
		if (field == null) {
			return result;
		}
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("ReflectHelper.getFieldValue is error. case:{}", e.getMessage());
			result = null;
		}
		return result;
	}


	public static void setFieldValue(Object obj, String fieldName, Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}


	public static Field getAccessibleField(Object obj, String fieldName) {
		if (obj == null) {
			throw new NullPointerException("ReflectionUtil.getAccessibleField(final Object obj, final String property)的obj参数不能为空");
		}
		if (StringUtils.isBlank(fieldName)) {
			throw new NullPointerException("ReflectionUtil.getAccessibleField(final Object obj, final String property)的fieldName参数不能为空");
		}
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}


	public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}


	public static Method getAccessibleMethod(Object obj, String methodName, Class<?>... parameterTypes) {
		if (obj == null) {
			logger.error("ReflectionUtil.getAccessibleField(final Object obj, final String property)的obj参数不能为空");
			throw new NullPointerException("object不能为空");
		}

		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

				method.setAccessible(true);

				return method;
			} catch (NoSuchMethodException e) {
				logger.error("NoSuchMethodException!", e);
			}
		}
		return null;
	}


	public static <T> Class<T> getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}


	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if ((index >= params.length) || (index < 0)) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}


	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException)) || ((e instanceof NoSuchMethodException))) {
			return new IllegalArgumentException("Reflection Exception.", e);
		}
		if ((e instanceof InvocationTargetException)) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		}
		if ((e instanceof RuntimeException)) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	public static Map<String ,Object> getStaticField(Class clazz) throws Exception {
		Map<String ,Object> fieldMap = Maps.newHashMap();

		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			return fieldMap;
		}

		for (Field field : fields) {
			field.setAccessible(true);
			if (Modifier.isStatic(field.getModifiers())) {
				fieldMap.put(field.getName() , field.get(clazz));
			}
		}
		return fieldMap;
	}

	public static Object getStaticField(Class clazz , String staticFieldName) {
		Object obj = null;

		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			return obj;
		}

		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				if (Modifier.isStatic(field.getModifiers()) && fieldName.equals(staticFieldName)) {
					obj = field.get(clazz);
				}
			}
		}catch (Exception e){
			obj = null;
		}
		return obj;
	}

	/**
	 * 获取对象class
	 *
	 * @param obj
	 * @return
	 */
	public static Class<?> getObjType(Object obj) {
		//得到Object的所有属性，第一个就是类型type
		Field[] fileds = obj.getClass().getDeclaredFields();
		Class<?> type = null;
		int i;
		for (i = 0; i < fileds.length; i++) {
			type = fileds[i].getType();
			break;
		}
		return type;
	}
}
