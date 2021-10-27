package org.opensourceframework.test.httpclient;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.demo.httpclient.HttpClientDemoBoot;
import org.opensourceframework.component.httpclient.service.HttpService;
import org.opensourceframework.component.httpclient.vo.HttpResponse;

/**
 * 
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HttpClientDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.application.name=opensources-user-httpclient"})
public class HttpClientTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientTest.class);

    @Autowired
    private HttpService httpService;

    @Test
    public void testGet() {
        HttpResponse httpResponse = httpService.get("https://api.apiopen.top/getAllUrl");
        Assertions.assertThat(httpResponse.getStatusCode()).isEqualTo(200);
        logger.info(JSON.toJSONString(httpResponse.getBody()));
    }

    @Test
    public void testPost() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", "1");
        paramsMap.put("count", "5");
        HttpResponse httpResponse = httpService.postJson("https://api.apiopen.top/getWangYiNews", JSON.toJSONString(paramsMap));
        Assertions.assertThat(httpResponse.getStatusCode()).isEqualTo(200);
        logger.info(JSON.toJSONString(httpResponse.getBody()));
    }
}
