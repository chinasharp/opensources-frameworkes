package org.opensourceframework.starter.restclient;

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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动加载配置
 *
 * @author yu.ce
 * @since 1.0.0
 *
 */
@Configuration
@EnableConfigurationProperties({HttpClientProperties.class})
public class RestClientAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RestClientAutoConfiguration.class);
    @Autowired
    private HttpClientProperties httpClientProperties;

    @Autowired
    private HttpClient httpClient;
    
    public RestClientAutoConfiguration() {
    }

    @Bean
    public HttpClient httpClient() {
        // 创建httpclient连接池
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        // 设置连接池最大数量
        connectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
        // 设置单个路由最大连接数量
        connectionManager.setDefaultMaxPerRoute(httpClientProperties.getMaxPerRoute());

        int socketTimeout = httpClientProperties.getReadTimeout();
        int connectTimeout = httpClientProperties.getConnectTimeout();
        int conReqTimeOut = httpClientProperties.getConReqTimeOut();

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
    public RestClient restClient(){
        return new RestClient(this.httpClient);
    }
}

