package org.opensourceframework.starter.es.processor;

import org.opensourceframework.component.es.config.ESConfig;
import org.opensourceframework.component.es.helper.ConfigHelper;
import org.springframework.beans.factory.InitializingBean;

/**
 * ESScan 扫描
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/25 下午6:33
 */
public class ScanLoadProcessor implements InitializingBean {
	private final ESConfig esConfig;

	public ScanLoadProcessor(ESConfig esConfig) {
		this.esConfig = esConfig;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ConfigHelper.scanLoad(esConfig.getScanPath());
	}
}
