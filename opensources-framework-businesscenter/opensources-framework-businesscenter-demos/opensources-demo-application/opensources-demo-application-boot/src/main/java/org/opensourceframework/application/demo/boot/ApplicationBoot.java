package org.opensourceframework.application.demo.boot;

import org.opensourceframework.commons.boot.AbstractBoot;
import org.opensourceframework.commons.boot.BaseBootApplication;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"org.opensourceframework"})
public class ApplicationBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(ApplicationBoot.class , args) {
			@Override
			public void execute() {
				OpensourceFrameworkSystem.init();
			}
		}.run();
	}
}
