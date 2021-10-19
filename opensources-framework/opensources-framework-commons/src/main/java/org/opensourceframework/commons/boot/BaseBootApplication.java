package org.opensourceframework.commons.boot;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.opensourceframework.base.app.AppInfo;
import org.opensourceframework.commons.filter.LoggerServletFilter;
import org.opensourceframework.commons.filter.RequestWrapperFilter;
import org.opensourceframework.commons.filter.SkyWalkingRequestFilter;
import org.opensourceframework.commons.http.JsLongFastJsonHttpMessageConverter;
import org.opensourceframework.commons.interceptor.RestInterceptor;
import org.opensourceframework.commons.spring.SpringBeanHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SpringBoot 启动基类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseBootApplication implements WebMvcConfigurer {
    private final List<RestInterceptor> interceptors = new ArrayList();

    @Value("${spring.application.name}")
    private String envModule;

    @Value("${opensourceframework.response.enable-null-field:true}")
    private final Boolean enableNullResponse = true;

    public BaseBootApplication() {
    }

    /**
     * 替换默认的MessageConvert为FastJsonMessgaeConvert
     * 解决Long长度过长 精度丢失的问题
     *
     * @return
     */
    private FastJsonHttpMessageConverter customConverters() {
        FastJsonHttpMessageConverter fastJsonMsgConvert = new JsLongFastJsonHttpMessageConverter();
        Charset UTF8 = StandardCharsets.UTF_8;
        fastJsonMsgConvert.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", UTF8), new MediaType("application", "*+json",
				UTF8)));
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        if (this.enableNullResponse) {
            fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
        } else {
            fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        }

        fastJsonMsgConvert.setFastJsonConfig(fastJsonConfig);
        return fastJsonMsgConvert;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // TODO Auto-generated method stub
        converters.add(0,this.customConverters());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        matcher.setUseRegisteredSuffixPatternMatch(true);
    }

    /**
     * 设置拦截与不拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        this.interceptors.forEach((item) -> {
            InterceptorRegistration res = registry.addInterceptor(item);
            if (item.includePath().length > 0) {
                res.addPathPatterns(item.includePath());
            }

            if (item.excludePath().length > 0) {
                res.excludePathPatterns(item.excludePath());
            }
        });
    }

    /**
     * 设置允许跨域访问
     */
    @Bean
    @ConditionalOnMissingBean(name = {"corsFilter"})
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean(name = {"methodValidationPostProcessor"})
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }


    @Bean
    @ConditionalOnMissingBean(name = {"requestWrapperFilter"})
    public RequestWrapperFilter requestWrapperFilter() {
        return new RequestWrapperFilter();
    }


    @Bean
    @ConditionalOnMissingBean(name = {"skyWalkingRequestFilter"})
    public SkyWalkingRequestFilter skyWalkingRequestFilter() {
        return new SkyWalkingRequestFilter();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"loggerServletFilter"})
    public LoggerServletFilter loggerServletFilter() {
        return new LoggerServletFilter();
    }

    @Bean
    @Primary
    public SpringBeanHelper springBeanHelper() {
        return new SpringBeanHelper();
    }

    @Bean
    public AppInfo envAppvo() {
        System.setProperty("spring.application.name", this.envModule);
        return new AppInfo();
    }
}
