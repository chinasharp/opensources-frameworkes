package org.opensourceframework.center.demo.biz.apiimpl.query;

import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.starter.rediscache.config.RedisProperties;
import org.opensourceframework.starter.restclient.config.HttpClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefreshScope 测试
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/nacos/config")
public class NacosRefreshScopeTest {
    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private HttpClientProperties httpClientProperties;

    @GetMapping("/redis")
    public RestResponse<RedisProperties> queryRedisConfig(){
        return new RestResponse<>(redisProperties);
    }

    @GetMapping("/httpclient")
    public RestResponse<HttpClientProperties> queryHttpClientConfig(){
        return new RestResponse<>(httpClientProperties);
    }
}
