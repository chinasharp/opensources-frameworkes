#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.mq.registryvo;

import ${groupId}.starter.mq.config.SubscribeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 消息订阅关系配置示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
@ConfigurationProperties(prefix = "mq.demouser.subscribe.registryvo")
public class DemoRegistryVo extends SubscribeProperties {
	public DemoRegistryVo() {
		super();
	}
}
