package org.opensourceframework.demo.mybatis.config;

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
@MapperScan(basePackages = {"org.opensourceframework.user.mybatis.biz.dao.mapper"} )
public class MyBatisMapperConfig {
}
