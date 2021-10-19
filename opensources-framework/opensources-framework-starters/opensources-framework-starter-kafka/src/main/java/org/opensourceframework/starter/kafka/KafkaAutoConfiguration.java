package org.opensourceframework.starter.kafka;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.component.kafka.api.IMessageSender;
import org.opensourceframework.component.kafka.api.impl.MessageSenderImpl;
import org.opensourceframework.component.kafka.consumer.IKafkaConsumer;
import org.opensourceframework.component.kafka.consumer.KafkaConsumer;
import org.opensourceframework.component.kafka.consumer.SubscribeListener;
import org.opensourceframework.component.kafka.helper.KafkaSpringHelper;
import org.opensourceframework.component.kafka.producer.IKafkaProducer;
import org.opensourceframework.component.kafka.producer.KafkaProducer;
import org.opensourceframework.starter.kafka.config.KafkaProperties;
import org.opensourceframework.starter.kafka.listener.MessageProcessorListener;
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
@EnableConfigurationProperties({KafkaProperties.class})
public class KafkaAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAutoConfiguration.class);

    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private IKafkaProducer kafkaProducer;
    @Autowired
    private IKafkaConsumer kafkaConsumer;
    @Value("${spring.application.name:}")
    private String envModule;
    @Value("${spring.profiles.active:}")
    private String profileActive;

    @Autowired
    private Environment environment;


    public KafkaAutoConfiguration() {
    }

    @Bean
    public IMessageSender messageSender() {
        return new MessageSenderImpl(this.kafkaProducer,this.profileActive ,kafkaProperties);
    }

    @Bean
    public MessageProcessorListener mqRegisteRunner() {
        return new MessageProcessorListener(subscribeListener(),  this.kafkaProperties, environment , profileActive);
    }

    @Bean
    public SubscribeListener subscribeListener(){
        return new SubscribeListener(this.kafkaConsumer);
    }

    @Bean
    public IKafkaProducer kafkaProducer() {
        logger.info("producer mqProperties:{}", JSON.toJSONString(this.kafkaProperties));
        KafkaProducer kafkaProducer = KafkaProducer.getInstance();
        kafkaProducer.init(kafkaProperties);
        logger.info("kafka producer created success.");
        return kafkaProducer;
    }

    @Bean
    public IKafkaConsumer kafkaConsumer() {
        logger.info("consumer mqProperties:{}", JSON.toJSONString(this.kafkaProperties));
        KafkaConsumer kafkaConsumer = KafkaConsumer.getInstance();
        kafkaConsumer.init(this.kafkaProperties);
        logger.info("mq consumer created success.");
        return kafkaConsumer;
    }

    @Bean("kafkaSpringHelper")
    public KafkaSpringHelper kafkaSpringHelper(){
        return new KafkaSpringHelper();
    }
}

