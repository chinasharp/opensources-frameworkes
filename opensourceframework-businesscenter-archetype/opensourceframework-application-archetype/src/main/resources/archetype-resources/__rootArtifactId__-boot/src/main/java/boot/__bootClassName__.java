#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot;

import ${groupId}.framework.boot.AbstractBoot;
import ${groupId}.framework.boot.BaseBootApplication;
import ${groupId}.framework.boot.opensourceframeworkSystem;
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
@ComponentScan({"${groupId}"})
public class ApplicationBoot extends BaseBootApplication {
	public static void main(String[] args) throws Exception {
		new AbstractBoot(ApplicationBoot.class , args) {
			@Override
			public void execute() {
				opensourceframeworkSystem.init();
			}
		}.run();
	}
}
