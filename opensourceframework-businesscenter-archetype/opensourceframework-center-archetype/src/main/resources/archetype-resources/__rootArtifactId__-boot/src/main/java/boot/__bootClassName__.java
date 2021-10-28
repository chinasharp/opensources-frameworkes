#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.BaseBootApplication;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 中心启动类示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"org.opensourceframework"})
public class ${bootClassName} extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(${bootClassName}.class , args) {
			@Override
			public void execute() {
				OpensourceFrameworkSystem.init();
			}
		}.run();
	}
}
