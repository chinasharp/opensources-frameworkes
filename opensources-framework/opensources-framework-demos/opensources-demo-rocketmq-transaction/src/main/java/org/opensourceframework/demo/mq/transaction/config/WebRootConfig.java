package org.opensourceframework.demo.mq.transaction.config;

import org.opensourceframework.common.interceptor.HttpReqInfoInterceptor;
import org.springframework.context.annotation.Configuration;
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
	public HttpReqInfoInterceptor httpReqInterceptor(){
		return new HttpReqInfoInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration =  registry.addInterceptor(httpReqInterceptor());
	}
}
