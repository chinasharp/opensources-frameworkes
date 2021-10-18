package org.opensourceframework.starter.mq.config;

import org.opensourceframework.component.mq.config.TopicConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * topic公共配置
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.mq.topic.registryvo")
public class TopicProperties extends TopicConfig {
    public TopicProperties() {
    }
}
