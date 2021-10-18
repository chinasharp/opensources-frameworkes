package org.opensourceframework.component.mongodb.contant;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum UpdateOperationalEnum {
	/**
	 * 增加一个指定值<br>
	 * eg:<br>
	 * 对指定字段进行增量增加，当字段不存在时，则在该文档中添加字段并赋值 <br>
	 * doc.updateOne(Filters.eq("name", "张三"), new Document("$inc",new Document("age",10)));
	 */
	INC("inc"),

	/**
	 * $mul的用法与$inc的用法差不多，差别在于$mul为相乘，$inc为相加，若字段不存在，添加字段并赋值为0
	 */
	MUL("mul"),
	/**
	 *
	 * 修改document的字段名<br>
	 * doc.updateOne(Filters.eq("name", "张三"), new Document("$rename",new Document("phone","telPhone")));
	 */
	RENAME("rename"),

	/**
	 *
	 */
	SET_ON_INSERT("setOnInsert"),

	/**
	 * 只修改指定字段值，当字段不存在时，则在该文档中添加一个新的字段并赋值
	 *
	 * doc.updateOne(Filters.eq("age", 20), new Document("$set",new Document("sex",2222)));
	 */
	SET("set"),

	/**
	 *
	 */
	UN_SET("unset"),
	/**
	 *
	 */
	MIN("min"),
	/**
	 *
	 */
	MAX("max"),
	/**
	 *
	 */
	CURRENT_DATE("currentDate");

	private String keyword;

	UpdateOperationalEnum(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
