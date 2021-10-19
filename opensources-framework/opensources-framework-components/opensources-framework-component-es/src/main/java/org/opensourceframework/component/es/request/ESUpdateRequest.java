package org.opensourceframework.component.es.request;

import org.opensourceframework.component.es.helper.DataHelper;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import java.util.Map;

/**
 * ES 更新数据请求
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午2:30
 */
public class ESUpdateRequest {
	private UpdateRequestBuilder updateRequestBuilder;

	private UpdateRequest updateRequest;

	public ESUpdateRequest(String index, String id) {
		updateRequest = new UpdateRequest(index, id);
	}

	public ESUpdateRequest setDoc(Map source) {
		DataHelper.fieldTypeHandler(source);
		updateRequest.doc(source);
		return this;
	}

	public ESUpdateRequest setDocAsUpsert(boolean shouldUpsertDoc) {
		updateRequest.docAsUpsert(shouldUpsertDoc);
		return this;
	}

	public ESUpdateRequest setRouting(String routing) {
		updateRequest.routing(routing);
		return this;
	}

	public UpdateRequestBuilder getUpdateRequestBuilder() {
		return updateRequestBuilder;
	}

	public void setUpdateRequestBuilder(UpdateRequestBuilder updateRequestBuilder) {
		this.updateRequestBuilder = updateRequestBuilder;
	}

	public UpdateRequest getUpdateRequest() {
		return updateRequest;
	}

	public void setUpdateRequest(UpdateRequest updateRequest) {
		this.updateRequest = updateRequest;
	}
}
