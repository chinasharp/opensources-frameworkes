package org.opensourceframework.starter.seclog;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.opensourceframework.component.mq.api.impl.MessageSenderImpl;
import org.opensourceframework.component.mq.consumer.IRocketConsumer;
import org.opensourceframework.component.mq.consumer.RocketConsumer;
import org.opensourceframework.component.mq.producer.IRocketProducer;
import org.opensourceframework.component.mq.producer.RocketRocketProducer;
import org.opensourceframework.component.seclog.config.SecLogMqConfig;
import org.opensourceframework.component.seclog.config.SecLogTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class SecLogMqAutoConfiguration {
    @Autowired
    private SecLogMqConfig secLogMqConfig;

    @Autowired
    private SecLogTopicConfig secLogTopicConfig;
    @Autowired
    private IRocketProducer producer;
    @Autowired
    private IRocketConsumer consumer;

    @Bean
    public SecLogMqConfig secLogMqConfig() {
        return new SecLogMqConfig();
    }

    @Bean
    public SecLogTopicConfig secLogTopicConfig() {
        return new SecLogTopicConfig();
    }

    @Bean
    public IMessageSender messageSender() {
        return new MessageSenderImpl(this.producer , secLogMqConfig.profileActive ,null , secLogMqConfig);
    }

    @Bean
    public IRocketProducer producer() {
        log.info("producer mqProperties:{}", JSON.toJSONString(this.secLogMqConfig));
        RocketRocketProducer rocketProducer = RocketRocketProducer.getInstance();
        rocketProducer.init(this.secLogMqConfig);
        log.info("mq producer created success.");
        return rocketProducer;
    }

    @Bean
    public IRocketConsumer consumer() {
        log.info("consumer mqProperties:{}", JSON.toJSONString(this.secLogMqConfig));
        RocketConsumer rocketConsumer = RocketConsumer.getInstance();
        rocketConsumer.init(this.secLogMqConfig);
        log.info("mq consumer created success.");
        return rocketConsumer;
    }

}
