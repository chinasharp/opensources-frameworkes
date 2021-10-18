package org.opensourceframework.component.seclog.config;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.opensourceframework.component.mq.api.impl.MessageSenderImpl;
import org.opensourceframework.component.mq.consumer.IRocketConsumer;
import org.opensourceframework.component.mq.consumer.RocketConsumer;
import org.opensourceframework.component.mq.producer.IRocketProducer;
import org.opensourceframework.component.mq.producer.RocketRocketProducer;
import org.opensourceframework.component.seclog.aop.SecLogAspect;
import org.opensourceframework.component.seclog.event.SecLogListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 审计日志入口配置
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Slf4j
@EnableAsync
public class SecLogConfig {

    @Autowired
    private SecLogMqConfig secLogMqConfig;

    @Autowired
    private SecLogTopicConfig secLogTopicConfig;

    @Autowired
    private IRocketProducer producer;
    @Autowired
    private IRocketConsumer consumer;

    /**
     * 审计日志切面注入Spring
     */
    @Bean
    public SecLogAspect secLogAspect() {
        return new SecLogAspect();
    }

    /**
     * 将secLogListener事件监听注入Spring
     */
    @Bean
    public SecLogListener secLogListener() {
        return new SecLogListener();
    }

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
        return new MessageSenderImpl(this.producer,secLogMqConfig.profileActive , null ,secLogMqConfig);
    }

    @Bean
    public IRocketProducer producer() {
        log.info("producer mqProperties:{}", JSON.toJSONString(secLogMqConfig));
        RocketRocketProducer rocketProducer = RocketRocketProducer.getInstance();
        rocketProducer.init(secLogMqConfig);
        log.info("mq producer created success.");
        return rocketProducer;
    }

    @Bean
    public IRocketConsumer consumer() {
        log.info("consumer mqProperties:{}", JSON.toJSONString(secLogMqConfig));
        RocketConsumer rocketConsumer = RocketConsumer.getInstance();
        rocketConsumer.init(secLogMqConfig);
        log.info("mq consumer created success.");
        return rocketConsumer;
    }

    /**
     * 异步线程池配置
     *
     * @return
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(30);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
