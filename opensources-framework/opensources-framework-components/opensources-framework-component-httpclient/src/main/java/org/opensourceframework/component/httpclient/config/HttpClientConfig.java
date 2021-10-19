package org.opensourceframework.component.httpclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Http配置类
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@RefreshScope
@ConfigurationProperties(prefix = "opensourceframework.httpclient.registryvo")
public class HttpClientConfig {

    /**
     * 连接池的最大连接数默认为50
     */
    @Value("${maxTotal:50}")
    private Integer maxTotal;

    /**
     * 分配给同一个route(路由)最大的并发连接数 默认100
     */
    @Value("${maxPerRoute:100}")
    private Integer maxPerRoute;

    /**
     * 读取数据超时(等待响应超时) 默认30秒
     */
    @Value("${readTimeout:30000}")
    private Integer readTimeout;

    /**
     * 客服端发送请求到与目标url建立起连接的最大时间 默认8秒
     */
    @Value("${connectTimeout:8000}")
    private Integer connectTimeout;

    /**
     * 从连接池中获取可用连接超时时间 默认5秒
     */
    @Value("${conReqTimeOut:5000}")
    private Integer conReqTimeOut;

    public HttpClientConfig() {
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getConReqTimeOut() {
        return conReqTimeOut;
    }

    public void setConReqTimeOut(Integer conReqTimeOut) {
        this.conReqTimeOut = conReqTimeOut;
    }
}
