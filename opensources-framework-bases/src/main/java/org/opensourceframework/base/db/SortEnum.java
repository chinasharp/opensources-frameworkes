package org.opensourceframework.base.db;

import java.util.HashMap;
import java.util.Map;

/**
 * 排序enum
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum SortEnum {
	/**
	 * 升序
	 */
	ASC(1 , "asc"),

	/**
	 * 降序
	 */
	DESC(-1 , "desc");

	private final static Map<Integer, SortEnum> valCache = new HashMap<>(16);
	private final static Map<String, SortEnum> codeCache = new HashMap<>(16);

	static {
        for (SortEnum value : SortEnum.values()) {
            valCache.put(value.getSortVal(), value);
            codeCache.put(value.getCode(), value);
        }
    }

    public static SortEnum getByVal(Integer sortVal) {
	    return valCache.get(sortVal);
    }

    public static SortEnum getByCode(String code) {
	    return codeCache.get(code);
    }

	Integer sortVal;
	String code;

	SortEnum(Integer sortVal , String code) {
		this.sortVal = sortVal;
		this.code = code;
	}

	public Integer getSortVal() {
		return sortVal;
	}

	public String getCode() {
		return code;
	}
}
