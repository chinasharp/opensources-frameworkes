package org.opensourceframework.component.es.base;

import com.alibaba.fastjson.annotation.JSONField;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.component.es.annotation.ESDocument;
import org.opensourceframework.component.es.annotation.ESField;
import org.opensourceframework.component.es.helper.ConfigHelper;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础ES 实体类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午4:53
 */
public class BaseEsEo implements Serializable {
	@JSONField(serialize = false)
	@Transient
	protected Condition condition;

	@ESField(column = "_id", type = "long")
	protected Long id;

	public org.opensourceframework.component.es.config.ESDocument initDocument() {
		return buildESDocument();
	}


	private org.opensourceframework.component.es.config.ESDocument buildBaseInfo(){
		ESDocument esDocument = this.getClass().getAnnotation(ESDocument.class);
		String index = esDocument.index();
		if(org.apache.commons.lang3.StringUtils.isBlank(index)){
			index = getIndex(this.getClass());
		}
		String type = esDocument.type();
		if (StringUtils.isBlank(type)) {
			type = index;
		}
		long maxResultWindow = esDocument.maxResultWindow();

		org.opensourceframework.component.es.config.ESDocument document = new org.opensourceframework.component.es.config.ESDocument();
		document.setIndex(index);
		document.setMaxResultWindow(maxResultWindow);

		return document;
	}


	private static String getIndex(Class<? extends BaseEsEo> clazz){
		String className = clazz.getSimpleName();
		int index = className.indexOf(ConfigHelper.EO_END_CHAR);
		if(index > 0){
			className = className.substring(0 , index);
		}
		className = ConfigHelper.camelToUnderline(className);
		return className;
	}

	/**
	 * 实体对象属性和表字段的关系列表
	 */
	private org.opensourceframework.component.es.config.ESDocument buildESDocument() {
		org.opensourceframework.component.es.config.ESDocument document = null;
		Class<?> clazz = this.getClass();
		if (clazz.isAnnotationPresent(ESDocument.class)) {
			document = buildBaseInfo();

			ESDocument esDocument = this.getClass().getAnnotation(ESDocument.class);
			boolean isHumpCol = esDocument.isHumpCol();
			List<org.opensourceframework.component.es.config.ESField> esFieldList = new ArrayList<>();
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				try {
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						// 修饰符是static的字段不处理
						if (Modifier.isStatic(field.getModifiers())) {
							continue;
						}

						org.opensourceframework.component.es.config.ESField esField = ConfigHelper.buildESColumn(field, isHumpCol);
						esFieldList.add(esField);
					}
				} catch (Exception e) {
				}
			}
			document.setEsFieldList(esFieldList);


			String jsonMapping = ConfigHelper.buildMapping(document);
			if (org.apache.commons.lang.StringUtils.isNotBlank(jsonMapping)) {
				document.setMapping(jsonMapping);
			}
		}
		return document;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
