package org.opensourceframework.component.mongodb.contant;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum QueryOperationalEnum {
	/**
	 * 等值条件 eg:find({"name":{$eq:"steven"}})
	 */
	EQ("eq"),

	/**
	 * 大于 eg:find({"age":{$gt:19}})
	 */
	GT("gt"),

	/**
	 * 大于等于
	 */
	GTE("gte"),
	/**
	 * 小于
	 */
	LT("lt"),
	/**
	 * 小于等于
	 */
	LTE("lte"),
	/**
	 * 不等于
	 */
	NE("ne"),

	/**
	 * 在...范围 eg：find({"name":{$in:["steven","jack"]}})
	 */
	IN("in"),
	/**
	 * 不在...范围
	 */
	NIN("nin"),

	/**
	 * eg: find({"$or" : [{"name":"steven"},{"age":20}]})
	 */
	OR("or"),

	/**
	 * find({"$and" : [{"name":"steven"},{"age":20}]})
	 */
	AND("and"),

	/**
	 * eg:age 大于等于20<br>
	 * find({"age":{"$not":{"$lt":20}}})
	 */
	NOT("not"),

	/**
	 * eg:name=steven 或者 age=20 的全部过滤掉<br>
	 * find({"$nor" : [{"name":"steven"},{"age":20}]})
	 */
	NOR("nor"),

	/**
	 * eg:<br>
	 * find({"name":{"$exists":true}}) 包含name这个key的文档全部返回<br>
	 * find({"name":{"$exists":false}}) 不包含name这个key的文档全部返回<br>
	 */
	EXISTS("exists"),

	/**
	 * eg：返回所有name字段为String类型的所有文档<br>
	 * find({"name":{"$type":2}})
	 */
	TYPE("type"),

	/**
	 * eg:返回age的值和 4 求余后 结果为 0 的数据<br>
	 * find({"age" : {"$mod" : [4,0]}})
	 */
	MOD("mod"),

	/**
	 * mogonDB专用 eg:<br>
	 * 1、find({name : { $regex: /^ABC/i })<br>
	 * 返回 name 符合指定正则的数据，ABC开头不区分大小写<br>
	 * 结果：<br>
	 * { "_id" : 100, "name" : "abc123", "description" : "Single line description." } <br>
	 * { "_id" : 101, "name" : "abc789", "description" : "First line\nSecond line" } <br>
	 * 2、find( { name: { $regex: /789$/ } } )<br>
	 * 789结尾的name<br>
	 * { "_id" : 101, "name" : "abc789", "description" : "First line\nSecond line" } <br>
	 * 相当于 WHERE sku like "%789"<br>
	 *
	 */
	REGEX("regex"),

	/**
	 * 包含
	 */
	CONTAINS("contains"),

	/**
	 * 以..开头
	 */
	STARTS_WITH("startsWith"),

	/**
	 * 以..结尾
	 */
	ENDS_WITH("endsWith"),

	/**
	 * ES专用<br>
	 * es中使用原生的es 查询表达式
	 */
	EXPRESSION("expression"),
	/**
	 *
	 */
	TEXT("text"),
	/**
	 *
	 */
	WHERE("where"),
	/**
	 *
	 */
	ALL("all"),
	/**
	 *
	 */
	ELEMMATCH("elemMatch"),
	/**
	 *
	 */
	SIZE("size"),
	/**
	 *
	 */
	COMMENT("comment"),
	/**
	 *
	 */
	GEOWITHIN("geoWithin"),
	/**
	 *
	 */
	GEOINTERSECTS("geoIntersects"),
	/**
	 *
	 */
	NEAR("near"),
	/**
	 *
	 */
	NEARSPHERE("nearSphere"),
	/**
	 *
	 */
	META("meta"),
	/**
	 * 数组类型字段的投影操作，返回原来数据的一个子集.针对一个数组，其有如下几种返回子集的方式<br>
	 * eg:<br>
	 * 返回博客的前10条评论 find({"comments":{"$slice":10}}) <br>
	 * 返回博客的后10条评论 find({"comments":{"$slice":-10}}) <br>
	 * 返回博客跳过前10条，然后返回第11 ~ 15条 find({"comments":{"$slice":[10,5]}})
	 */
	SLICE("slice");

	private String keyword;

	QueryOperationalEnum(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
