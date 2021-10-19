package org.opensourceframework.gateway.demo.boot;

import org.opensourceframework.commons.boot.AbstractBoot;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/4 上午10:11
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class GateWayDamoBoot{
	public static void main(String[] args) throws Exception {
		new AbstractBoot(GateWayDamoBoot.class) {
			/**
			 * 可供Spring Boot启动时 增加业务逻辑和配置初始化等操作
			 *
			 * @throws Exception
			 */
			@Override
			public void execute() throws Exception {
				OpensourceFrameworkSystem.init();
			}
		}.run(args);
	}
}