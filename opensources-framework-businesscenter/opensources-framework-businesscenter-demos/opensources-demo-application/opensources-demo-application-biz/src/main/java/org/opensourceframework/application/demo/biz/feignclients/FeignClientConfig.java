package org.opensourceframework.application.demo.biz.feignclients;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * FeignService 配置
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
@EnableFeignClients(basePackages = {"org.opensourceframework.application.demo.biz.feignclients.feignservice"})
public class FeignClientConfig {
}
