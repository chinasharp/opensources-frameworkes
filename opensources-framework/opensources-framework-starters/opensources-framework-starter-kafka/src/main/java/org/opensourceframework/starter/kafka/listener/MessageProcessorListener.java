
package org.opensourceframework.starter.kafka.listener;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.component.kafka.annotation.KafkaSubscribe;
import org.opensourceframework.component.kafka.api.IKafkaMessageProcessor;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.constant.KafkaConstants;
import org.opensourceframework.component.kafka.consumer.SubscribeListener;
import org.opensourceframework.component.kafka.helper.KafkaConfigHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将需要订阅的consumer注册 包含@MQSubscribe标识的Messageprocessor
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MessageProcessorListener implements BeanPostProcessor, CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessorListener.class);
    private static final Pattern PATTERN = Pattern.compile("\\$\\{.+\\}");

    private final KafkaConfig kafkaConfig;
    private final SubscribeListener subscribeListener;
    private final Environment environment;
    private final String profileActive;

    public MessageProcessorListener(SubscribeListener subscribeListener, KafkaConfig kafkaConfig,
                                    Environment environment , String profileActive) {
        this.subscribeListener = subscribeListener;
        this.kafkaConfig = kafkaConfig;
        this.environment = environment;
        this.profileActive = profileActive;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IKafkaMessageProcessor) {
            KafkaSubscribe kafkaSubscribe = bean.getClass().getAnnotation(KafkaSubscribe.class);
            if (null == kafkaSubscribe) {
                return bean;
            }

            String topic = kafkaSubscribe.topic();
            String consumer = kafkaSubscribe.consumer();
            String pattern = kafkaSubscribe.pattern();

            //处理环境变量${}
            topic = handleEnv(topic).toUpperCase();
            consumer = handleEnv(consumer);

            //处理consumer命名 为不是CID开头的加上CID
            consumer = KafkaConfigHelper.parseConsumer(this.kafkaConfig, consumer, KafkaConstants.DEF_CONSUMER_ID);

            KafkaConfigHelper.addSubscribeRelation(topic, pattern, consumer, beanName);
        }

        return bean;
    }

    private String handleEnv(String propertyVal){
        Matcher matcher = PATTERN.matcher(propertyVal);
        if (matcher.matches()) {
            propertyVal = matcher.group(0).substring(2,propertyVal.length() - 1);
            propertyVal = environment.getProperty(propertyVal , propertyVal);
        }

        if(StringUtils.isNotBlank(profileActive)){
            propertyVal = propertyVal.concat("_").concat(profileActive);
        }

        return propertyVal;
    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }

    private void start() {
        if (!KafkaConfigHelper.subscribeRelationMap.keySet().isEmpty()) {
            Iterator iterator = KafkaConfigHelper.subscribeRelationMap.keySet().iterator();

            while(iterator.hasNext()) {
                String consumer = (String)iterator.next();

                logger.info("Kafka Consumer listener start. subscribeRelation:{}", JSON.toJSONString(KafkaConfigHelper.subscribeRelationMap.get(consumer)));
                this.subscribeListener.start(consumer);
            }
        }
    }

}
