package org.opensourceframework.starter.seclog;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import org.opensourceframework.component.seclog.aop.SecLogAspect;
import org.opensourceframework.component.seclog.event.SecLogListener;

import lombok.extern.slf4j.Slf4j;

/**
 * 审计日志自动化配置
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableAsync
public class SecLogAutoConfiguration{

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
