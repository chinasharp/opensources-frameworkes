#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.feignclients;

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
@EnableFeignClients(basePackages = {"${package}.biz.feignclients.feignservice"})
public class FeignClientConfig {
}
