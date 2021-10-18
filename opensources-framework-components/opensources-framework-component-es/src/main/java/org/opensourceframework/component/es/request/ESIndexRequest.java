package org.opensourceframework.component.es.request;

import org.opensourceframework.component.es.helper.DataHelper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;

import java.util.Map;

/**
 * ES 数据保存请求
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午2:23
 */
public class ESIndexRequest {
	private IndexRequestBuilder indexRequestBuilder;

	private IndexRequest indexRequest;

	public ESIndexRequest(String index, String id) {
		indexRequest = new IndexRequest(index);
		indexRequest.id(id);
	}

	public ESIndexRequest setSource(Map<String, Object> source) {
		//处理es不支持的数据类型
		DataHelper.fieldTypeHandler(source);
		indexRequest.source(source);
		return this;
	}

	public ESIndexRequest setRouting(String routing) {
		indexRequest.routing(routing);
		return this;
	}

	public IndexRequestBuilder getIndexRequestBuilder() {
		return indexRequestBuilder;
	}

	public void setIndexRequestBuilder(IndexRequestBuilder indexRequestBuilder) {
		this.indexRequestBuilder = indexRequestBuilder;
	}

	public IndexRequest getIndexRequest() {
		return indexRequest;
	}

	public void setIndexRequest(IndexRequest indexRequest) {
		this.indexRequest = indexRequest;
	}
}
