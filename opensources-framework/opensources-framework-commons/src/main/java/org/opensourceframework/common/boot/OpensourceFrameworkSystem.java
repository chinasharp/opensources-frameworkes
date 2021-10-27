package org.opensourceframework.common.boot;

import org.opensourceframework.base.microservice.ServiceContext;
import org.opensourceframework.base.helper.IpAddressHelper;
import org.opensourceframework.common.log.LogConfigSetting;
import org.opensourceframework.common.log.LoggerFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.List;

/**
 * 系统相关配置加载与设置
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/15 下午3:45
 */
public class OpensourceFrameworkSystem {
	private static final Logger logger = LoggerFactory.getLogger(OpensourceFrameworkSystem.class);

	public static void init(){
		// 配置dubbo支持logback
		System.setProperty("dubbo.application.logger" , "slf4j");

		// 配置RocketMQ支持logback
		System.setProperty("rocketmq.client.logUseSlf4j","true");
		System.setProperty("rocketmq.client.logLevel" , "ERROR");

		String ip = getIp();

		MDC.put("server.localHost", ip);
		System.setProperty("server.localHost", ip);

		MDC.put("opensourceframework.server.localHost", ip);
		System.setProperty("opensourceframework.server.localHost" , ip);

		MDC.put("opensourceframework.server.ip", ip);
		System.setProperty("server.ip", ip);

		String systemReqId = "system-startup";
		MDC.put(ServiceContext.REQ_REQUESTID , systemReqId);

		// 指定dubbo consumer服务注册的ip 与该应用的服务生产者的ip一致
		String serverIp = System.getProperty("server.host.ip");
		if(StringUtils.isNotBlank(serverIp)) {
			System.setProperty("DUBBO_IP_TO_REGISTRY", serverIp);
		}

		logger.info("server startup start. sever ip :{}" , ip);

		LogConfigSetting.initLogger();
	}

	public static String getIp(){
		List<String> ips = IpAddressHelper.resolveLocalIps();
		String ip = null;
		if (CollectionUtils.isEmpty(ips)) {
			ip = "127.0.0.1";
		}else{
			ip = ips.get(ips.size() - 1);
		}
		return ip;
	}
}
