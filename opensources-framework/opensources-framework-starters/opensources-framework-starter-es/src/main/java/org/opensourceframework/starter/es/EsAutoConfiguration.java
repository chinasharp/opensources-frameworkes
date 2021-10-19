package org.opensourceframework.starter.es;

import org.opensourceframework.component.es.base.ESConnection;
import org.opensourceframework.component.es.base.ESTemplate;
import org.opensourceframework.component.es.config.ESConfig;
import org.opensourceframework.starter.es.config.ESConfigProperties;
import org.opensourceframework.starter.es.processor.ScanLoadProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 *
 *
 * @author yu.ce@foxmail.com
 * @date Created in 10:41 2020/5/19
 */
@EnableConfigurationProperties({ESConfigProperties.class})
public class EsAutoConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(EsAutoConfiguration.class);

	@Autowired
	private ESConfig esConfig;

	@Bean
	public ESConnection esConnection(ESConfig esConfig){
		return new ESConnection(esConfig);
	}

	@Bean
	public ESTemplate esTemplate(ESConnection esConnection){
		return new ESTemplate(esConnection);
	}

	@Bean
	public ScanLoadProcessor scanLoadProcessor(ESConfig esConfig){
		return new ScanLoadProcessor(esConfig);
	}
}
