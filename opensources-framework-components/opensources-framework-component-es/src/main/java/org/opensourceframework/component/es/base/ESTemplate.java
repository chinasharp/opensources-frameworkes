package org.opensourceframework.component.es.base;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.opensourceframework.component.es.helper.ESConditionHelper;
import org.opensourceframework.component.es.request.ESBulkRequest;
import org.opensourceframework.component.es.response.ESBulkResponse;
import org.opensourceframework.component.es.config.ESDocument;
import org.opensourceframework.component.es.helper.ConfigHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ES 模板类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/23 下午2:46
 */
public class ESTemplate {
	private static final Logger logger = LoggerFactory.getLogger(ESTemplate.class);
	private static final int MAX_BATCH_SIZE = 10;

	private final ESConnection esConnection;
	private final ESBulkRequest esBulkRequest;

	public ESTemplate(ESConnection esConnection) {
		this.esConnection = esConnection;
		this.esBulkRequest = new ESBulkRequest(esConnection);
	}

	public void resetBulkRequest() {
		this.esBulkRequest.resetBulk();
	}

	public ESBulkResponse commit() {
		ESBulkResponse response= esBulkRequest.bulk();
		return response;
	}

	/**
	 * 删除索引
	 *
	 * @param esDocuments
	 * @return 已删除的index集合
	 */
	Set<String> deleteIndexes(List<ESDocument> esDocuments){
		Set<String> successIndexSet = Sets.newHashSet();
		Set<String> delIndexSet = Sets.newHashSet();

		if (CollectionUtils.isNotEmpty(esDocuments)) {
			esDocuments.forEach(esDocument -> {
				delIndexSet.add(esDocument.getIndex());
			});

			if (CollectionUtils.isNotEmpty(delIndexSet)) {
				for (String index : delIndexSet) {
					try {
						GetIndexRequest getIndexRequest = new GetIndexRequest(index);
						boolean isExists = esConnection.getRestHighLevelClient().indices().exists(getIndexRequest, RequestOptions.DEFAULT);
						if (isExists) {
							DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
							esConnection.getRestHighLevelClient().indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
							successIndexSet.add(index);
						} else {
							logger.info("no found index:{} , delete index operation skip!", index);
						}
					} catch (Exception exception) {

					}
				}
			}
		}
		return successIndexSet;
	}

