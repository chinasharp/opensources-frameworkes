package org.opensourceframework.component.es.helper;

import org.opensourceframework.component.es.request.ESBulkRequest;
import org.opensourceframework.component.es.response.ESBulkResponse;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * ES 数据处理帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/5 上午3:28
 */
public class DataHelper {
	private static final Logger logger = LoggerFactory.getLogger(DataHelper.class);

	private static final int ES_COMMIT_NUM = 3000;
	public static final String ES_DATA_ID = "_id";

	/***
	 * 处理写入ES时 ES不支持的数据类型
	 *
	 * @param esFieldDatas
	 */
	public static void fieldTypeHandler(Map<String, Object> esFieldDatas) {
		if (MapUtils.isNotEmpty(esFieldDatas)) {
			for (Map.Entry<String, Object> esFieldData : esFieldDatas.entrySet()) {
				if (esFieldData.getValue() != null) {
					if (esFieldData.getValue() instanceof Timestamp) {
						Timestamp timestamp = (Timestamp) esFieldData.getValue();
						Date date = new Date(timestamp.getTime());
						esFieldDatas.put(esFieldData.getKey(), date);
					}
				}
			}
		}
	}

	public static void runningBulkCommit(ESBulkRequest esBulkRequest){
		if (esBulkRequest.numberOfActions()  >  ES_COMMIT_NUM) {
			ESBulkResponse rp = esBulkRequest.bulk();
			if (rp.hasFailures()) {
				rp.processFailBulkResponse("Sync Data error!");
			}
			esBulkRequest.resetBulk();
			logger.info("ESBulkRequest 提交数据累计超过 {} 条,执行提交操作." , ES_COMMIT_NUM );
		}
	}

	public static void finallyCommit(ESBulkRequest esBulkRequest) {
		if(esBulkRequest.numberOfActions() > 0) {
			ESBulkResponse rp = esBulkRequest.bulk();
			if (rp.hasFailures()) {
				rp.processFailBulkResponse("Sync data error!");
			}
		}
		logger.info("Sync Data End! ESBulkRequest执行最后数据提交!");
	}
}
