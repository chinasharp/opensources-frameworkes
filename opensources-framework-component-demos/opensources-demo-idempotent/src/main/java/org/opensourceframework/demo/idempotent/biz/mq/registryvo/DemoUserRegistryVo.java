package org.opensourceframework.demo.idempotent.biz.mq.registryvo;

import org.opensourceframework.starter.mq.config.SubscribeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * DemoUser消息订阅关系配置
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
@ConfigurationProperties(prefix = "mq.demouser.subscribe.registryvo")
public class DemoUserRegistryVo extends SubscribeProperties {
	public DemoUserRegistryVo() {
		super();
	}
}
