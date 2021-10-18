package org.opensourceframework.component.es.base;

import org.opensourceframework.component.es.config.ESConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

/**
 * ES 连接类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午1:47
 */
public class ESConnection {
	private static final Logger logger = LoggerFactory.getLogger(ESConnection.class);
	private static final String SERVER_HOST_LIST_SPLIT = ",";
	private static final String SERVER_IP_PORT_SPLIT = ":";

	private RestHighLevelClient restHighLevelClient;

	public ESConnection(ESConfig esConfig) {
		String seversHosts = esConfig.getServerHosts();
		HttpHost[] httpHosts = buildHttpHost(seversHosts);
		RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
		String userName = esConfig.getUserName();
		String passWord = esConfig.getPassWord();

		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(passWord)) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName.trim(), passWord.trim()));
			restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		}
		restHighLevelClient = new RestHighLevelClient(restClientBuilder);
		logger.info("connect es servers:{} success." , esConfig.getServerHosts());
	}

	private HttpHost[] buildHttpHost(String serverHosts) {
		if (StringUtils.isBlank(serverHosts)) {
			throw new ElasticsearchException("Not Found Config For Server Hosts!");
		}
		String[] serverHostArray = serverHosts.split(SERVER_HOST_LIST_SPLIT);
		HttpHost[] httpHosts = new HttpHost[serverHostArray.length];
		int count = 0;
		try {
			for (String serverHost : serverHostArray) {
				String[] server = serverHost.split(SERVER_IP_PORT_SPLIT);
				if (server.length == 2) {
					HttpHost httpHost = new HttpHost(InetAddress.getByName(server[0].trim()), Integer.parseInt(server[1].trim()));
					httpHosts[count++] = httpHost;
				} else {
					throw new ElasticsearchException("Server Hosts Config is Error!ServerHosts:{}", serverHosts);
				}
			}
		}catch (Exception e){
			throw new ElasticsearchException(e.getMessage());
		}
		return httpHosts;
	}

	public void close() {
		try {
			restHighLevelClient.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public MappingMetadata getMapping(String index) {
		MappingMetadata mappingMetaData = null;

		try {
			GetMappingsRequest request = new GetMappingsRequest();
			request.indices(index);
			GetMappingsResponse response = restHighLevelClient.indices().getMapping(request, RequestOptions.DEFAULT);

			Map<String, MappingMetadata> mappings = response.mappings();
			mappingMetaData = mappings.get(index);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Not found the mapping info of index: " + index);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			mappingMetaData = null;
		}

		return mappingMetaData;
	}

	public RestHighLevelClient getRestHighLevelClient() {
		return restHighLevelClient;
	}

	public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}
}
