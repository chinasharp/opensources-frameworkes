package org.opensourceframework.component.httpclient.service;

import org.opensourceframework.component.httpclient.config.HttpClientConfig;
import org.opensourceframework.component.httpclient.vo.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于HttpClient的Http请求服务，支持连接池
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
public class HttpService {
    private static final Logger log = LoggerFactory.getLogger(HttpService.class);
    private static final String DEFAULT_CHARSET = "UTF-8";

    private final CloseableHttpClient httpClient;

    private final ResponseHandler<HttpResponse> responseHandler;

    public HttpService(HttpClientConfig httpClientConfig) {
        httpClient = initHttpClient(httpClientConfig);
        responseHandler = initResponseHandler();
        shutdownHook();
    }

    /**
     * 初始化HttpClient
     * @return
     */
    private CloseableHttpClient initHttpClient(HttpClientConfig httpClientConfig) {
        // 创建httpclient连接池
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
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
        log.info("http client create success. httpClient:{}", httpClient.toString());

        return httpClient;
    }

    /**
     * 初始化返回响应
     * @return ResponseHandler
     */
    private ResponseHandler<HttpResponse> initResponseHandler() {
        return new ResponseHandler<HttpResponse>() {
            @Override
            public HttpResponse handleResponse(org.apache.http.HttpResponse httpResponse) throws IOException {
                StatusLine statusLine = httpResponse.getStatusLine();
                HttpEntity entity = httpResponse.getEntity();
                final byte[] bodyBytes;
                if (entity != null) {
                    bodyBytes = EntityUtils.toByteArray(entity);
                } else {
                    bodyBytes = new byte[0];
                }
                HttpResponse respObj = new HttpResponse();
                respObj.setStatusCode(statusLine.getStatusCode());
                respObj.setBody(new String(bodyBytes, DEFAULT_CHARSET));

                return respObj;
            }
        };
    }

    /**
     * 关闭HttpClient连接
     */
    private void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("thread-http.shutdown") {
            @Override
            public void run() {
                try {
                    httpClient.close();
                } catch (IOException ignore) {
                    log.error("httpClient 关闭失败", ignore);
                }
            }
        });
    }

    /**
     * 发送get请求，不带请求头和请求参数
     * @param url 请求链接
     * @return HttpResponse
     */
    public HttpResponse get(String url) {
        return get(url, null);
    }

    /**
     * 发送get请求， 带请求参数
     * @param url 请求链接
     * @param params 请求参数
     * @return HttpResponse
     */
    public HttpResponse get(String url, Map<String, Object> params) {
        return getHttpRespObj(url, params, null);
    }

    /**
     * 发送get请求，带请求头和请求参数
     * @param url 请求链接
     * @param params 请求参数
     * @param headers 请求头
     * @return HttpResponse
     */
    public String get(String url, Map<String, Object> params, Map<String, String> headers) {
        return getHttpRespObj(url, params, headers).getBody();
    }


    /**
     * 发送get请求调用封装
     * @param url 请求链接
     * @param params 请求参数
     * @param headers 请求头
     * @return HttpResponse
     */
    private HttpResponse getHttpRespObj(String url, Map<String, Object> params,
                                       Map<String, String> headers) {

        //===1.组装url
        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> param : params.entrySet()) {
                nvps.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
            }
            String queryString = URLEncodedUtils.format(nvps, DEFAULT_CHARSET);
            url = url + "?" + queryString;
        }

        HttpGet get = new HttpGet(url);

        return execute(get, headers);
    }


    /**
     * 发送post请求，带请求参数
     * @param url 请求链接
     * @param params 请求参数
     * @return HttpResponse
     */
    public HttpResponse post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    /**
     * 发送post请求，带请求参数，请求头
     * @param url 请求链接
     * @param params 请求参数
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse post(String url, Map<String, Object> params, Map<String, String> headers) {
        UrlEncodedFormEntity entity = null;
        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> param : params.entrySet()) {
                nvps.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
            }
            entity = new UrlEncodedFormEntity(nvps, Charset.forName(DEFAULT_CHARSET));
        }
        return post(url, entity, headers);
    }

    /**
     * 发送post请求，contentType为application/json格式
     * @param url 请求链接
     * @param body 请求体
     * @return HttpResponse
     */
    public HttpResponse postJson(String url, String body) {
        return postJson(url, body, null);
    }

    /**
     * 发送post请求，contentType为application/json格式，带请求头
     * @param url 请求链接
     * @param body 请求体
     * @param headers 请求头
     * @return HttpResponse
     */
    public HttpResponse postJson(String url, String body, Map<String, String> headers) {
        StringEntity entity = new StringEntity(body, DEFAULT_CHARSET);
        entity.setContentType("application/json");
        return post(url, entity, headers);
    }


    /**
     * 发送post请求封装
     * @param url 请求链接
     * @param entity 请求实体
     * @param headers 请求头
     * @return HttpResponse
     */
    private HttpResponse post(String url, HttpEntity entity, Map<String, String> headers) {
        HttpPost post = new HttpPost(url);
        if (entity != null) {
            post.setEntity(entity);
        }
        return execute(post, headers);
    }

    /**
     * 发送请求具体封装方法
     * @param req 请求
     * @param headers 请求头
     * @return HttpResponse
     */
    private HttpResponse execute(HttpUriRequest req, Map<String, String> headers) {
        //===请求header
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                req.setHeader(entry.getKey(), entry.getValue());
            }
        }
        //===traceId
        String traceId = MDC.get("X-B3-TraceId");
        if (StringUtils.isNotBlank(traceId)) {
            req.setHeader("X-B3-TraceId", traceId);
        }
        try {
            return httpClient.execute(req, responseHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
