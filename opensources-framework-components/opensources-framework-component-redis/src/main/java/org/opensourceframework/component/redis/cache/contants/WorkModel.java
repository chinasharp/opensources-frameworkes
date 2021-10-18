package org.opensourceframework.component.redis.cache.contants;

/**
 * redis部署模式
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum WorkModel {
	/**
	 * 单机模式
	 */
	SINGLE("single"),
	/**
	 * 集群模式
	 */
	CLUSTER("cluster"),
	/**
	 * 切片模式
	 */
	SHARDING("sharding"),

	/**
	 * 哨兵模式
	 */
	SENTINE("sentine");

	private final String name;

	WorkModel(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
