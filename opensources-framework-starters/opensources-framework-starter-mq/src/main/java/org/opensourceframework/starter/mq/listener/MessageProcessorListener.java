
package org.opensourceframework.starter.mq.listener;

import org.opensourceframework.component.mq.annotation.MQSubscribe;
import org.opensourceframework.component.mq.api.IMessageProcessor;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.config.TopicConfig;
import org.opensourceframework.component.mq.constant.MQConstant;
import org.opensourceframework.component.mq.consumer.SubscribeListener;
import org.opensourceframework.component.mq.helper.SubscribeRelationHelper;
import org.opensourceframework.starter.mq.ISubscribeConfig;
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

    private final TopicConfig topicConfig;
    private final MqConfig mqConfig;
    private final ISubscribeConfig subscribeConfig;
    private final SubscribeListener subscribeListener;
    private final Environment environment;
    private final String profileActive;

    public MessageProcessorListener(SubscribeListener subscribeListener, ISubscribeConfig subscribeConfig ,
                                    MqConfig mqConfig, TopicConfig topicConfig, Environment environment , String profileActive) {
        this.subscribeListener = subscribeListener;
        this.subscribeConfig = subscribeConfig;
        this.mqConfig = mqConfig;
        this.topicConfig = topicConfig;
        this.environment = environment;
        this.profileActive = profileActive;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IMessageProcessor) {
            MQSubscribe mqSubscribe = bean.getClass().getAnnotation(MQSubscribe.class);
            if (null == mqSubscribe) {
                return bean;
            }

            String topic = mqSubscribe.topic();
            String consumer = mqSubscribe.consumer();
            String tag = mqSubscribe.tag();
            int messageType = mqSubscribe.messageType();

            //处理环境变量${}
            topic = handleEnv(topic).toUpperCase();
            tag = handleEnv(tag).toUpperCase();
            consumer = handleEnv(consumer);

            //处理consumer命名 为不是CID开头的加上CID
            consumer = SubscribeRelationHelper.parseConsumer(this.mqConfig, consumer, MQConstant.DEF_CONSUMER_ID);

            SubscribeRelationHelper.addSubscribeRelation(topic, tag, consumer, beanName , messageType);
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
        if(this.subscribeConfig != null) {
            if (topicConfig.getEnableDBConfig()) {
                logger.info("load message subscribe registryvo from DB start.");
                subscribeConfig.loadSubscribeConfig();
                logger.info("load message subscribe registryvo from DB end.");
            }
        }

        this.start();
    }

    private void start() {
        if (!SubscribeRelationHelper.subscribeRelationMap.keySet().isEmpty()) {
            Iterator iterator = SubscribeRelationHelper.subscribeRelationMap.keySet().iterator();

            while(iterator.hasNext()) {
                String consumer = (String)iterator.next();

                logger.info("开始监听 consumer:{}, processors:{}", consumer, SubscribeRelationHelper.subscribeRelationMap.get(consumer));
                this.subscribeListener.start(consumer);
            }
        }
    }

}
