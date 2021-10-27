package org.opensourceframework.demo.httpclient;

import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.BaseBootApplication;
import org.opensourceframework.component.httpclient.config.HttpClientConfig;
import org.opensourceframework.component.httpclient.service.HttpService;

/**
 * httpclient demo类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class HttpClientDemoBoot extends BaseBootApplication {
    public static void main(String[] args) throws Exception {
        new AbstractBoot(HttpClientDemoBoot.class) {
            @Override
            public void execute() {
                OpensourceFrameworkSystem.init();
            }
        }.run(args);
    }

    @Autowired
    private HttpClientConfig httpClientConfig;

    @Bean
    public HttpClientConfig httpClientConfig() {
        return new HttpClientConfig();
    }

    @Bean
    public HttpService httpService() {
        return new HttpService(httpClientConfig);
    }
}
