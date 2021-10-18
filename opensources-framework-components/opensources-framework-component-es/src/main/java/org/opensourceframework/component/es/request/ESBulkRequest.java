package org.opensourceframework.component.es.request;

import org.opensourceframework.component.es.base.ESConnection;
import org.opensourceframework.component.es.response.ESBulkResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.List;

/**
 * ES 批量请求
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午2:38
 */
public class ESBulkRequest {
	private BulkRequestBuilder bulkRequestBuilder;

	private BulkRequest bulkRequest;
	private final ESConnection esConnection;

	public ESBulkRequest(ESConnection esConnection) {
		this.esConnection = esConnection;
	}

	public void resetBulk() {
		bulkRequest = new BulkRequest();
	}

	public ESBulkRequest setRefreshPolicy(WriteRequest.RefreshPolicy refreshPolicy){
		bulkRequest.setRefreshPolicy(refreshPolicy);
		return this;
	}

	public ESBulkRequest add(ESIndexRequest indexRequest) {
		bulkRequest.add(indexRequest.getIndexRequest());
		return this;
	}

	public ESBulkRequest addInsertList(List<ESIndexRequest> esIndexRequests) {
		esIndexRequests.forEach(indexRequest -> {
			bulkRequest.add(indexRequest.getIndexRequest());
		});
		return this;
	}

	public ESBulkRequest add(ESUpdateRequest updateRequest) {
		bulkRequest.add(updateRequest.getUpdateRequest());
		return this;
	}

	public ESBulkRequest add(ESDeleteRequest deleteRequest) {
		bulkRequest.add(deleteRequest.getDeleteRequest());
		return this;
	}

	public ESBulkRequest addDeleteList(List<ESDeleteRequest> esDeleteRequests) {
		esDeleteRequests.forEach(esDeleteRequest -> {
			add(esDeleteRequest);
		});
		return this;
	}

	public ESBulkResponse bulk() {
		ESBulkResponse esBulkResponse = null;
		if (bulkRequest.numberOfActions() > 0) {
			try {
				bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
				BulkResponse response = esConnection.getRestHighLevelClient().bulk(bulkRequest, RequestOptions.DEFAULT);
				esBulkResponse = new ESBulkResponse(response);
				if (response.hasFailures()) {
					esBulkResponse.processFailBulkResponse("ES sync commit error ");
				}
				return esBulkResponse;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return esBulkResponse;
	}

	public int numberOfActions() {
		return bulkRequest.numberOfActions();
	}

	public BulkRequestBuilder getBulkRequestBuilder() {
		return bulkRequestBuilder;
	}

	public void setBulkRequestBuilder(BulkRequestBuilder bulkRequestBuilder) {
		this.bulkRequestBuilder = bulkRequestBuilder;
	}

	public BulkRequest getBulkRequest() {
		return bulkRequest;
	}

	public void setBulkRequest(BulkRequest bulkRequest) {
		this.bulkRequest = bulkRequest;
	}
}
