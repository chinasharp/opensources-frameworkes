package org.opensourceframework.component.es.config;

import java.io.Serializable;

/**
 * Es 连接配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午1:56
 */
public class ESConfig implements Serializable {
	protected String serverHosts;
	protected String scanPath;
	protected String userName;
	protected String passWord;

	public ESConfig() {
	}

	public ESConfig(String serverHosts, String userName, String passWord) {
		this.serverHosts = serverHosts;
		this.userName = userName;
		this.passWord = passWord;
	}

	public String getServerHosts() {
		return serverHosts;
	}

	public void setServerHosts(String serverHosts) {
		this.serverHosts = serverHosts;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getScanPath() {
		return scanPath;
	}

	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}
}
