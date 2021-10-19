package org.opensourceframework.component.seclog.config;

import org.opensourceframework.component.mq.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;

/**
 * 审计日志组件消息主题配置
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
public class SecLogTopicConfig extends TopicConfig {
    /**
     * 消息主题
     * @param topic
     */
    @Override
    @Value("${opensourceframework.mq.seclog.registryvo.topic:SEC_LOG_TOPIC}")
    public void setTopic(String topic) {
        super.setTopic(topic);
    }

    /**
     * 消息标识
     */
    @Value("${opensourceframework.mq.seclog.registryvo.tag:SEC_LOG_TAG}")
    private String tag;

    @Override
    @Value("${opensourceframework.mq.seclog.registryvo.enableDBConfig:false}")
    public void setEnableDBConfig(Boolean enableDBConfig) {
        super.setEnableDBConfig(enableDBConfig);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
