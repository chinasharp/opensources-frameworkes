#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot.config;

import ${groupId}.common.interceptor.HttpReqInfoInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 底层配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
public class RootContextConfig implements WebMvcConfigurer {
	public HttpReqInfoInterceptor httpReqInterceptor(){
		return new HttpReqInfoInterceptor();
	}

	/**
	 * 请求参数输出
	 *
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration =  registry.addInterceptor(httpReqInterceptor());
	}
}
