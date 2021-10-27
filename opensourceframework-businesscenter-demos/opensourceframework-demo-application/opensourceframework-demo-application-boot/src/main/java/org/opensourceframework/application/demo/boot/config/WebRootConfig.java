package org.opensourceframework.application.demo.boot.config;

import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import org.opensourceframework.common.interceptor.HttpReqInfoInterceptor;
import org.opensourceframework.starter.restclient.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 请求处理类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
public class WebRootConfig implements WebMvcConfigurer {
	@Autowired
	private RestClient restClient;

	@Bean
	public HttpReqInfoInterceptor httpReqInterceptor(){
		return new HttpReqInfoInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration =  registry.addInterceptor(httpReqInterceptor());
	}

	@Bean
	@LoadBalanced
	@DubboTransported
	public RestTemplate restTemplate(){
		return this.restClient.restTemplate();
	}
}
