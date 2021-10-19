package org.opensourceframework.starter.mq;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.opensourceframework.component.mq.api.impl.MessageSenderImpl;
import org.opensourceframework.component.mq.consumer.IRocketConsumer;
import org.opensourceframework.component.mq.consumer.RocketConsumer;
import org.opensourceframework.component.mq.consumer.SubscribeListener;
import org.opensourceframework.component.mq.helper.SpringHelper;
import org.opensourceframework.component.mq.producer.IRocketProducer;
import org.opensourceframework.component.mq.producer.RocketRocketProducer;
import org.opensourceframework.component.mq.producer.transaction.DefaultTransactionListener;
import org.opensourceframework.starter.mq.config.MqProperties;
import org.opensourceframework.starter.mq.config.TopicProperties;
import org.opensourceframework.starter.mq.listener.MessageProcessorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * MQ自动加载配置
 *
 * @author yu.ce
 * @since 1.0.0
 *
 */
@Configuration
@EnableConfigurationProperties({MqProperties.class, TopicProperties.class})
public class MqAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MqAutoConfiguration.class);

    static {
        // 配置RocketMQ支持logback
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        System.setProperty("rocketmq.client.logLevel" , "ERROR");
    }

    @Autowired
    private MqProperties mqProperties;
    @Autowired
    private IRocketProducer producer;
    @Autowired
    private IRocketConsumer consumer;
    @Value("${opensourceframework.mq.default.consumer:GID_COMMON_CONSUMER}")
    private String defaultConsumer;
    @Value("${spring.application.name:}")
    private String envModule;
    @Value("${spring.profiles.active:}")
    private String profileActive;

    @Autowired
    private TopicProperties topicProperties;

    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private ISubscribeConfig subscribeConfig;

    @Autowired
    private DefaultTransactionListener defaultTransactionListener;

    public MqAutoConfiguration() {
    }

    @Bean
    public MessageProcessorListener mqRegisteRunner() {
        return new MessageProcessorListener(this.subscribeListener(), subscribeConfig , this.mqProperties, this.topicProperties , environment , profileActive);
    }
    
    @Bean
    public IMessageSender messageSender() {
        return new MessageSenderImpl(this.producer,this.profileActive ,defaultTransactionListener, mqProperties);
    }

    @Bean
    public SubscribeListener subscribeListener(){
        return new SubscribeListener(this.consumer());
    }

    @Bean
    public IRocketProducer producer() {
        logger.info("producer mqProperties:{}", JSON.toJSONString(this.mqProperties));
        RocketRocketProducer rocketProducer = RocketRocketProducer.getInstance();
        rocketProducer.init(mqProperties);
        logger.info("mq producer created success.");
        return rocketProducer;
    }

    @Bean
    public IRocketConsumer consumer() {
        logger.info("consumer mqProperties:{}", JSON.toJSONString(this.mqProperties));
        RocketConsumer rocketConsumer = RocketConsumer.getInstance();
        rocketConsumer.init(this.mqProperties);
        logger.info("mq consumer created success.");
        return rocketConsumer;
    }

    @Bean("springHelper")
    public SpringHelper springHelper(){
        return new SpringHelper();
    }
}

