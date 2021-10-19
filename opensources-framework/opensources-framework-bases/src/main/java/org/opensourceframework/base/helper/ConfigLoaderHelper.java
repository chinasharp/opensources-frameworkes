package org.opensourceframework.base.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 读取系统配置文件并存储在map
 * 
 * @time  2018年5月23日 上午11:38:43
 * @author yu.ce@foxmail.com/yu.ce@foxmail.com
 * @since 0.5.1
 */
public class ConfigLoaderHelper {
	private static final Logger logger = LoggerFactory.getLogger(ConfigLoaderHelper.class);

	/**
	 * 已经执行,不再重复执行.
 	 */
	private static final AtomicBoolean isExeced = new AtomicBoolean(false);

	/**
	 * 系统参数集合
	 */
	private static final Map<String, String> systemConfigMap = new HashMap<String, String>();

	/**
	 * 业务规则集合
	 */

	private ConfigLoaderHelper() {

	}

	/**
	 * 加载系统配置文件
	 */
	public static void loadSystemConfig(String[] paths) {
		if (isExeced.compareAndSet(false, true)) {
			logger.info("load registryvo.properties start......");
			PropertiesHelper propertiesHelper = new PropertiesHelper("registryvo.properties");
			Enumeration<Object> keys = propertiesHelper.getProperties().keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement().toString();
				String value = propertiesHelper.getProperty(key);
				ConfigLoaderHelper.put(key, value);
			}
			logger.info("load registryvo.properties end......");
		}
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public static void put(String key, String value) {
		systemConfigMap.put(key, value);
	}

	/**
	 * 根据key值返回value
	 * 
	 * @param key
	 * @return
	 */
	private static String getValue(String key) {
		return systemConfigMap.get(key);
	}

	/**
	 * 根据key值返回String类型的value.
	 */
	public static String getStringValue(String key) {
		return getValue(key);
	}

	/**
	 * 根据key值返回Integer类型的value.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static String getStringValue(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 根据key值返回Integer类型的value.如果都為Null或内容错误则抛出异常.
	 */
	public static Integer getIntegerValue(String key) {
		String value = getValue(key);
		if (value == null) {
			return null;
		}
		return Integer.valueOf(value);
	}

	/**
	 * 根据key值返回Integer类型的value.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 根据key值返回Double类型的value.如果都為Null或内容错误则抛出异常.
	 */
	public static Double getDoubleValue(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 根据key值返回Double类型的value.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public static Double getDoubleValue(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 根据key值返回Boolean类型的value.如果都為Null抛出异常,如果内容不是true/false则返回false.
	 */
	public static Boolean getBooleanValue(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 根据key值返回Boolean类型的value.如果都為Null則返回Default值,如果内容不为true/false则返回false.
	 */
	public static Boolean getBooleanValue(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
}
