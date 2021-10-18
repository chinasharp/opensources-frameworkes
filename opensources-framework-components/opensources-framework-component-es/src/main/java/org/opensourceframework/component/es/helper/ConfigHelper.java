package org.opensourceframework.component.es.helper;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.component.es.base.BaseEsEo;
import org.opensourceframework.component.es.config.ESDocument;
import org.opensourceframework.component.es.config.ESField;
import org.opensourceframework.component.es.processor.ESScan;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 配置帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ConfigHelper {
	private final static Logger logger = LoggerFactory.getLogger(ConfigHelper.class);
	public final static String EO_END_CHAR = "Eo";

	/**
	 * es实体字段类型本地缓存
	 */
	private static final ConcurrentMap<String, ESDocument> esDocumentMap = new ConcurrentHashMap<>();

	/**
	 * 扫描注解加载
	 *
	 * @return
	 */
	public static Map<String, ESDocument> scanLoad(String scanPath) {
		logger.info("ES Mapping Scan Load Start");
		try {
			ESScan esScan = new ESScan();
			Set<Class<? extends BaseEsEo>> classes = esScan.scanClass(scanPath);
			if (CollectionUtils.isNotEmpty(classes)) {
				for (Class<? extends BaseEsEo> clazz : classes) {
					BaseEsEo baseEsEo = clazz.newInstance();
					ESDocument esDocument = baseEsEo.initDocument();
					if(esDocument != null){
						esDocumentMap.putIfAbsent(ConfigHelper.camelToUnderline(clazz.getSimpleName()) , esDocument);
					}
				}
			}
		} catch (Exception e) {

		}
		logger.info("ES Mapping Scan Load End. Size:{}" , esDocumentMap.size());
		return esDocumentMap;
	}

	/**
	 * 根据类查找ESDocument
	 *
	 * @param clazz
	 * @return
	 */
	public static ESDocument getESDocument(Class<?> clazz){
		String className = ConfigHelper.camelToUnderline(clazz.getSimpleName());
		return esDocumentMap.get(className);
	}

	/**
	 * 获取类对应的index
	 */
	public static String getIndex(Class<?> clazz){
		String index = null;
		ESDocument esDocument = getESDocument(clazz);
		if(esDocument != null){
			index = esDocument.getIndex();
		}
		return index;
	}


	/**
	 * java驼峰风格 转为数据库字段格式
	 *
	 * @param name
	 * @return
	 */
	public static String camelToUnderline(String name) {
		if (StringUtils.isEmpty(name)) {
			return "";
		} else {
			int len = name.length();
			StringBuilder sb = new StringBuilder(len);

			for (int i = 0; i < len; ++i) {
				char c = name.charAt(i);
				if (i > 0) {
					if (Character.isUpperCase(c)) {
						sb.append('_').append(Character.toLowerCase(c));
					} else {
						sb.append(c);
					}
				} else {
					sb.append(Character.toLowerCase(c));
				}

			}
			return sb.toString();
		}
	}

	/**
	 * java类型与ElasticSearch类型映射
	 *
	 * @param javaType
	 * @return
	 */
	public static String getESMappingType(String javaType) {
		String esType;
		switch (javaType) {
			case "Date":
				esType = "date";
				break;
			case "Double":
				esType = "double";
				break;
			case "Long":
				esType = "long";
				break;
			case "Integer":
				esType = "integer";
				break;
			case "String":
				esType = "keyword";
				break;
			case "BigDecimal":
			case "BigInteger":
				esType = "scaled_float";
				break;
			default:
				esType = "text";
				break;

		}
		return esType;
	}


	public static String buildMapping(ESDocument esDocument) {
		String jsonMapping = null;

		Map<String, Object> mappingMap = new HashMap<>();
		Map<String, Object> contentMap = new HashMap<>();
		mappingMap.put(esDocument.getIndex(), contentMap);

		Map<String, Object> headerMap = buildMappingHeader(esDocument);
		if (MapUtils.isNotEmpty(headerMap)) {
			contentMap.putAll(headerMap);
		}

		Map<String, Object> propertiesMap = buildMappingProperties(esDocument);
		if (MapUtils.isNotEmpty(propertiesMap)) {
			contentMap.put("properties", propertiesMap);
			jsonMapping = JSON.toJSONString(mappingMap);
		}

		return jsonMapping;
	}


	/**
	 * 构建头部信息
	 *
	 * @param esDocument
	 * @return
	 */
	private static Map<String, Object> buildMappingHeader(ESDocument esDocument) {
		Map<String, Object> headerMap = new HashMap<>();
		//Boolean dynamic = esDocument.getDynamic();
		//Boolean allFlg = esDocument.getAll();
		//if (allFlg == null) {
		//	allFlg = false;
		//}

		// headerMap.put("dynamic", dynamic);

		// Map<String, Object> allMap = Maps.newHashMap();
		// allMap.put("enabled", allFlg);

		// headerMap.put("_all", allMap);
		return headerMap;
	}

	/**
	 * 构建properties信息
	 *
	 * @param esDocument
	 * @return
	 */
	private static Map<String, Object> buildMappingProperties(ESDocument esDocument) {
		Map<String, Object> propertiesMap = new HashMap<>();

		List<ESField> esFieldList = esDocument.getEsFieldList();
		for (ESField esField : esFieldList) {
			Map<String, Object> propertyMap = new HashMap<>();
			propertyMap.put("type", esField.getType());
			if (StringUtils.isNotBlank(esField.getAnalyzer())) {
				propertyMap.put("analyzer", esField.getAnalyzer());
				propertyMap.put("search_analyzer", esField.getSearchAnalyzer());
			}
			if (StringUtils.isNotBlank(esField.getFormat())) {
				propertyMap.put("format", esField.getFormat());
			}

			if("scaled_float".equalsIgnoreCase(esField.getType())){
				propertyMap.put("scaling_factor", esField.getScalingFactor());
			}

			propertiesMap.put(esField.getColumn(), propertyMap);
		}
		return propertiesMap;
	}

	/**
	 * 根据index 获取ESMapping
	 *
	 * @param index
	 * @return
	 */
	public ESDocument findEsMapping(String index){
		ESDocument esDocument = null;
		Collection<ESDocument> esDocuments = esDocumentMap.values();
		if(CollectionUtils.isNotEmpty(esDocuments)){
			for(ESDocument mapping : esDocuments){
				if(mapping.getIndex().equals(index)){
					esDocument = mapping;
					break;
				}
			}
		}
		return esDocument;
	}

	public static ESField buildESColumn(Field field , boolean isHumpCol) {
		String column = null;
		String type = null;
		String analyzer = null;
		String search_analyzer = null;
		String format = null;
		Integer scalingFactor = null;

		if (field.isAnnotationPresent(org.opensourceframework.component.es.annotation.ESField.class)) {
			org.opensourceframework.component.es.annotation.ESField esField = field.getAnnotation(org.opensourceframework.component.es.annotation.ESField.class);
			column = esField.column();
			type = esField.type();
			analyzer = esField.analyzer();
			search_analyzer = esField.search_analyzer();
			format = esField.format();
			scalingFactor = esField.scalingFactor();
		} else {
			if(!isHumpCol) {
				column = ConfigHelper.camelToUnderline(field.getName());
			}else {
				column = field.getName();
			}
			type = ConfigHelper.getESMappingType(field.getType().getSimpleName());
			analyzer = ESField.DEF_ANALYZER;
			search_analyzer = ESField.DEF_ANALYZER;
			scalingFactor = 1000;
		}
		ESField esField = new ESField(column, type);

		if (ESField.IK_TYPE.equalsIgnoreCase(type)) {
			if (org.apache.commons.lang.StringUtils.isNotBlank(analyzer)) {
				esField.setAnalyzer(analyzer);
			} else {
				esField.setAnalyzer(ESField.DEF_ANALYZER);
			}

			if (org.apache.commons.lang.StringUtils.isNotBlank(search_analyzer)) {
				esField.setSearchAnalyzer(search_analyzer);
			} else {
				esField.setSearchAnalyzer(ESField.DEF_ANALYZER);
			}
		}

		if(ESField.SCALED_FLOAT_TYPE.equalsIgnoreCase(type)){
			esField.setScalingFactor(scalingFactor);
		}

		return esField;
	}
}
