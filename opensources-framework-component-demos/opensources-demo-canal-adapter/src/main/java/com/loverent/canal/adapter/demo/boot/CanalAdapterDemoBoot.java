package org.opensourceframework.canal.adapter.demo.boot;

import org.opensourceframework.boot.AbstractBoot;
import org.opensourceframework.boot.BaseBootApplication;
import org.opensourceframework.boot.opensourceframeworkSystem;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动入口
 *
 * @author yuce
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class CanalAdapterDemoBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(CanalAdapterDemoBoot.class, Banner.Mode.OFF, args) {
			@Override
			public void execute() {
				opensourceframeworkSystem.init();
			}
		}.run();
	}
}
