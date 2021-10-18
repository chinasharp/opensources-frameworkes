/**
 * <html>
 * <body>
 *  <P> Copyright 2018 广东粤通宝电子商务有限公司 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2018年6月26日</p>
 *  <p> Created on 于策</p>
 *  </body>
 * </html>
 */
package org.opensourceframework.base.helper;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * bean相关操作工具类
 *
 * * @author yu.ce@foxmal.com
 * @version: 1.0
 */
public class BeanHelper {
	private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);
	private static Map<Class<?>, Object> simpleBeanTypeMap = getSimpleBeanTypeMap();

	/**
	 * 将Bean对象转换成Map对象，将忽略掉值为null或size=0的属性
	 * 
	 * @param obj
	 * @return 若给定对象为null则返回size=0的map对象
	 */
	public static Map<String, Object> toMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj == null) {
			return map;
		}
		BeanMap beanMap = new BeanMap(obj);
		Iterator<String> it = beanMap.keyIterator();
		while (it.hasNext()) {
			String name = it.next();
			Object value = beanMap.get(name);
			// 转换时会将类名也转换成属性，此处去掉
			if (value != null && !"class".equalsIgnoreCase(name)) {
				map.put(name, value);
			}
		}
		return map;
	}

	/**
	 * 该方法将给定的所有对象参数列表转换合并生成一个Map，对于同名属性，依次由后面替换前面的对象属性
	 * 
	 * @param objs
	 *            对象列表
	 * @return 对于值为null的对象将忽略掉
	 */
	public static Map<String, Object> toMap(Object... objs) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object object : objs) {
			if (object != null) {
				map.putAll(toMap(object));
			}
		}
		return map;
	}

	/**
	 * dto对象和eo对象相互复制
	 *
	 * @param target
	 * @param source
	 * @param ignoreProperties
	 */
	public static void copyProperties(Object target, Object source, String... ignoreProperties) {
		if (target != null && source != null) {
			if (!simpleBeanTypeMap.containsKey(target.getClass()) && !simpleBeanTypeMap.containsKey(source.getClass())) {
				PropertyDescriptor[] targetPds = PropertyUtils.getPropertyDescriptors(target.getClass());
				PropertyDescriptor[] sourcePds = PropertyUtils.getPropertyDescriptors(source.getClass());
				Map<String, PropertyDescriptor> descriptorMap = getDescriptorMap(sourcePds);
				List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : new ArrayList();
				PropertyDescriptor sourcePd = null;
				Object value = null;

				try {
					int length = targetPds.length;

					for(int index = 0; index < length; ++index) {
						PropertyDescriptor targetPd = targetPds[index];
						Method writeMethod = targetPd.getWriteMethod();
						if (writeMethod != null && !ignoreList.contains(targetPd.getName())) {
							sourcePd = descriptorMap.get(targetPd.getName());
							if (sourcePd != null) {
								Method readMethod = sourcePd.getReadMethod();
								if (readMethod != null) {
									value = readMethod.invoke(source);
									if (null != value) {
										if (!simpleBeanTypeMap.containsKey(value.getClass())) {
											if (value instanceof Collection) {
												value = collectionCopy(targetPd, value);
											} else if (value instanceof Map) {
												value = mapCopy(targetPd, value);
											} else {
												Object newObj = value;
												try {
													newObj = writeMethod.getParameterTypes()[0].newInstance();
													copyProperties(newObj, value);
												} catch (Exception e) {
													logger.error("targetPd newInstance Exception:", e);
												}

												value = newObj;
											}
										}

										if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
											readMethod.setAccessible(true);
										}

										writeMethod.invoke(target, value);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					if (sourcePd != null && value != null) {
						Field field = null;

						try {
							field = target.getClass().getDeclaredField(sourcePd.getName());
						} catch (Exception exception) {
						}

						StringBuilder errorMsg = new StringBuilder();
						errorMsg.append("copyProperties Exception==>>");
						errorMsg.append(" sourceClassName:{}").append(source.getClass());
						errorMsg.append(", targetClassName:{}").append(target.getClass());
						errorMsg.append(", property:").append(sourcePd.getName());
						errorMsg.append(", sourceFieldClass:").append(value.getClass());
						errorMsg.append(", targetFieldClass:").append(field == null ? "null" : field.getType());
						throw new RuntimeException(errorMsg.toString());
					}

					logger.error("copyProperties Exception:", e);
				}

			}else{
				target = source;
			}
		} else {
			logger.info("---------copyProperties-------target or source is null");
		}
	}

	private static Map<String, PropertyDescriptor> getDescriptorMap(PropertyDescriptor[] descriptors) {
		Map<String, PropertyDescriptor> descriptorMap = new HashMap();
		int length = descriptors.length;

		for(int index = 0; index < length; ++index) {
			PropertyDescriptor descriptor = descriptors[index];
			descriptorMap.put(descriptor.getName(), descriptor);
		}

		return descriptorMap;
	}

	private static Object mapCopy(PropertyDescriptor descriptor, Object value) throws Exception {
		if (value == null) {
			return null;
		} else {
			//Class genericClazz = getGenericClazz(descriptor, value.getClass(), 1);
			Map targetMap = null;
			if (value.getClass().getSimpleName().contains("Persistent")) {
				targetMap = Maps.newHashMap();
			} else {
				targetMap = (Map)value.getClass().newInstance();
			}
			targetMap.putAll((Map)value);
			return targetMap;
		}
	}

	private static Object collectionCopy(PropertyDescriptor descriptor, Object value) throws Exception {
		if (value == null) {
			return null;
		} else {
			Object targetCollection = null;

			try {
				Class genericClazz = getGenericClazz(descriptor, value.getClass(), 0);
				Collection sourceCollection = (Collection)value;
				if (sourceCollection.isEmpty()) {
					return sourceCollection;
				}

				if (value.getClass().getSimpleName().contains("Persistent")) {
					if (value instanceof Set) {
						targetCollection = Sets.newHashSet();
					} else if (value instanceof List) {
						targetCollection = Lists.newArrayList();
					}
				} else if (value.getClass().getSimpleName().contains("ArrayList")) {
					targetCollection = Lists.newArrayList();
				} else {
					targetCollection = value.getClass().newInstance();
				}

				copyCollection((Collection)targetCollection, sourceCollection, genericClazz);
			} catch (Exception e) {
				logger.error("collectionCopy Exception:", e);
			}

			return targetCollection;
		}
	}

	private static Class getGenericClazz(PropertyDescriptor descriptor, Class<?> clazz, int index) {
		Class genericClazz = clazz;
		Type fc = descriptor.getWriteMethod().getGenericParameterTypes()[0];
		if (fc != null && fc instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)fc;
			genericClazz = (Class)pt.getActualTypeArguments()[index];
		}

		return genericClazz;
	}

	public static Map<Class<?>, Object> getSimpleBeanTypeMap() {
		if (simpleBeanTypeMap == null) {
			simpleBeanTypeMap = Maps.newHashMap();
			simpleBeanTypeMap.put(BigDecimal.class, null);
			simpleBeanTypeMap.put(BigInteger.class, null);
			simpleBeanTypeMap.put(Boolean.class, null);
			simpleBeanTypeMap.put(Byte.class, null);
			simpleBeanTypeMap.put(Character.class, null);
			simpleBeanTypeMap.put(Double.class, null);
			simpleBeanTypeMap.put(Float.class, null);
			simpleBeanTypeMap.put(Integer.class, null);
			simpleBeanTypeMap.put(Long.class, null);
			simpleBeanTypeMap.put(Short.class, null);
			simpleBeanTypeMap.put(String.class, null);
			simpleBeanTypeMap.put(Class.class, null);
			simpleBeanTypeMap.put(Date.class, null);
			simpleBeanTypeMap.put(LocalDateTime.class, null);
			simpleBeanTypeMap.put(Calendar.class, null);
			simpleBeanTypeMap.put(File.class, null);
			simpleBeanTypeMap.put(java.sql.Date.class, null);
			simpleBeanTypeMap.put(Time.class, null);
			simpleBeanTypeMap.put(Timestamp.class, null);
			simpleBeanTypeMap.put(URL.class, null);
		}

		return simpleBeanTypeMap;
	}

	/**
	 * dto集合数据与eo集合数据相互复制
	 *
	 * @param target
	 * @param source
	 * @param targetClazz
	 */
	public static void copyCollection(Collection target, Collection source, Class<?> targetClazz) {
		if (!CollectionUtils.isEmpty(source)) {
			Iterator iterator = source.iterator();

			while(true) {
				while(iterator.hasNext()) {
					Object sourceObj = iterator.next();
					if (simpleBeanTypeMap.containsKey(sourceObj.getClass())) {
						target.add(sourceObj);
					} else {
						Object newObj = null;

						try {
							newObj = targetClazz.newInstance();
						} catch (Exception e) {
							logger.error("copyCollection newInstance Exception:", e);
							target.add(sourceObj);
							continue;
						}

						copyProperties(newObj, sourceObj);
						target.add(newObj);
					}
				}

				return;
			}
		}
	}

	/**
	 * Dto分页数据预Eo分页数据的相互复制
	 *
	 * @param target
	 * @param source
	 * @param targetListClazz
	 */
	public static void copyPageInfo(PageInfo target , PageInfo source , Class<?> targetListClazz){
		copyProperties(target , source , "list" , "navigatepageNums");
		List targetList = Lists.newArrayList();
		List sourceList = source.getList();
		copyCollection(targetList, sourceList, targetListClazz);
		target.setList(targetList);
		target.setNavigatepageNums(source.getNavigatepageNums());
	}

}
