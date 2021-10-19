package org.opensourceframework.starter.es.config;

import org.opensourceframework.component.es.config.ESConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Configuration properties for Elasticsearch.
 * @author yu.ce@foxmail.com
 * @date Created in 9:39 2020/5/19
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.es.registryvo")
public class ESConfigProperties extends ESConfig {

}
