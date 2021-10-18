package org.opensourceframework.component.mongodb.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.LogicalCondition;
import org.opensourceframework.base.db.OperationalEnum;
import org.opensourceframework.base.eo.BaseEoUtil;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.base.microservice.ServiceContext;
import org.opensourceframework.component.mongodb.MongoDateBaseFactory;
import org.opensourceframework.component.mongodb.dao.IBaseMongoDao;
import org.opensourceframework.component.mongodb.eo.BaseMongoEo;
import org.opensourceframework.component.mongodb.contant.MongoConstants;
import org.opensourceframework.component.mongodb.helper.MongoHelper;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonArray;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MongoDB基类Dao实现类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24 9:07 AM
 */
public class BaseMongoDaoImpl<T extends BaseMongoEo, PK extends Serializable> implements IBaseMongoDao<T, PK> {
	private static final Logger logger = LoggerFactory.getLogger(BaseMongoDaoImpl.class);
	private static final String PK_IS_NOT_NULL = "id不能为空";
	private static final String PK_NAME_MONGODB = "_id";
	private static final String PK_NAME_ENTITY = "id";
	@Autowired(required = false)
	protected MongoClient mongoClient;

	protected String dataBase;

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	public MongoCollection<Document> getConCollection() {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = getGenericClass().getName();
		MongoCollection<Document> collection = db.getCollection(className);
		return collection;
	}

