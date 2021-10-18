package org.opensourceframework.component.es.config;

import java.io.Serializable;
import java.util.List;

/**
 * ES 文档实体类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午5:01
 */
public class ESDocument implements Serializable {
	/**
	 * 索引名
	 */
	private String index;

	/**
	 * 类型
	 */
	private String type;
	/**
	 * 最大查询数
	 */
	private Long maxResultWindow;

	private List<ESField> esFieldList;

	/**
	 * 属性与Es Field映射Mapping
	 */
	private String mapping;

	public ESDocument() {
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public Long getMaxResultWindow() {
		return maxResultWindow;
	}

	public void setMaxResultWindow(Long maxResultWindow) {
		this.maxResultWindow = maxResultWindow;
	}

	public List<ESField> getEsFieldList() {
		return esFieldList;
	}

	public void setEsFieldList(List<ESField> esFieldList) {
		this.esFieldList = esFieldList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
