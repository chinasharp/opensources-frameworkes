package org.opensourceframework.demo.mq.transaction;

import org.opensourceframework.commons.boot.AbstractBoot;
import org.opensourceframework.commons.boot.BaseBootApplication;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 中心启动类示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class MQTransactionDemoBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(MQTransactionDemoBoot.class , args) {
			@Override
			public void execute() {
				OpensourceFrameworkSystem.init();
			}
		}.run();
	}
}
