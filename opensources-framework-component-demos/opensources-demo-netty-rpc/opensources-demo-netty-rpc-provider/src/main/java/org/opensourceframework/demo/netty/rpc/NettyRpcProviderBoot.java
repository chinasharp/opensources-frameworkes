package org.opensourceframework.demo.netty.rpc;

import org.opensourceframework.commons.boot.AbstractBoot;
import org.opensourceframework.commons.boot.BaseBootApplication;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.opensourceframework.netty.rpc.core.handler.ProviderHandler;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Netty Server Demo Boot start
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/25 下午6:01
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class NettyRpcProviderBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception{
		new AbstractBoot(NettyRpcProviderBoot.class, Banner.Mode.CONSOLE, args) {
			@Override
			public void execute() {
				OpensourceFrameworkSystem.init();
			}
		}.run();
	}

	@Bean
	public ProviderHandler providerHandler(){
		return ProviderHandler.instance();
	}
}
