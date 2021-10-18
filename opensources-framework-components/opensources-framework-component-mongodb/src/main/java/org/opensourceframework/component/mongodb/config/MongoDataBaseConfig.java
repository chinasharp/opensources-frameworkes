package org.opensourceframework.component.mongodb.config;

import java.io.Serializable;

/**
 * MongoDB配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2017/11/24
 */
public class MongoDataBaseConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4141295350425580293L;

	/**
	 * 是否需要权限访问 默认不需要
	 */
	protected Boolean needAuth = false;

	/**
	 * 服务器实际建立的连接数量<br>
	 * connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量
	 */
	protected Integer connectionsPerHost = 200;

	/**
	 * 每个连接上可以排队等待的线程数量<br>
	 * connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量, Default is 5.
	 */
	protected Integer threadsAllowedToBlockForConnectionMultiplier = 5;

	/**
	 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间<br>
	 * e.g:当前数据库的连接都在使用中，线程T10尝试访问数据库。此时T10会在某个connection上排队等待。<br>
	 * 如果超过maxWaitTime都没有获取到这个连接的话。该线程就会抛出Exception（此处为巨坑，一定要注意）。<br>
	 * 所以maxWaitTime一定要设置的足够大，以免由于排队线程过多造成的访问数据库失败的情况。 <br>
	 * Default is 120,000. A value of 0 means that it will not wait
	 */
	protected Integer maxWaitTime = 120000;

	/**
	 * 与数据库建立连接的timeout, Default is 10000毫秒
	 */
	protected Integer connectTimeout = 10000;

	/**
	 * 数据库连接读取和写入数据的timeout,0 means no timeout
	 */
	protected Integer socketTimeout = 0;

	protected Boolean sslEnabled = false;

	protected String user;
	protected String dataBase;
	protected String passWord;

	protected String serverAddresses;

	public MongoDataBaseConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MongoDataBaseConfig(String user, String dataBase, String passWord, String serverAddresses) {
		super();
		this.user = user;
		this.dataBase = dataBase;
		this.passWord = passWord;
		this.serverAddresses = serverAddresses;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * 服务器列表 比如:192.168.0.1:27017,192.168.0.2:27017
	 *
	 * @return
	 */
	public String getServerAddresses() {
		return serverAddresses;
	}

	/**
	 * 服务器列表 比如:192.168.0.1:27017,192.168.0.2:27017
	 * @return
	 */
	public void setServerAddresses(String serverAddresses) {
		this.serverAddresses = serverAddresses;
	}

	/**
	 * 
	 * 服务器实际建立的连接数量,default 200<br>
	 * connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量
	 *
	 * @return
	 */
	public Integer getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public void setConnectionsPerHost(Integer connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}

	/**
	 * 
	 * 每个连接上可以排队等待的线程数量<br>
	 *              connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量, Default is 5.
	 * @return
	 */
	public Integer getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}

	/**
	 * 每个连接上可以排队等待的线程数量<br>
	 *              connectionsPerHost * threadAllowedToBlockForConnectionMultiplier == 数据库允许的最大并发数量, Default is 5.
	 *
	 * @param threadsAllowedToBlockForConnectionMultiplier
	 */
	public void setThreadsAllowedToBlockForConnectionMultiplier(Integer threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}

	/**
	 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间 <br>
	 *              Default is 120,000. A value of 0 means that it will not wait
	 * @return
	 */
	public Integer getMaxWaitTime() {
		return maxWaitTime;
	}

	/**
	 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间 <br>
	 *              Default is 120,000. A value of 0 means that it will not wait
	 * @param maxWaitTime
	 */
	public void setMaxWaitTime(Integer maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	/**
	 * 与数据库建立连接的timeout, Default is 10,000
	 *
	 * @return
	 */
	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 与数据库建立连接的timeout, Default is 10,000
	 * @param connectTimeout
	 */
	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 数据库连接读取和写入数据的timeout,0 means no timeout.default is 0
	 *
	 * @return
	 */
	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * 数据库连接读取和写入数据的timeout,0 means no timeout.default is 0
	 *
	 * @param socketTimeout
	 */
	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Boolean getSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(Boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	public Boolean getNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(Boolean needAuth) {
		this.needAuth = needAuth;
	}
}
