package org.opensourceframework.starter.kafka.config;

import org.opensourceframework.component.kafka.config.KafkaConfig;
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
@ConfigurationProperties(prefix = "opensourceframework.kafka.config.registryvo")
public class KafkaProperties extends KafkaConfig {
    public KafkaProperties() {
    }
}
