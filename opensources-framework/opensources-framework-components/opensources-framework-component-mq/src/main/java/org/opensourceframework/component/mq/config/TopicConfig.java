package org.opensourceframework.component.mq.config;

import org.opensourceframework.component.mq.constant.MQConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ConfigurationProperties(prefix = "opensourceframework.mq.topic.registryvo")
public class TopicConfig {
    private String topic = MQConstant.DEF_TOPIC;

    /**
     * 是否读取数据配置的消息订阅配置 默认true
     */
    private Boolean enableDBConfig = true;

    public TopicConfig() {
    }

    public TopicConfig(String topic, Boolean enableDBConfig) {
        this.topic = topic;
        this.enableDBConfig = enableDBConfig;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public Boolean getEnableDBConfig() {
        return enableDBConfig;
    }

    public void setEnableDBConfig(Boolean enableDBConfig) {
        this.enableDBConfig = enableDBConfig;
    }
}
