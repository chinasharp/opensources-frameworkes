package org.opensourceframework.component.mongodb;

import org.opensourceframework.component.mongodb.config.MongoDataBaseConfig;
import com.mongodb.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB工厂类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public class MongoDateBaseFactory {
	private MongoDataBaseConfig config;
	private static MongoClient client;
	public static String DEFAULT_DATA_BASE;

	public MongoDateBaseFactory(MongoDataBaseConfig config) {
		this.config = config;
	}

	private void init() {
		// 构建server列表
		List<ServerAddress> serverList = new ArrayList<ServerAddress>();
		if (StringUtils.isNotBlank(config.getServerAddresses())) {
			String[] serverAddressArray = config.getServerAddresses().split(",");
			for (String serverAddress : serverAddressArray) {
				if (StringUtils.isBlank(serverAddress) || serverAddress.indexOf(":") < 0 || serverAddress.split(":").length != 2) {
					throw new MongoException("serverAddress is invalid for mongoDB connection.serverAddress:" + serverAddress);
				} else {
					ServerAddress server = new ServerAddress(serverAddress.split(":")[0], Integer.valueOf(serverAddress.split(":")[1]));
					serverList.add(server);
				}
			}
		} else {
			throw new MongoException("not found address registryvo for mongoDB connection!");
		}
		if (serverList.size() == 0) {
			throw new MongoException("not registryvo serverAddress for mongoDB connection!");
		}

		// 构建操作选项,默认参数满足大多数场景
		MongoClientOptions options = MongoClientOptions.builder().sslEnabled(config.getSslEnabled())
				.connectionsPerHost(config.getConnectionsPerHost())
				.threadsAllowedToBlockForConnectionMultiplier(config.getThreadsAllowedToBlockForConnectionMultiplier())
				.maxWaitTime(config.getMaxWaitTime()).connectTimeout(config.getConnectTimeout()).socketTimeout(config.getSocketTimeout()).build();

		if(config.getNeedAuth()){
			// 构建鉴权信息
			MongoCredential credential = MongoCredential.createScramSha1Credential(config.getUser(), config.getDataBase(), config.getPassWord().toCharArray());
			client = new MongoClient(serverList, credential, options);
		}else {
			client = new MongoClient(serverList, options);
		}
		DEFAULT_DATA_BASE = config.getDataBase();
	}

	public MongoClient createClient() {
		if (client == null) {
			init();
		}
		return client;
	}

	public MongoDataBaseConfig getConfig() {
		return config;
	}

	public void setConfig(MongoDataBaseConfig config) {
		this.config = config;
	}
}
