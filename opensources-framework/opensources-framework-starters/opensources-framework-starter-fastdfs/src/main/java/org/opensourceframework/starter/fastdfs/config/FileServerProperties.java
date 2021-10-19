package org.opensourceframework.starter.fastdfs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * xxl-job 属性
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.file.server.registryvo")
public class FileServerProperties extends FileServerConfig {
	public FileServerProperties() {

	}
}
