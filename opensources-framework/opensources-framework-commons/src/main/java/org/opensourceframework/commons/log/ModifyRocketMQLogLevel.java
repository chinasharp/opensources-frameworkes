package org.opensourceframework.commons.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 修改RocketMQ client 日志级别
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Component
public class ModifyRocketMQLogLevel implements ApplicationListener<ContextRefreshedEvent> {
    private static final String ROCKETMQ_CLIENT_LOGGER = "RocketmqClient";
    private static final String ROCKETMQ_REMOTING_LOGGER = "RocketmqRemoting";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(ROCKETMQ_CLIENT_LOGGER).setLevel(Level.ERROR);
        loggerContext.getLogger(ROCKETMQ_REMOTING_LOGGER).setLevel(Level.ERROR);
    }
}
