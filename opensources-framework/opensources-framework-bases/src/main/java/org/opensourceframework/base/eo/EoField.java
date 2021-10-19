package org.opensourceframework.base.eo;

import java.io.Serializable;

/**
 * 实体对象的属性与表字段对应对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class EoField implements Serializable {
	/**
	 * 实体对象的属性名称
	 */
	private String eoAttribute;

	/**
	 * 实体对象对应的表字段名
	 */
	private String tableColumn;

	public EoField() {
	}

	public EoField(String eoAttribute, String tableColumn) {
		this.eoAttribute = eoAttribute;
		this.tableColumn = tableColumn;
	}

	public String getEoAttribute() {
		return eoAttribute;
	}

	public void setEoAttribute(String eoAttribute) {
		this.eoAttribute = eoAttribute;
	}

	public String getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}
}
