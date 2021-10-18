#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis Mapper文件配置加载类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
@MapperScan(basePackages = {"${package}.biz.dao.org.opensourceframework.user.bloomfilter.mybatis.mapper"} )
public class CenterDataMapperConfig {
}
