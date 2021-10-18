package org.opensourceframework.component.mongodb.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.db.*;
import org.opensourceframework.component.mongodb.service.BaseMongoService;
import org.opensourceframework.component.mongodb.dao.IBaseMongoDao;
import org.opensourceframework.component.mongodb.eo.BaseMongoEo;
import org.opensourceframework.component.mongodb.helper.MongoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * service基类实现类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public abstract class BaseMongoServiceImpl<T extends BaseMongoEo, PK extends Serializable> implements BaseMongoService<T, PK> {
	/**
	 * 获取数据库操类
	 * 
	 * @return
	 */
	protected abstract IBaseMongoDao<T, PK> getDao();

	@Override
	public T find(T entity) {
		return getDao().find(entity);
	}

	/**
	 * @Description 查询
	 * @param entity
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * 
	 */
	@Override
	public T find(T entity, List<String> queryProperties) {
		return getDao().find(entity, queryProperties);
	}

	@Override
	public T findById(PK id) {
		return getDao().findById(id);
	}

	@Override
	public T findById(PK id, List<String> queryProperties) {
		return getDao().findById(id, queryProperties);
	}

	@Override
	public List<T> findByIds(List<PK> ids) {
		return getDao().findByIds(ids);
	}

	/**
	 * 根据ID集合来查询
	 * 
	 * @param ids
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	@Override
	public List<T> findByIds(List<PK> ids, List<String> queryProperties) {
		return getDao().findByIds(ids, queryProperties);
	}

	@Override
	public PageInfo<T> findListByPage(List<Condition> conditions, List<Sort> sorts, Page<T> page) {
		Document sort = null;
		if (CollectionUtils.isEmpty(sorts)) {
			sort = new Document();
		} else {
			sort = new Document();
			Collections.sort(sorts);
			for (Sort s : sorts) {
				sort.append(s.getProperty(), s.getOrder().getSortVal());
			}
		}

		Condition condition = Restrictions.and(conditions);
		return getDao().findListByPage(MongoHelper.buildConditionBson(condition), sort, page);
	}

	public PageInfo<T> findListByPage(Map<String, Object> parms, Map<String, Integer> sortMap, Page<T> page) {
		Bson filters = null;
		if (MapUtils.isEmpty(parms)) {
			filters = new Document();
		} else {
			filters = Document.parse(JSON.toJSONString(parms));
		}

		Document sort = null;
		if (MapUtils.isEmpty(sortMap)) {
			sort = new Document();
		} else {
			sort = Document.parse(JSON.toJSONString(sortMap));
		}
		return getDao().findListByPage(filters, sort, page);
	}

	@Override
	public List<T> findAll() {
		return getDao().findAll();
	}

	/**
	 * 查询所有记录
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	@Override
	public List<T> findAll(List<String> queryProperties) {
		return getDao().findAll(queryProperties);
	}

	@Override
	public Long count(Condition condition) {
		return getDao().count(condition);
	}

	@Override
	public Long count() {
		Condition condition = new QueryCondition();
		return getDao().count(condition);
	}

	@Override
	public PK insert(T entity) {
		return getDao().insert(entity);
	}

	@Override
	public void delete(T entity) {
		getDao().delete(entity);
	}

	/**
	 * @Description 生成多个属性的索引
	 * @param fieldNames
	 * @return 索引Name
	 * 
	 */
	@Override
	public List<String> createIndex(List<String> fieldNames) {
		return getDao().createIndex(fieldNames);
	}

	@Override
	public void deleteById(PK id) {
		getDao().deleteById(id);
	}

	@Override
	public void deleteByIds(List<PK> ids) {
		getDao().deleteByIds(ids);
	}

	@Override
	public void deleteAll() {
		getDao().deleteAll();
	}

	@Override
	public void update(T entity) {
		getDao().updateNotWithNull(entity);
	}

	@Override
	public void updateNotWithNull(T entity) {
		// TODO Auto-generated method stub
		getDao().updateNotWithNull(entity);
	}

	@Override
	public void updateWithNull(T entity) {
		// TODO Auto-generated method stub
		getDao().updateWithNull(entity);
	}

	@Override
	public Boolean check(Map<String, Serializable> params) {
		return getDao().check(params);
	}

	@Override
	public void batchDelete(List<T> entities) {
		getDao().batchDelete(entities);
	}

	@Override
	public void batchInsert(List<T> entitys) {
		getDao().batchInsert(entitys);
	}

	@Override
	public void batchUpdate(List<T> entitys) {
		getDao().batchUpdate(entitys);
	}

	/**
	 * @Description 根据对象属性查找对象,多个条件默认and连接
	 * @param paraMap
	 * @return
	 * 
	 */
	@Override
	public List<T> findByParams(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Document document = new Document();
		// BasicDBObject queryCond = new BasicDBObject();
		for (String key : paraMap.keySet()) {
			document.append(key, paraMap.get(key));
		}
		return getDao().findByCondition(document);
	}

	/**
	 * @Description 根据条件查询 多个条件and连接
	 * @param conditions
	 * @return
	 * 
	 */
	@Override
	public List<T> findByAndCondition(List<Condition> conditions) {
		if (CollectionUtils.isEmpty(conditions)) {
			return getDao().findAll();
		} else if (conditions.size() == 1) {
			return getDao().findByCondition(MongoHelper.buildConditionBson(conditions.get(0)));
		} else {
			LogicalCondition condition = new LogicalCondition(conditions, OperationalEnum.AND);
			return findByCondition(condition);
		}
	}

	/**
	 * @Description 根据条件查询 多个条件and连接
	 * @param conditions
	 * @return
	 * 
	 */
	@Override
	public List<T> findByAndCondition(List<Condition> conditions, List<String> queryProperties) {
		if (CollectionUtils.isEmpty(conditions)) {
			return getDao().findAll();
		} else if (conditions.size() == 1) {
			return getDao().findByCondition(MongoHelper.buildConditionBson(conditions.get(0)), queryProperties);
		} else {
			LogicalCondition condition = new LogicalCondition(conditions, OperationalEnum.AND);
			return findByCondition(condition, queryProperties);
		}
	}

	/**
	 * @Description 根据条件更新信息 多个条件为and连接,条件都为等值判断,
	 * @param conditionMap
	 * @param updateMap
	 * 
	 */
	@Override
	public Long updateByParams(Map<String, Object> conditionMap, Map<String, Object> updateMap) {
		return getDao().updateByParams(conditionMap, updateMap);
	}

	/**
	 * @Description 根据Id更新dataMap数据
	 * @param id
	 * @param updateMap
	 * @return
	 * 
	 */
	@Override
	public void updateById(PK id, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		getDao().updateById(id, updateMap);
	}

	@Override
	public void updateByIds(List<PK> ids, Map<String, Object> updateMap) {
		getDao().updateByIds(ids, updateMap);
	}

	/**
	 * @Description 根据多个条件更新信息 and连接
	 * @param conditions
	 * @param updateMap
	 * 
	 * @return 更新记录的条数
	 */
	@Override
	public Long updateByAndCondition(List<Condition> conditions, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		return getDao().updateByConditions(conditions, updateMap);
	}

	@Override
	public Long deleteByParams(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		Document document = new Document();
		for (String key : paraMap.keySet()) {
			document.append(key, paraMap.get(key));
		}
		return getDao().deleteByCondition(document);
	}

	@Override
	public Long deleteByAndCondition(List<Condition> conditions) {
		// TODO Auto-generated method stub
		if (CollectionUtils.isEmpty(conditions)) {
			return getDao().deleteAll();
		} else if (conditions.size() == 1) {
			return getDao().deleteByCondition(MongoHelper.buildConditionBson(conditions.get(0)));
		} else {
			LogicalCondition condition = new LogicalCondition(conditions, OperationalEnum.AND);
			return deleteByCondition(condition);
		}
	}

	@Override
	public Long deleteByCondition(Condition condition) {
		Bson bson = MongoHelper.buildConditionBson(condition);
		return getDao().deleteByCondition(bson);
	}

	/**
	 * @Description 根据查询条件查询
	 * @param condition
	 * @return
	 * 
	 */
	@Override
	public List<T> findByCondition(Condition condition, List<String> queryProperties) {
		return getDao().findByCondition(MongoHelper.buildConditionBson(condition), queryProperties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ytb.framework.mvc.mongodb.dubbo.BaseMongoService#updateByLogicalCondition(com.ytb.framework.mvc.vo.LogicalCondition,
	 * java.util.Map)
	 */
	@Override
	public Long updateByCondition(Condition condition, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		return getDao().updateByCondition(condition, updateMap);
	}

	@Override
	public void updateById(T entity) {
		getDao().updateWithNull(entity);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Page<T> page) {
		Bson sort = null;
		if (MapUtils.isEmpty(sortMap)) {
			sort = new Document();
		} else {
			sort = Document.parse(JSON.toJSONString(sortMap));
		}
		return getDao().findListByPage(MongoHelper.buildConditionBson(condition), sort, page);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, List<Sort> sorts, Page<T> page) {
		Document sort = null;
		if (CollectionUtils.isEmpty(sorts)) {
			sort = new Document();
		} else {
			sort = new Document();
			Collections.sort(sorts);
			for (Sort s : sorts) {
				sort.append(s.getProperty(), s.getOrder().getSortVal());
			}
		}

		return getDao().findListByPage(MongoHelper.buildConditionBson(condition), sort, page);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Sort sort, Page<T> page){
		List<Sort> sorts = new ArrayList<>();
		sorts.add(sort);
		return findListByPage(condition,sorts,page);
	}

	@Override
	public PageInfo<T> findListByPage(Map<String, Object> parms, List<Sort> sorts, Page<T> page) {
		// TODO Auto-generated method stub
		Bson filters = null;
		if (MapUtils.isEmpty(parms)) {
			filters = new Document();
		} else {
			filters = Document.parse(JSON.toJSONString(parms));
		}

		Document sort = null;
		if (CollectionUtils.isEmpty(sorts)) {
			sort = new Document();
		} else {
			sort = new Document();
			Collections.sort(sorts);
			for (Sort s : sorts) {
				sort.append(s.getProperty(), s.getOrder().getSortVal());
			}
		}

		return getDao().findListByPage(filters, sort, page);
	}

	@Override
	public PageInfo<T> findListByPage(Map<String, Object> parms, Sort sort, Page<T> page) {
		List<Sort> sorts = new ArrayList<>();
		sorts.add(sort);
		return findListByPage(parms,sorts,page);
	}

	@Override
	public List<T> findByCondition(Condition condition) {
		// TODO Auto-generated method stub
		return getDao().findByCondition(MongoHelper.buildConditionBson(condition));
	}

	@Override
	public <E> List<E> query(Class<E> clazz, Condition condition) {
		// TODO Auto-generated method stub
		return getDao().query(clazz, condition);
	}

	@Override
	public List<String> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit) {
		// TODO Auto-generated method stub
		return getDao().aggregatesQuery(condition, groupJson, sortMap, projectMap, skip, limit);
	}

	@Override
	public <E> List<E> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit, Class<E> resultClass) {
		// TODO Auto-generated method stub
		return getDao().aggregatesQuery(condition, groupJson, sortMap, projectMap, skip, limit, resultClass);
	}

	@Override
	public List<String> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit) {
		// TODO Auto-generated method stub
		return getDao().aggregatesQuery(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit);
	}

	@Override
	public Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson) {
		return getDao().aggregatesCount(condition, unwindMap, groupJson);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, String> unwindMap, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, String groupJson, Page<T> page) {
		// TODO Auto-generated method stub
		return getDao().findListByPage(condition, unwindMap, sortMap, projectMap, groupJson, page);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Map<String, Integer> projectMap, String groupJson,
			Page<T> page) {
		// TODO Auto-generated method stub
		return getDao().findListByPage(condition, sortMap, projectMap, groupJson, page);
	}

	@Override
	public <E> PageInfo<E> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit, Class<E> clazz) {
		return getDao().aggregatesQuery(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit, clazz);
	}
}
