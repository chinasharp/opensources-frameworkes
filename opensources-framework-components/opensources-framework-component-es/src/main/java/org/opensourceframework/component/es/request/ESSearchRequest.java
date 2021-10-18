package org.opensourceframework.component.es.request;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * ES 数据查询请求
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午2:35
 */
public class ESSearchRequest {
	private SearchRequestBuilder searchRequestBuilder;

	private SearchRequest searchRequest;

	private final SearchSourceBuilder sourceBuilder;

	public ESSearchRequest(String index) {
		searchRequest = new SearchRequest(index);
		sourceBuilder = new SearchSourceBuilder();
	}

	public ESSearchRequest setQuery(QueryBuilder queryBuilder) {
		sourceBuilder.query(queryBuilder);
		return this;
	}

	public ESSearchRequest size(int size) {
		sourceBuilder.size(size);
		return this;
	}

	public ESSearchRequest from(int size) {
		sourceBuilder.from(size);
		return this;
	}

	public SearchRequestBuilder getSearchRequestBuilder() {
		return searchRequestBuilder;
	}

	public void setSearchRequestBuilder(SearchRequestBuilder searchRequestBuilder) {
		this.searchRequestBuilder = searchRequestBuilder;
	}

	public SearchRequest getSearchRequest() {
		return searchRequest;
	}

	public void setSearchRequest(SearchRequest searchRequest) {
		this.searchRequest = searchRequest;
	}
}