	/**
	 * 获取接口的泛型类型，如果不存在则返回null
	 * 
	 * @return
	 */
	private Class<?> getGenericClass() {
		Type t = getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			return ((Class<?>) p[0]);
		}
		return null;
	}

	@Override
	public T find(T entity) {
		// TODO Auto-generated method stub
		return find(entity, new ArrayList<>());
	}

	@Override
	public T find(T entity, List<String> queryProperties) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = entity.getClass().getName();
		MongoCollection<Document> collection = db.getCollection(className);

		Document document = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			document = collection.find(Filters.eq(MongoConstants.PK_FIELD, entity.getId())).first();
		} else {
			document = collection.find(Filters.eq(MongoConstants.PK_FIELD, entity.getId())).projection(buildFiedFilter(queryProperties)).first();
		}
		if (document != null) {
			return (T) JSON.parseObject(JSON.toJSONString(document), entity.getClass());
		} else {
			return null;
		}
	}

	@Override
	public T findById(PK id) {
		// TODO Auto-generated method stub
		return findById(id, null);
	}

	@Override
	public T findById(PK id, List<String> queryProperties) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();

		Document document = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			document = collection.find(new Document(MongoConstants.PK_FIELD, id)).first();
		} else {
			document = collection.find(Filters.eq(MongoConstants.PK_FIELD, id)).projection(buildFiedFilter(queryProperties)).first();
		}
		if (document != null) {
			return (T) JSON.parseObject(JSON.toJSONString(document), getGenericClass());
		} else {
			return null;
		}
	}

	@Override
	public List<T> findByIds(List<PK> ids) {
		// TODO Auto-generated method stub
		return findByIds(ids, null);
	}

	@Override
	public List<T> findByIds(List<PK> ids, List<String> queryProperties) {
		List<T> list = new ArrayList<>();
		MongoCollection<Document> collection = getConCollection();

		MongoCursor<Document> cursor = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(Filters.in(MongoConstants.PK_FIELD, ids)).iterator();
		} else {
			cursor = collection.find(Filters.in(MongoConstants.PK_FIELD, ids)).projection(buildFiedFilter(queryProperties)).iterator();
		}
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
			}
		}
		return list;
	}

	@Override
	public PageInfo<T> findListByPage(Bson condition, Bson sort, Page<T> page) {
		int pageSize = page.getPageSize();
		MongoCollection<Document> collection = getConCollection();
		int skip = page.getPageSize() * (page.getPageNum() - 1);
		MongoCursor<Document> cursor = collection.find(condition).sort(sort).skip(skip).limit(page.getPageSize()).iterator();
		while (cursor.hasNext()) {
			page.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
		}
		Long count = collection.countDocuments(condition);
		page.setTotal(count);
		PageInfo<T> info = new PageInfo<>(page);
		return info;
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, String> unwindMap, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, String groupJson, Page<T> page) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());
		int skip = page.getPageSize() * (page.getPageNum() - 1);
		List<Bson> pipeline = buildPipeline(condition, unwindMap, groupJson, sortMap, projectMap, skip, page.getPageSize());
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		while (cursor.hasNext()) {
			page.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
		}
		Long count = collection.countDocuments(MongoHelper.buildConditionBson(condition));
		page.setTotal(count);
		PageInfo<T> info = new PageInfo<>(page);
		return info;
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Map<String, Integer> projectMap, String groupJson,
			Page<T> page) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());
		int skip = page.getPageSize() * (page.getPageNum() - 1);
		List<Bson> pipeline = buildPipeline(condition, null, groupJson, sortMap, projectMap, skip, page.getPageSize());
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		while (cursor.hasNext()) {
			page.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
		}
		Long count = collection.countDocuments(MongoHelper.buildConditionBson(condition));
		page.setTotal(count);
		PageInfo<T> info = new PageInfo<>(page);
		return info;
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return findAll(null);
	}

	@Override
	public List<T> findAll(List<String> queryProperties) {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<>();
		MongoCollection<Document> collection = getConCollection();

		MongoCursor<Document> cursor = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find().iterator();
		} else {
			cursor = collection.find().projection(buildFiedFilter(queryProperties)).iterator();
		}
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
			}

		}
		return list;
	}

	@Override
	public Long count(Condition condition) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		return collection.countDocuments(MongoHelper.buildConditionBson(condition));
	}

	@Override
	public PK insert(T entity) {
		// TODO Auto-generated method stub
		if (entity.getId() == null) {
			entity.setId(getId());
		}
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = entity.getClass().getName();
		Document document = Document.parse(JSON.toJSONString(entity));
		MongoCollection<Document> collection = db.getCollection(className);
		collection.insertOne(document);
		return (PK) entity.getId();
	}

	@Override
	public Long delete(T entity) {
		// TODO Auto-generated method stub
		Assert.notNull(entity.getId(), PK_IS_NOT_NULL);
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = entity.getClass().getName();
		MongoCollection<DBObject> collection = db.getCollection(className, DBObject.class);
		DeleteResult result = collection.deleteOne(new Document(MongoConstants.PK_FIELD, entity.getId()));
		return result.getDeletedCount();
	}

	@Override
	public Long deleteById(PK id) {
		// TODO Auto-generated method stub
		Assert.notNull(id, PK_IS_NOT_NULL);
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteOne(new Document(MongoConstants.PK_FIELD, id));
		return result.getDeletedCount();
	}

	@Override
	public Long deleteByIds(List<PK> ids) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteMany(Filters.in(MongoConstants.PK_FIELD, ids));
		return result.getDeletedCount();
	}

	@Override
	public Long deleteAll() {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteMany(new Document());
		return result.getDeletedCount();
	}

	@Override
	public Boolean check(Map<String, Serializable> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long batchDelete(List<T> entities) {
		// TODO Auto-generated method stub
		Long delCount = 0L;
		List<PK> pkIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entities)) {
			for (T entity : entities) {
				if (entity.getId() != null) {
					pkIds.add((PK) entity.getId());
				}
				if (!CollectionUtils.isEmpty(pkIds)) {
					delCount = deleteByIds(pkIds);
				}
			}
		}
		return delCount;
	}

	@Override
	public Long batchInsert(List<T> entitys) {
		// TODO Auto-generated method stub
		if (!CollectionUtils.isEmpty(entitys)) {
			MongoDatabase db = mongoClient.getDatabase(currentDataBase());
			String className = entitys.get(0).getClass().getName();
			List<Document> documents = new ArrayList<>();
			for (T entity : entitys) {
				if (entity.getId() == null) {
					entity.setId(getId());
				}
				Document document = Document.parse(JSON.toJSONString(entity));
				documents.add(document);
			}
			MongoCollection<Document> collection = db.getCollection(className);
			collection.insertMany(documents);
			return Long.valueOf(documents.size());
		}
		return 0L;
	}

	@Override
	public Long batchUpdate(List<T> entitys) {
		// TODO Auto-generated method stub
		if (!CollectionUtils.isEmpty(entitys)) {
			MongoDatabase db = mongoClient.getDatabase(currentDataBase());
			String className = entitys.get(0).getClass().getName();

			// 根据mongoDB 官方文档db.runCommand语法封装
			Document command = new Document();
			command.put("update", className);

			List<Document> updates = new ArrayList<>();
			for (T entity : entitys) {
				Document update = new Document();
				if (entity.getId() == null) {
					logger.warn("exec batchUpdate warn.entity's id is null,update is canceled.data:" + JSON.toJSONString(entity));
					continue;
				}
				update.put("q", new Document(MongoConstants.PK_FIELD, entity.getId()));

				Document dataMap = Document.parse(JSON.toJSONString(entity));
				Document updateDocument = new Document("$set", dataMap);
				update.put("u", updateDocument);
				update.put("upsert", false);
				update.put("multi", true);
				updates.add(update);
			}

			command.put("updates", updates);
			command.put("ordered", false);

			Document result = db.runCommand(command);
			float status = Float.parseFloat(result.get("ok").toString());
			if (status == 1) {
				return Long.parseLong(result.get("n").toString());
			} else {
				logger.error("exec batchUpdate error.datas:" + JSON.toJSONString(entitys));
				return 0L;
			}
		}
		return 0L;
	}

	private String currentDataBase() {
		if (StringUtils.isBlank(dataBase)) {
			dataBase = MongoDateBaseFactory.DEFAULT_DATA_BASE;
		}
		return dataBase;
	}

	/**
	 * 生成多个属性的索引
	 * @param fieldNames
	 * @return 索引Name
	 * 
	 */
	@Override
	public List<String> createIndex(List<String> fieldNames) {
		List<String> indexNames = new ArrayList<>();
		if (!CollectionUtils.isEmpty(fieldNames)) {
			MongoCollection<Document> collection = getConCollection();
			String indexName = null;
			for (String fieldName : fieldNames) {
				indexName = collection.createIndex(Indexes.ascending(fieldName));
				indexNames.add(indexName);
			}
		}
		return indexNames;
	}

	@Override
	public List<T> findByCondition(Bson queryCond) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		MongoCursor<Document> cursor = collection.find(queryCond).iterator();
		List<T> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
		}
		return findByCondition(queryCond, null);
	}

	/**
	 * 根据查询条件查找对象
	 * 
	 * @param queryCond
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * 
	 */
	@Override
	public List<T> findByCondition(Bson queryCond, List<String> queryProperties) {
		MongoCollection<Document> collection = getConCollection();
		MongoCursor<Document> cursor = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(queryCond).iterator();
		} else {
			cursor = collection.find(queryCond).projection(buildFiedFilter(queryProperties)).iterator();
		}

		List<T> list = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
			}
		}
		return list;
	}

	/**
	 * 根据条件删除数据
	 * @param queryCond
	 * @return
	 * 
	 */
	@Override
	public Long deleteByCondition(Bson queryCond) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteMany(queryCond);
		return result.getDeletedCount();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * 更新所有属性 包括entity中为null的字段 为null的字段将更新为空白字符
	 */
	@Override
	public Long updateWithNull(T entity) {
		// TODO Auto-generated method stub
		Assert.notNull(entity.getId(), "id不允许为空!");
		return updateAllField((PK)entity.getId(), Document.parse(JSON.toJSONString(entity)));
	}

	/**
	 * 根据Id更新dataMap中的数据
	 * 
	 * @param id
	 * @param updateMap
	 * @return
	 */
	@Override
	public Long updateById(PK id, Map<String, Object> updateMap) {
		return updateByIdForDataMap(id, updateMap);
	}

	@Override
	public Long updateByIds(List<PK> ids, Map<String, Object> updateMap) {
		return updateByIdsForDataMap(ids, updateMap);
	}

	/**
	 * 根据Id更新所有属性 包括entity中为null的字段 为null的字段将更新为空白字符
	 * @param id
	 * @param document
	 * @return
	 * 
	 */
	private Long updateAllField(PK id, Document document) {
		Assert.notNull(id, PK_IS_NOT_NULL);
		MongoCollection<Document> collection = getConCollection();
		UpdateResult result = collection.replaceOne(new Document(MongoConstants.PK_FIELD, id), document);
		return result.getModifiedCount();
	}

	@Override
	public Long updateNotWithNull(T entity) {
		// TODO Auto-generated method stub
		Assert.notNull(entity.getId(), PK_IS_NOT_NULL);
		Map<String, Object> dataMap = JSON.parseObject(JSON.toJSONString(entity, SerializerFeature.NotWriteDefaultValue));
		return updateByIdForDataMap((PK)entity.getId(), dataMap);
	}

	@Override
	public Long updateByIdForDataMap(PK id, Map<String, Object> dataMap) {
		// TODO Auto-generated method stub
		Assert.notEmpty(dataMap, "更新信息dataMap不能为空");
		Assert.notNull(id, "id不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = Filters.eq(MongoConstants.PK_FIELD, id);

		Document document = Document.parse(JSON.toJSONString(dataMap));
		List<Bson> setBsons = new ArrayList<>();

		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			if (val instanceof JSONObject) {
				val = jsonObjectConvertToBsonString((JSONObject) val);
			}
			setBsons.add(Updates.set(key, val));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateOne(filter, update);
		return result.getModifiedCount();
	}

	@Override
	public Long updateByIdsForDataMap(List<PK> ids, Map<String, Object> dataMap) {
		// TODO Auto-generated method stub
		Assert.notEmpty(dataMap, "更新信息dataMap不能为空");
		Assert.notNull(ids, "id集合不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = Filters.in(MongoConstants.PK_FIELD, ids);

		Document document = Document.parse(JSON.toJSONString(dataMap));
		List<Bson> setBsons = new ArrayList<>();

		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			if (val instanceof JSONObject) {
				val = jsonObjectConvertToBsonString((JSONObject) val);
			}
			setBsons.add(Updates.set(key, val));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	private BsonArray jsonArrayConvertToBsonArray(JSONArray jsonArray) {
		String jsonData = jsonArray.toJSONString();
		return BsonArray.parse(jsonData);
	}

	private BsonString jsonObjectConvertToBsonString(JSONObject jsonObject) {
		String jsonData = jsonObject.toJSONString();
		return new BsonString(jsonData);
	}

	/**
	 * 根据多个eq条件 and连接查询
	 * @param conditionMap
	 * @param updateMap
	 */
	@Override
	public Long updateByParams(Map<String, Object> conditionMap, Map<String, Object> updateMap) {
		Assert.notEmpty(updateMap, "更新信息不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (CollectionUtils.isEmpty(conditionMap)) {
			filter = new Document();
		} else {
			List<Bson> filters = new ArrayList<>();
			for (String key : conditionMap.keySet()) {
				if("id".equals(key)){
					filters.add(Filters.eq(MongoConstants.PK_FIELD, conditionMap.get(key)));
				} else {
                    filters.add(Filters.eq(key, conditionMap.get(key)));
				}
			}
			filter = Filters.and(filters);
		}

		List<Bson> setBsons = new ArrayList<>();
		Document document = Document.parse(JSON.toJSONString(updateMap));
		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			setBsons.add(Updates.set(key, val));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	/**
	 * 根据多个条件更新信息 and连接
	 * @param conditions
	 * @param updateMap
	 * @return 更新记录的条数
	 */
	@Override
	public Long updateByConditions(List<Condition> conditions, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		Assert.notEmpty(updateMap, "更新信息不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (CollectionUtils.isEmpty(conditions)) {
			filter = new Document();
		} else {
			LogicalCondition logicalCondition = new LogicalCondition(conditions, OperationalEnum.AND);
			filter = MongoHelper.buildConditionBson(logicalCondition);
		}

		List<Bson> setBsons = new ArrayList<>();
		Document document = Document.parse(JSON.toJSONString(updateMap));
		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			setBsons.add(Updates.set(key, val));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	/**
	 * 根据查询条件查询
	 * @param condition
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	@Override
	public List<T> findByCondition(Condition condition, List<String> queryProperties) {
		MongoCollection<Document> collection = getConCollection();
		MongoCursor<Document> cursor = null;

		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(MongoHelper.buildConditionBson(condition)).iterator();
		} else {
			cursor = collection.find(MongoHelper.buildConditionBson(condition)).projection(buildFiedFilter(queryProperties)).iterator();
		}
		List<T> list = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) JSON.parseObject(JSON.toJSONString(cursor.next()), getGenericClass()));
			}
		}
		return list;
	}

	@Override
	public Long updateByCondition(Condition condition, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (condition == null) {
			filter = new Document();
		} else {
			filter = MongoHelper.buildConditionBson(condition);
		}

		List<Bson> setBsons = new ArrayList<>();
		Document document = Document.parse(JSON.toJSONString(updateMap));
		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			setBsons.add(Updates.set(key, val));
			// setBsons.add(Updates.set(key, updateMap.get(key)));
		}
		Bson update = Updates.combine(setBsons);
		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	@Override
	public Long deleteByCondition(Condition condition) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (condition == null) {
			filter = new Document();
		} else {
			filter = MongoHelper.buildConditionBson(condition);
		}
		DeleteResult result = collection.deleteMany(filter);
		return result.getDeletedCount();
	}

	/**
	 * 根据条件查询指定实体类
	 * @param clazz
	 * @param condition
	 * @return
	 * 
	 */
	@Override
	public <E> List<E> query(Class<E> clazz, Condition condition) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(clazz.getName());
		MongoCursor<Document> cursor = collection.find(MongoHelper.buildConditionBson(condition)).iterator();
		List<E> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(JSON.parseObject(JSON.toJSONString(cursor.next()), clazz));
		}
		return list;
	}

	/**
	 * 根据条件查询指定实体类
	 * @param clazz
	 * @param condition
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * 
	 */
	@Override
	public <E> List<E> query(Class<E> clazz, Condition condition, List<String> queryProperties) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(clazz.getName());
		MongoCursor<Document> cursor = null;

		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(MongoHelper.buildConditionBson(condition)).iterator();
		} else {
			cursor = collection.find(MongoHelper.buildConditionBson(condition)).projection(buildFiedFilter(queryProperties)).iterator();
		}

		List<E> list = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add(JSON.parseObject(JSON.toJSONString(cursor.next()), clazz));
			}
		}
		return list;
	}

	/**
	 * @Description
	 * @param queryProperties
	 *            需要查询的属性名
	 * @return
	 * 
	 */
	private Document buildFiedFilter(List<String> queryProperties) {
		int index = 0;
		boolean isHadPK = false;
		for (String property : queryProperties) {
			if (property.equalsIgnoreCase(PK_NAME_ENTITY) || property.equalsIgnoreCase(PK_NAME_MONGODB)) {
				isHadPK = true;
				break;
			}
			index++;
		}
		if (isHadPK) {
			queryProperties.remove(index);
		}
		queryProperties.add(PK_NAME_MONGODB);

		Document fieldFilter = new Document();
		for (String propertyName : queryProperties) {
			fieldFilter.put(propertyName, 1);
		}

		return fieldFilter;
	}

	/**
	 * @Description
	 * @param condition
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param limit
	 *            传null 表示不进行limit操作
	 * @param skip
	 *            传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @return
	 * 
	 */
	@Override
	public List<String> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit) {
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, null, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		List<String> jsonList = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				jsonList.add(JSON.toJSONString(cursor.next()));
			}
		}
		return jsonList;

	}

	@Override
	public Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson) {
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, null, groupJson, null, null, null, null);
		Bson countBson = new Document("$count", "count");
		pipeline.add(countBson);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		String count = null;
		if (cursor != null) {
			while (cursor.hasNext()) {
				count = JSON.toJSONString(cursor.next());
			}
		}
		if (count != null) {
			JSONObject object = JSON.parseObject(count);
			count = object.getString("count");
			return Long.valueOf(count);
		}
		return 0L;
	}

	private List<Bson> buildPipeline(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit) {
		List<Bson> pipeline = new ArrayList<>();

		if (condition != null) {
			Document match = new Document();
			match.put("$match", MongoHelper.buildConditionDocument(condition));
			pipeline.add(match);
		}

		Bson unwind = null;
		if (!CollectionUtils.isEmpty(unwindMap)) {
			unwind = Document.parse(JSON.toJSONString(unwindMap));
			pipeline.add(unwind);
		}

		Bson sortBson = null;
		if (!CollectionUtils.isEmpty(sortMap)) {
			sortBson = Document.parse(JSON.toJSONString(sortMap));
			Document sortDocument = new Document();
			sortDocument.put("$sort", sortBson);
			pipeline.add(sortDocument);
		}

		Bson project = null;
		if (!CollectionUtils.isEmpty(projectMap)) {
			project = Document.parse(JSON.toJSONString(projectMap));
			Document projectdDoc = new Document();
			projectdDoc.put("$project", project);
			pipeline.add(projectdDoc);
		}

		Document group = Document.parse(groupJson);
		pipeline.add(group);

		Bson skipBson = null;
		if (limit != null) {
			skipBson = new Document("$skip", skip);
			pipeline.add(skipBson);
		}

		Bson limitBson = null;
		if (limit != null) {
			limitBson = new Document("$limit", limit);
			pipeline.add(limitBson);
		}
		return pipeline;
	}

	/**
	 * @Description
	 * @param condition
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param limit
	 *            传null 表示不进行limit操作
	 * @param skip
	 *            传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @param resultClass
	 *            查询结果自动转换为的类型
	 * @return
	 */
	@Override
	public <E> List<E> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit, Class<E> resultClass) {

		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, null, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		List<E> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(JSON.parseObject(JSON.toJSONString(cursor.next()), resultClass));
		}
		return list;
	}

	@Override
	public List<String> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit) {
		// TODO Auto-generated method stub
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		List<String> jsonList = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				jsonList.add(JSON.toJSONString(cursor.next()));
			}
		}
		return jsonList;
	}

	@Override
	public <E> PageInfo<E> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit, Class<E> clazz) {
		// TODO Auto-generated method stub
		PageInfo<E> info = new PageInfo<>(new ArrayList<E>());
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		if (cursor != null) {
			while (cursor.hasNext()) {
				info.getList().add(JSON.parseObject(JSON.toJSONString(cursor.next()), clazz));
			}
		}
		Long count = collection.countDocuments(MongoHelper.buildConditionBson(condition));
		info.setTotal(count);
		info.setPageSize(limit);
		return info;
	}

	private Long getId() {
		Long applicationId = ServiceContext.getContext().getRequestApplicationId();
		Long workerId = BaseEoUtil.getWorkerId();
		return SnowFlakeId.nextId(workerId, applicationId);
	}
}
