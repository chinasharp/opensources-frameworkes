package org.opensourceframework.demo.sentinel;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/7 上午1:46
 */
public class SentinelDemoBoot {
	public static void main(String[] args) {
		AbstractBoot abstractBoot = new AbstractBoot(SentinelDemoBoot.class) {
			/**
			 * 可供Spring Boot启动时 增加业务逻辑和配置初始化等操作
			 *
			 * @throws Exception
			 */
			@Override
			public void execute() throws Exception {
				OpensourceFrameworkSystem.init();
			}
		};
	}
}
