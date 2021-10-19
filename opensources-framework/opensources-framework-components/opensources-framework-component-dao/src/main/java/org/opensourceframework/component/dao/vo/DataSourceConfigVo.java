package org.opensourceframework.component.dao.vo;

import org.opensourceframework.base.constants.CommonCanstant;

import java.io.Serializable;

/**
 * 数据库连接配置vo
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class DataSourceConfigVo implements Serializable {
	protected String driverClassName = "com.mysql.jdbc.Driver";
	protected String jdbcUrl;
	protected String jdbcUserName;
	protected String jdbcUserPassword;
	/** 连接属性 */
	protected String connectionProperties;
	protected String filters;
	protected String validationQuery = "SELECT 1";
	protected Integer initialSize = 1;
	protected Integer maxActive = 5;
	protected Integer minIdle = 0;
	protected Integer maxWait = 60000;
	/**
	 * 是否是分布式数据库
	 */
	protected Boolean distributedFlag = false;

	/**
	 * 分布式数据库事务类型
	 */
	protected String transactionPolicy;

	/**
	 * 是否将实体类的驼峰属性自动转化为下划线
	 */
	protected String autoHumpCol = CommonCanstant.NO_STR;

	public DataSourceConfigVo() {
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUserName() {
		return jdbcUserName;
	}

	public void setJdbcUserName(String jdbcUserName) {
		this.jdbcUserName = jdbcUserName;
	}

	public String getJdbcUserPassword() {
		return jdbcUserPassword;
	}

	public void setJdbcUserPassword(String jdbcUserPassword) {
		this.jdbcUserPassword = jdbcUserPassword;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}

	public Boolean getDistributedFlag() {
		return distributedFlag;
	}

	public void setDistributedFlag(Boolean distributedFlag) {
		this.distributedFlag = distributedFlag;
	}

	public String getTransactionPolicy() {
		return transactionPolicy;
	}

	public void setTransactionPolicy(String transactionPolicy) {
		this.transactionPolicy = transactionPolicy;
	}

    public String getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

	public String getAutoHumpCol() {
		return autoHumpCol;
	}

	public void setAutoHumpCol(String autoHumpCol) {
		this.autoHumpCol = autoHumpCol;
	}

	@Override
	public String toString() {
		return "jdbcUrl:" + this.jdbcUrl + ", jdbcUserName:" + this.jdbcUserName + ", jdbcUserPassword:" + this.jdbcUserPassword;
	}
}
