package org.opensourceframework.starter.fastdfs.util;


/**
 * 文件类型枚取
 */
public enum FileType {

	/** JEPG. */
	JPEG("FFD8FF", "JPEG"),
	/** JPG. */
	JPG("FFD8FF", "JPG"),
	/** PNG. */
	PNG("89504E47", "PNG"),
	/** GIF. */
	GIF("47494638", "gif"),
	/** XML. */
	XML("3C3F786D6C", "XML"),
	/** HTML. */
	HTML("68746D6C3E", "HTML"),
	;

	public static FileType find(String code) {
		for (FileType frs : FileType.values()) {
			if (frs.getCode().startsWith(code)) {
				return frs;
			}
		}
		return null;
	}

	/** 状态码 **/
	private final String code;
	/** 状态描述 **/
	private final String description;

	/**
	 * 私有构造方法
	 * 
	 * @param code
	 *            编码
	 * @param description
	 *            描述
	 **/
    FileType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
