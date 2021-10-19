package org.opensourceframework.starter.soapclient;

import org.opensourceframework.starter.restclient.config.HttpClientProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Webservice调用配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Configuration
@EnableConfigurationProperties({HttpClientProperties.class})
public class SoapClientAutoConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(SoapClientAutoConfiguration.class);
	@Autowired
	private HttpClientProperties httpClientConfig;

	@Autowired
	private HttpClient httpClient;

	public SoapClientAutoConfiguration() {
	}

	@Bean
	@ConditionalOnMissingBean(value = {HttpClient.class})
	public HttpClient httpClient() {
		// 创建httpclient连接池
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		// 设置连接池最大数量
		connectionManager.setMaxTotal(httpClientConfig.getMaxTotal());
		// 设置单个路由最大连接数量
		connectionManager.setDefaultMaxPerRoute(httpClientConfig.getMaxPerRoute());

		int socketTimeout = httpClientConfig.getReadTimeout();
		int connectTimeout = httpClientConfig.getConnectTimeout();
		int conReqTimeOut = httpClientConfig.getConReqTimeOut();

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(conReqTimeOut).build();

		// 声明重定向策略对象
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager)
				.setRedirectStrategy(redirectStrategy).build();
		logger.info("http client create success. httpClient:{}" , httpClient.toString());
		return httpClient;
	}

	@Bean
	public SoapClient soapClient(){
		return new SoapClient(this.httpClient);
	}
}