	/**
	 * 创建索引
	 *
	 * @param esDocuments
	 * @return 已删除的index集合
	 */
	Set<String> createIndexes(List<ESDocument> esDocuments){
		Set<String> successIndex = new HashSet<>();
		Map<String, ESDocument> esMappingMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(esDocuments)) {
			esDocuments.forEach(esDocument -> {
				if (!esMappingMap.containsKey(esDocument.getIndex())) {
					esMappingMap.put(esDocument.getIndex(), esDocument);
				}
			});

			//创建索引
			for (Map.Entry<String, ESDocument> entry : esMappingMap.entrySet()) {
				ESDocument esDocument = entry.getValue();
				String index = esDocument.getIndex();
				String type = esDocument.getType();
				if(StringUtils.isNotBlank(type)){
					type = index;
				}
				String mappings = esDocument.getMapping();
				try {
					GetIndexRequest getIndexRequest = new GetIndexRequest(index);
					boolean isExists = esConnection.getRestHighLevelClient().indices().exists(getIndexRequest, RequestOptions.DEFAULT);

					if (!isExists) {
						CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
						createIndexRequest.settings(Settings.builder().put("max_result_window", 5000000));
						esConnection.getRestHighLevelClient().indices().create(createIndexRequest, RequestOptions.DEFAULT);

						//为索引添加映射
						XContentBuilder contentBuilder = XContentFactory.jsonBuilder();
						InputStream stream = new ByteArrayInputStream(mappings.getBytes());
						contentBuilder.rawValue(stream, XContentType.JSON);
						//pois：索引名   cxyword：类型名（可以自己定义）
						PutMappingRequest putmapRequest = Requests.putMappingRequest(index).type(type).source(contentBuilder);

						esConnection.getRestHighLevelClient().indices().putMapping(putmapRequest, RequestOptions.DEFAULT);
						successIndex.add(index);
					}
				} catch (Exception e) {
					logger.error("create index error.index:{},error:{}", index, e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		}
		return successIndex;
	}

	public <T> List<T> findList(T conditionEo){
		return null;
	}


	public <T> Long findCount(Class<T> tClass , Map<String ,Object> queryParams){
		Long count = 0L;
		String index = ConfigHelper.getIndex(tClass);
		if(StringUtils.isNotBlank(index)) {
			CountRequest countRequest = new CountRequest(index);
			BoolQueryBuilder queryBuilder = ESConditionHelper.buildBoolQueryBuilder(queryParams);
			if(queryBuilder != null) {
				countRequest.query(queryBuilder);
			}
			try {
				CountResponse countResponse = esConnection.getRestHighLevelClient().count(countRequest, RequestOptions.DEFAULT);
				count = countResponse.getCount();
			} catch (IOException e) {
				e.printStackTrace();
				count = 0L;
			}
		}
		return count;
	}

	public <T> List<T> findByPage(Class<T> tClass , Map<String , Object> queryParams , int from , int size){
		List<T> tList = new ArrayList<>();
		String index = ConfigHelper.getIndex(tClass);
		if(StringUtils.isNotBlank(index)) {
			SearchRequest searchRequest = new SearchRequest(index);
			SearchSourceBuilder requestBuilder = new SearchSourceBuilder();

			BoolQueryBuilder queryBuilder = ESConditionHelper.buildBoolQueryBuilder(queryParams);
			if(queryBuilder != null) {
				requestBuilder.query(queryBuilder);
			}
			searchRequest.source(requestBuilder.from(from).size(size));

			try {
				SearchResponse searchResponse = esConnection.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
				SearchHit[] searchHits = searchResponse.getHits().getHits();
				if (searchHits.length > 0) {
					for (SearchHit searchHit : searchHits) {
						Map<String, Object> data = searchHit.getSourceAsMap();
						T t = JSON.parseObject(JSON.toJSONString(data), tClass);
						tList.add(t);
					}
				}
			} catch (IOException e) {
				tList = new ArrayList<>();
			}
		}
		return tList;
	}

	public <T> List<T> findByParams(Class<T> tClass , Map<String , Object> queryParams){
		List<T> tlist = new ArrayList<>();
		String index = ConfigHelper.getIndex(tClass);
		if(StringUtils.isNotBlank(index)) {
			List<SearchHit> searchHitList = queryByParams(index, queryParams, 1000);
			if(CollectionUtils.isNotEmpty(searchHitList)) {
				for (SearchHit searchHit : searchHitList) {
					Map<String, Object> data = searchHit.getSourceAsMap();
					T t = JSON.parseObject(JSON.toJSONString(data), tClass);
					tlist.add(t);
				}
			}
		}
		return tlist;
	}

	private List<SearchHit> queryByParams(String index, Map<String, Object> queryParams, int size) {
		List<SearchHit> searchHitList = new ArrayList<>();
		BoolQueryBuilder queryBuilder = ESConditionHelper.buildBoolQueryBuilder(queryParams);
		SearchRequest searchRequest = new SearchRequest(index);
		int from = 0;
		while (true) {
			try {
				SearchSourceBuilder requestBuilder = new SearchSourceBuilder();
				searchRequest.source(requestBuilder.query(queryBuilder).from(from).size(size));
				SearchResponse searchResponse = esConnection.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
				SearchHit[] searchHits = searchResponse.getHits().getHits();
				if (searchHits.length > 0) {
					searchHitList.addAll(Arrays.asList(searchHits));
				}
				if (searchHits.length != size || searchHits.length == 0) {
					break;
				}
				from = from + size;
				//logger.info("Exec QueryDataByFields. Query Params : {}" , fieldMap);
			} catch (Exception e) {
				logger.error("query es error.exception:{}", e);
			}
		}
		return searchHitList;
	}

	/**
	 * 多数据insert
	 *
	 * @param list
	 */
	public <T> void batchinsert(List<T> list) {
		/*
		if (MapUtils.isNotEmpty(esDataMap)) {
			esDataMap.forEach((pkVal, esFieldData) -> {
				ESUpdateRequest updateRequest =
						esConnection.new ESUpdateRequest(mapping.get_index(), pkVal.toString()).setDoc(esFieldData).setDocAsUpsert(true);
				getBulk().add(updateRequest);
			});
			commitBulk();
		}
		*/
	}

	/**
	 * 批量提交
	 *
	 * @param esBulkRequest
	 */
	private void commit(ESBulkRequest esBulkRequest) {
		if (esBulkRequest.numberOfActions() > 0) {
			ESBulkResponse response = esBulkRequest.bulk();
			if (response.hasFailures()) {
				response.processFailBulkResponse("ES sync commit error ");
			}
			esBulkRequest.resetBulk();
		}
	}
}
