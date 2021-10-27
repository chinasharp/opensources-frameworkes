package org.opensourceframework.demo.netty.rpc;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.BaseBootApplication;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Netty Client Demo Start
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/25 下午6:01
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class NettyRpcConsumerBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(NettyRpcConsumerBoot.class, Banner.Mode.CONSOLE, args) {
			@Override
			public void execute() {
				OpensourceFrameworkSystem.init();
			}
		}.run();
	}
}
