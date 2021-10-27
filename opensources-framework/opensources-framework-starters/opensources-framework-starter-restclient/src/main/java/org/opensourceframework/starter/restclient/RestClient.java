package org.opensourceframework.starter.restclient;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.opensourceframework.common.rest.ApiSignatureException;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * RestFull调用配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RestClient {
	private final Logger logger = LoggerFactory.getLogger(RestClient.class);
	private final RestTemplate restTemplate;
	private ClientHttpRequestInterceptor signInterceptor;
	private final ClientHttpRequestInterceptor headerWrapperInterceptor;
	private final HttpHeaders httpHeaders = new HttpHeaders();
	private HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

	public RestClient(HttpClient httpClient) {
		this.restTemplate = buildRestTemplate(httpClient);
		this.reInitMessageConverter();
		this.headerWrapperInterceptor = new RestClient.HttpRequestHeaderWrapperInterceptor();
		this.restTemplate.setInterceptors(Collections.singletonList(this.headerWrapperInterceptor));
	}

	private RestTemplate buildRestTemplate(HttpClient httpClient){
		requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return new RestTemplate(requestFactory);
	}

	public void setConnectTimeout(int timeout) {
		this.requestFactory.setConnectTimeout(timeout);
	}

	public void setReadTimeout(int timeout) {
		this.requestFactory.setReadTimeout(timeout);
	}


	public RestClient(HttpClient httpClient , String appkey, String privateKey) {
		this.restTemplate = buildRestTemplate(httpClient);
		this.reInitMessageConverter();
		this.signInterceptor = new RestClient.HttpRequestSignInterceptor(appkey, privateKey);
		this.headerWrapperInterceptor = new RestClient.HttpRequestHeaderWrapperInterceptor();
		this.restTemplate.setInterceptors(Arrays.asList(this.signInterceptor, this.headerWrapperInterceptor));

	}

	public void setNeedSign(boolean needSign) {
		if (this.signInterceptor == null) {
			this.logger.warn("签名过滤器（HttpRequestSignInterceptor）没有初始化");
		} else {
			((RestClient.HttpRequestSignInterceptor)this.signInterceptor).setNeedSign(needSign);
		}

	}

	/**
	 * 获取内置的RestTemplate
	 *
	 * @return
	 */
	public RestTemplate restTemplate(){
		return this.restTemplate;
	}

	/**
	 * 添加请求头
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public RestClient addHeader(String name, String value) {
		this.httpHeaders.add(name, value);
		return this;
	}

	/**
	 * 移除请求头
	 *
	 * @param name
	 */
	public void removeHeader(String name) {
		this.httpHeaders.remove(name);
	}

	/**
	 * 设置请求头中某个参数的值
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public RestClient setHeader(String name, String value) {
		this.httpHeaders.set(name, value);
		return this;
	}

	/**
	 * 清除请求头信息
	 *
	 * @return
	 */
	public RestClient clearHeaders() {
		this.httpHeaders.clear();
		return this;
	}

	/**
	 * 设置messageconvert
	 *
	 */
	private void reInitMessageConverter() {
		List<HttpMessageConverter<?>> converterList = this.restTemplate.getMessageConverters();
		Iterator iterator = converterList.iterator();

		// 重新设置StringHttpMessageConverter字符集为UTF-8，解决中文乱码问题
		while(iterator.hasNext()) {
			HttpMessageConverter<?> item = (HttpMessageConverter)iterator.next();
			if (item instanceof StringHttpMessageConverter) {
				int index = converterList.indexOf(item);
				iterator.remove();
				HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
				converterList.add(index, converter);
				break;
			}
		}

		/**
		 * 加入FastJson转换器
		 */
		// 1.定义convert转换消息的对象;
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		// 2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		// 3处理中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		// 4.在convert中添加配置信息.
		fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		// 5.将convert添加到converters当中.
		converterList.add(fastJsonHttpMessageConverter);

	}

	public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		this.restTemplate.setMessageConverters(messageConverters);
	}

	public List<HttpMessageConverter<?>> getMessageConverters() {
		return this.restTemplate.getMessageConverters();
	}

	public void setErrorHandler(ResponseErrorHandler errorHandler) {
		this.restTemplate.setErrorHandler(errorHandler);
	}

	public ResponseErrorHandler getErrorHandler() {
		return this.restTemplate.getErrorHandler();
	}

	public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) {
		return this.restTemplate.getForObject(url, responseType, urlVariables);
	}

	public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> urlVariables) {
		return this.restTemplate.getForObject(url, responseType, urlVariables);
	}

	public <T> T getForObject(URI url, Class<T> responseType) {
		return this.restTemplate.getForObject(url, responseType);
	}

	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... urlVariables) {
		return this.restTemplate.getForEntity(url, responseType, urlVariables);
	}

	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> urlVariables) {
		return this.restTemplate.getForEntity(url, responseType, urlVariables);
	}

	public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) {
		return this.restTemplate.getForEntity(url, responseType);
	}

	public HttpHeaders headForHeaders(String url, Object... urlVariables) {
		return this.restTemplate.headForHeaders(url, urlVariables);
	}

	public HttpHeaders headForHeaders(String url, Map<String, ?> urlVariables) {
		return this.restTemplate.headForHeaders(url, urlVariables);
	}

	public HttpHeaders headForHeaders(URI url) {
		return this.restTemplate.headForHeaders(url);
	}

	public URI postForLocation(String url, Object request, Object... urlVariables) {
		return this.restTemplate.postForLocation(url, request, urlVariables);
	}

	public URI postForLocation(String url, Object request, Map<String, ?> urlVariables) {
		return this.restTemplate.postForLocation(url, request, urlVariables);
	}

	public URI postForLocation(URI url, Object request) {
		return this.restTemplate.postForLocation(url, request);
	}

	public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) {
		return this.restTemplate.postForObject(url, request, responseType, uriVariables);
	}

	public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
		return this.restTemplate.postForObject(url, request, responseType, uriVariables);
	}

	public <T> T postForObject(URI url, Object request, Class<T> responseType) {
		return this.restTemplate.postForObject(url, request, responseType);
	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) {
		return this.restTemplate.postForEntity(url, request, responseType, uriVariables);
	}

	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
		return this.restTemplate.postForEntity(url, request, responseType, uriVariables);
	}

	public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) {
		return this.restTemplate.postForEntity(url, request, responseType);
	}

	public void put(String url, Object request, Object... urlVariables) {
		this.restTemplate.put(url, request, urlVariables);
	}

	public void put(String url, Object request, Map<String, ?> urlVariables) {
		this.restTemplate.put(url, request, urlVariables);
	}

	public void put(URI url, Object request) {
		this.restTemplate.put(url, request);
	}

	public <T> T putForObject(String url, Object request, Class<T> responseType) {
		HttpEntity req = new HttpEntity(request, this.httpHeaders);
		ResponseEntity<T> resultEntity = this.restTemplate.exchange(url, HttpMethod.PUT, req, responseType);
		return resultEntity.getBody();
	}

	public <T> T putForObject(String url, Object request, Class<T> responseType, Object... uriVariables) {
		HttpEntity req = new HttpEntity(request, this.httpHeaders);
		ResponseEntity<T> resultEntity = this.restTemplate.exchange(url, HttpMethod.PUT, req, responseType, uriVariables);
		return resultEntity.getBody();
	}

	public <T> T putForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
		HttpEntity req = new HttpEntity(request, this.httpHeaders);
		ResponseEntity<T> resultEntity = this.restTemplate.exchange(url, HttpMethod.PUT, req, responseType, uriVariables);
		return resultEntity.getBody();
	}

	public void delete(String url, Object... urlVariables) {
		this.restTemplate.delete(url, urlVariables);
	}

	public void delete(String url, Map<String, ?> urlVariables) {
		this.restTemplate.delete(url, urlVariables);
	}

	public void delete(URI url) {
		this.restTemplate.delete(url);
	}

	public Set<HttpMethod> optionsForAllow(String url, Object... urlVariables) {
		return this.restTemplate.optionsForAllow(url, urlVariables);
	}

	public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> urlVariables) {
		return this.restTemplate.optionsForAllow(url, urlVariables);
	}

	public Set<HttpMethod> optionsForAllow(URI url) {
		return this.restTemplate.optionsForAllow(url);
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
		return this.restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
		return this.restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
		return this.restTemplate.exchange(url, method, requestEntity, responseType);
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
		return this.restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
		return this.restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
		return this.restTemplate.exchange(url, method, requestEntity, responseType);
	}

	public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType) {
		return this.restTemplate.exchange(requestEntity, responseType);
	}

	public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
		return this.restTemplate.exchange(requestEntity, responseType);
	}

	public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... urlVariables) {
		return this.restTemplate.execute(url, method, requestCallback, responseExtractor, urlVariables);
	}

	public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables) {
		return this.restTemplate.execute(url, method, requestCallback, responseExtractor, urlVariables);
	}

	public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) {
		return this.restTemplate.execute(url, method, requestCallback, responseExtractor);
	}

	public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		this.restTemplate.setInterceptors(interceptors);
	}

	private class HttpRequestHeaderWrapperInterceptor implements ClientHttpRequestInterceptor {
		private HttpRequestHeaderWrapperInterceptor() {
		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
			requestWrapper.getHeaders().putAll(RestClient.this.httpHeaders);
			return execution.execute(request, body);
		}
	}

	private class HttpRequestSignInterceptor implements ClientHttpRequestInterceptor {
		private final String appKey;
		private final String privateKey;
		private boolean needSign = true;

		public HttpRequestSignInterceptor(String appKey, String privateKey) {
			this.appKey = appKey;
			this.privateKey = privateKey;
		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			if (this.isNeedSign()) {
				String url = request.getURI().getPath();
				HttpHeaders headers = request.getHeaders();
				String query = request.getURI().getQuery();
				Map<String, String> queryMap = null;
				if (request.getMethod().equals(HttpMethod.GET)) {
					queryMap = this.queryToMap(query);
				} else {
					queryMap = new HashMap();
				}

				this.sign(url, queryMap, body, headers);
			}

			return execution.execute(request, body);
		}

		private void sign(String url, Map<String, String> queryMap, byte[] body, HttpHeaders headers) {
			if (StringUtils.isEmpty(this.appKey)) {
				throw new ApiSignatureException(HttpStatus.BAD_REQUEST, "签名失败，Appkey不能为空");
			} else if (StringUtils.isEmpty(this.privateKey)) {
				throw new ApiSignatureException(HttpStatus.BAD_REQUEST, "签名失败，私钥不能为空");
			} else {
				MediaType contentType = headers.getContentType();
				String sign = null;
				if (contentType != null && contentType.includes(MediaType.MULTIPART_FORM_DATA)) {
					sign = RestSignUtil.sign(this.appKey, url, queryMap, new byte[0], this.privateKey);
				} else {
					sign = RestSignUtil.sign(this.appKey, url, queryMap, body, this.privateKey);
				}

				headers.set("Appkey", this.appKey);
				headers.set("sign", sign);
			}
		}

		public boolean isNeedSign() {
			return this.needSign;
		}

		public void setNeedSign(boolean needSign) {
			this.needSign = needSign;
		}

		private Map<String, String> queryToMap(String query) {
			Map<String, String> queryMap = new HashMap();
			if (StringUtils.isEmpty(query)) {
				return queryMap;
			} else {
				String[] queryArray = query.split("&");
				int length = queryArray.length;

				for(int i = 0; i < length; ++i) {
					String str = queryArray[i];
					String[] split = str.split("=");
					if (split.length == 1) {
						queryMap.put(split[0], "");
					} else {
						queryMap.put(split[0], split[1]);
					}
				}

				return queryMap;
			}
		}
	}

	public static class HttpHeaderNames {
		public static final String APP_KEY = "Appkey";
		public static final String SIGN = "sign";

		public HttpHeaderNames() {
		}
	}
}

