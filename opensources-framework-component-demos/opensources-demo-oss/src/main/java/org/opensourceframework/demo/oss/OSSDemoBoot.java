package org.opensourceframework.demo.oss;

import org.opensourceframework.commons.boot.AbstractBoot;
import org.opensourceframework.commons.boot.BaseBootApplication;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class OSSDemoBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(OSSDemoBoot.class) {
			@Override
			public void execute() {
				OpensourceFrameworkSystem.init();
			}
		}.run(args);
	}
}
