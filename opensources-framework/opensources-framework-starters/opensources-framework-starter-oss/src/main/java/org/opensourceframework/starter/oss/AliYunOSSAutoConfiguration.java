package org.opensourceframework.starter.oss;

import org.opensourceframework.starter.oss.config.OssConfig;
import org.opensourceframework.starter.oss.service.AliOSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * AliYun Oss 自动装配类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/15 下午4:11
 */
@Configuration
@EnableConfigurationProperties({OssConfig.class})
public class AliYunOSSAutoConfiguration {
	@Autowired
	private Environment environment;
	@Autowired
	private OssConfig ossConfig;

	@Bean
	public AliOSSService aliOOSService(OssConfig ossConfig){
		return new AliOSSService(ossConfig);
	}
}
