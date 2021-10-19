package org.opensourceframework.component.es.response;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bulk 响应类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午2:43
 */
public class ESBulkResponse {
	private static final Logger logger = LoggerFactory.getLogger(ESBulkResponse.class);
	private final BulkResponse bulkResponse;

	public ESBulkResponse(BulkResponse bulkResponse) {
		this.bulkResponse = bulkResponse;
	}

	public boolean hasFailures() {
		return bulkResponse.hasFailures();
	}

	public void processFailBulkResponse(String errorMsg) {
		for (BulkItemResponse itemResponse : bulkResponse.getItems()) {
			if (!itemResponse.isFailed()) {
				continue;
			}
			if (itemResponse.getFailure().getStatus() == RestStatus.NOT_FOUND) {
				logger.error(itemResponse.getFailureMessage());
			} else {
				throw new RuntimeException(errorMsg + itemResponse.getFailureMessage());
			}
		}
	}
}
