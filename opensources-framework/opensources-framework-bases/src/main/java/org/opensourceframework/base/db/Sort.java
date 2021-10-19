package org.opensourceframework.base.db;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 排序
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public class Sort implements Comparable<Sort> {
	/**
	 * 排序字段
	 */
	private String property;

	/**
	 * desc/asc
	 */
	private SortEnum order;

	/**
	 * 先后顺序
	 */
	private Integer index;

	private final List<Sort> sortList = Lists.newArrayList();

	public Sort() {
		super();
	}

	public Sort desc(String fieldName){
		int size = sortList.size();
		Sort sort = new Sort(fieldName ,SortEnum.DESC , size);
		sortList.add(sort);
		return sort;
	}

	public Sort asc(String fieldName){
		int size = sortList.size();
		Sort sort = new Sort(fieldName ,SortEnum.ASC , size);
		sortList.add(sort);
		return sort;
	}

	public Sort(String fieldName, SortEnum order) {
		this.property = fieldName;
		this.order = order;
		this.index = 0;
	}

	public Sort(String fieldName, SortEnum order, Integer index) {
		super();
		this.property = fieldName;
		this.order = order;
		this.index = index;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public SortEnum getOrder() {
		return order;
	}

	public void setOrder(SortEnum order) {
		this.order = order;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Sort other = (Sort) obj;
		if (property == null) {
			if (other.property != null) {
				return false;
			}
		} else if (!property.equals(other.property)) {
			return false;
		}
		if (index == null) {
			if (other.index != null) {
				return false;
			}
		} else if (!index.equals(other.index)) {
			return false;
		}
		return order == other.order;
	}

	@Override
	public int compareTo(Sort o) {
		// TODO Auto-generated method stub
		if (o == null || o.getIndex() == null) {
			return 1;
		} else {
			return index.compareTo(o.getIndex());
		}
	}
}
