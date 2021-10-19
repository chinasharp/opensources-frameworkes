package org.opensourceframework.demo.cache;

import org.opensourceframework.commons.boot.AbstractBoot;
import org.opensourceframework.commons.boot.BaseBootApplication;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Ehcache Demo Boot 启动类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/15 下午3:32
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class CacheDemoBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(CacheDemoBoot.class) {
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
