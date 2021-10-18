package org.opensourceframework.starter.mybatis.config;

import org.opensourceframework.component.dao.vo.DataSourceConfigVo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 数据库链接配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.mybatis.registryvo")
public class DataSourceProperties extends DataSourceConfigVo {
	public DataSourceProperties() {
	}
}
