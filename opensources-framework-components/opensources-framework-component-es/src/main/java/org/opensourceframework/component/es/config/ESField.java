package org.opensourceframework.component.es.config;

import java.io.Serializable;

/**
 * 文档字段实体类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ESField implements Serializable {
	public static final String IK_TYPE = "string";
	public static final String DEF_ANALYZER = "ik";

	public static final String DATE_TYPE = "date";

	public static final String SCALED_FLOAT_TYPE = "scaled_float";

	protected String column;
	protected String type;
	protected String analyzer;
	protected String searchAnalyzer;
	protected String format;
	protected Integer scalingFactor;

	public ESField() {
	}

	public ESField(String column, String type) {
		this.column = column;
		this.type = type;
	}

	public ESField(String column, String type, String analyzer, String search_analyzer, String format) {
		this.column = column;
		this.type = type;
		this.analyzer = analyzer;
		this.searchAnalyzer = search_analyzer;
		this.format = format;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public String getSearchAnalyzer() {
		return searchAnalyzer;
	}

	public void setSearchAnalyzer(String searchAnalyzer) {
		this.searchAnalyzer = searchAnalyzer;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getScalingFactor() {
		return scalingFactor;
	}

	public void setScalingFactor(Integer scalingFactor) {
		this.scalingFactor = scalingFactor;
	}
}
