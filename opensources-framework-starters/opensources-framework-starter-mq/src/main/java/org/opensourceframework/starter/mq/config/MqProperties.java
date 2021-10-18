package org.opensourceframework.starter.mq.config;

import org.opensourceframework.component.mq.config.MqConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * MQ 连接配置
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.mq.config.registryvo")
public class MqProperties extends MqConfig {
    public MqProperties() {
    }
}
