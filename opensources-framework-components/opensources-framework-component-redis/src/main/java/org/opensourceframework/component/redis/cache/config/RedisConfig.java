package org.opensourceframework.component.redis.cache.config;

import org.opensourceframework.component.redis.cache.contants.WorkModel;

import java.io.Serializable;

/**
 * redis连接配置
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RedisConfig implements Serializable {
	protected String host;
	protected String port;

	/**
	 * 连接密码
	 */
	protected String authPwd;

	protected int timeout = 60000;
	protected int maxIdle = 20;
	protected int maxTotal = 30;
	protected int maxWaitMillis = 3000;
	protected int livetime = 86370;

	protected int database = 0;

	protected String workModel = WorkModel.SINGLE.getName();
	protected String[] addresses;

	protected boolean ssl = false;

	/* ------- BoolFilter Config Start ------ */
	/**
	 * 容量
	 */
	protected Long expectedInsertions = 1000000L;
	/**
	 * 误差
 	 */
	protected Double falseProbability = 0.001;
	/* ------- BoolFilter Config End   ------- */

	public RedisConfig() {
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAuthPwd() {
		return authPwd;
	}

	public void setAuthPwd(String authPwd) {
		this.authPwd = authPwd;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public int getLivetime() {
		return livetime;
	}

	public void setLivetime(int livetime) {
		this.livetime = livetime;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public String getWorkModel() {
		return workModel;
	}

	public void setWorkModel(String workModel) {
		this.workModel = workModel;
	}

	public String[] getAddresses() {
		return addresses;
	}

	public void setAddresses(String[] addresses) {
		this.addresses = addresses;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Long getExpectedInsertions() {
		return expectedInsertions;
	}

	public void setExpectedInsertions(Long expectedInsertions) {
		this.expectedInsertions = expectedInsertions;
	}

	public Double getFalseProbability() {
		return falseProbability;
	}

	public void setFalseProbability(Double falseProbability) {
		this.falseProbability = falseProbability;
	}
}
