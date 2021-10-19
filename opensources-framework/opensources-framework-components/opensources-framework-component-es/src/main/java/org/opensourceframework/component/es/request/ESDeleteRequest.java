package org.opensourceframework.component.es.request;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;

/**
 * ES 数据删除请求
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午2:33
 */
public class ESDeleteRequest {
	private DeleteRequestBuilder deleteRequestBuilder;

	private DeleteRequest deleteRequest;

	public ESDeleteRequest(String index, String id) {
			deleteRequest = new DeleteRequest(index, id);
	}

	public DeleteRequestBuilder getDeleteRequestBuilder() {
		return deleteRequestBuilder;
	}

	public void setDeleteRequestBuilder(DeleteRequestBuilder deleteRequestBuilder) {
		this.deleteRequestBuilder = deleteRequestBuilder;
	}

	public DeleteRequest getDeleteRequest() {
		return deleteRequest;
	}

	public void setDeleteRequest(DeleteRequest deleteRequest) {
		this.deleteRequest = deleteRequest;
	}
}
