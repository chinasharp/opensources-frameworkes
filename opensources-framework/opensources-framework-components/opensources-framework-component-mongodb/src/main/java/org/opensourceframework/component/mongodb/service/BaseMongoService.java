package org.opensourceframework.component.mongodb.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.db.Sort;
import org.opensourceframework.component.mongodb.eo.BaseMongoEo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseMongoService
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2018/11/24
 */
public interface BaseMongoService<T extends BaseMongoEo, PK extends Serializable> {

	/**
	 * 查询
	 *
	 * @param entity
	 * @return
	 */
	T find(T entity);

	/**
	 * 通过Id查询
	 *
	 * @param id
	 * @return
	 */
	T findById(PK id);

	/**
	 * 根据ID集合来查询
	 *
	 * @param ids
	 * @return
	 */
	List<T> findByIds(List<PK> ids);

	/**
	 * 查询列表
	 * @param parms
	 * @param sort
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(Map<String, Object> parms, Sort sort, Page<T> page);

	/**
	 * 查询列表
	 *
	 * @param parms
	 * @param sorts
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(Map<String, Object> parms, List<Sort> sorts, Page<T> page);

	/**
	 * 查询所有记录
	 *
	 * @return
	 */
	List<T> findAll();

	/**
	 * 查询总记录数
	 *
	 * @return
	 */
	Long count();

	/**
	 * 添加
	 *
	 * @param entity
	 */
	PK insert(T entity);

	/**
	 * 删除
	 *
	 * @param entity
	 */
	void delete(T entity);

	/**
	 * 根据Id删除
	 *
	 * @param id
	 */
	void deleteById(PK id);

	/**
	 * 根据ID集合删除
	 *
	 * @param ids
	 */
	void deleteByIds(List<PK> ids);

	/**
	 * 删除所有记录
	 */
	void deleteAll();

	/**
	 * 根据Id更新有值的属性、新增字段 不更新值为null或者空字符串的属性
	 *
	 * @param entity
	 */
	void update(T entity);

	/**
	 * 更新所有属性 包括值为null或者空字符串的属性
	 *
	 * @param entity
	 */
	void updateById(T entity);

	/**
	 * 根据实体类集合批量删除
	 *
	 * @param entitys
	 */
	void batchDelete(List<T> entitys);

	/**
	 * 批量插入
	 *
	 * @param entitys
	 */
	void batchInsert(List<T> entitys);

	/**
	 * 批量更新
	 *
	 * @param entitys
	 */
	void batchUpdate(List<T> entitys);

	/**
	 * 检查数据是否已经存在
	 *
	 * @param params
	 * @return
	 */
	Boolean check(Map<String, Serializable> params);

	/**
	 * 查询
	 * @param entity
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	T find(T entity, List<String> queryProperties);

	/**
	 * 查询
	 * @param id
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	T findById(PK id, List<String> queryProperties);

	/**
	 * 根据ID集合来查询
	 *
	 * @param ids
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 */
	List<T> findByIds(List<PK> ids, List<String> queryProperties);

	/**
	 * 查询列表
	 *
	 * @param conditions
	 *            查询条件 and连接
	 * @param sorts
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(List<Condition> conditions, List<Sort> sorts, Page<T> page);

	/**
	 * 查询列表
	 *
	 * @param condition
	 *            查询条件
	 * @param sorts
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(Condition condition, List<Sort> sorts, Page<T> page);

	/**
	 * 查询列表
	 *
	 * @param condition
	 *            查询条件
	 * @param sort
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(Condition condition, Sort sort, Page<T> page);

	/**
	 *
	 * @param condition
	 * @param sortMap
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Page<T> page);

	/**
	 * 查询所有记录
	 *
	 * @param queryProperties  需要查询的实体类属性列表
	 * @return
	 */
	List<T> findAll(List<String> queryProperties);

	/**
	 * 根据查询条件查询
	 *
	 * @param condition
	 * @return
	 */
	List<T> findByCondition(Condition condition);

	/**
	 * 根据查询条件查询 指定的实体类
	 *
	 * @param condition
	 * @param clazz
	 * @return
	 */
	<E> List<E> query(Class<E> clazz, Condition condition);

	/**
	 * 查询总记录数
	 *
	 * @param condition
	 * @return
	 */
	Long count(Condition condition);

	/**
	 * 根据参数条件删除数据
	 *
	 * @param paraMap
	 * @return 删除的记录数
	 */
	Long deleteByParams(Map<String, Object> paraMap);

	/**
	 * 根据条件删除 多个条件默认and连接
	 * @param conditions
	 *
	 * @return 删除记录的条数
	 */
	Long deleteByAndCondition(List<Condition> conditions);

	/**
	 * 更新所有属性 包括entity中为null的字段 为null的字段将更新为空白字符
	 *
	 * @param entity
	 */
	void updateWithNull(T entity);

	/**
	 * 更新所有属性 不包括entity中为null的属性
	 *
	 * @param entity
	 * @return
	 */
	void updateNotWithNull(T entity);

	/**
	 * 根据Id更新updateMap中的数据
	 *
	 * @param id
	 * @param updateMap
	 * @return
	 */
	void updateById(PK id, Map<String, Object> updateMap);

	/**
	 * 根据Id集合更新updateMap中的数据
	 *
	 * @param ids
	 * @param updateMap
	 */
	void updateByIds(List<PK> ids, Map<String, Object> updateMap);

	/**
	 * 生成多个属性的索引
	 *
	 * @param fieldNames
	 * @return 索引Name
	 */
	List<String> createIndex(List<String> fieldNames);

	/**
	 * 根据对象属性查找对象,多个条件默认and连接 条件都为等值判断
	 *
	 * @param paraMap
	 * @return
	 */
	List<T> findByParams(Map<String, Object> paraMap);

	/**
	 * 根据条件查询 多个条件默认and连接
	 *
	 * @param conditions
	 * @return 更新记录的条数
	 */
	List<T> findByAndCondition(List<Condition> conditions);

	/**
	 * 根据条件查询 多个条件默认and连接
	 *
	 * @param conditions
	 * @param queryProperties  需要查询的实体类属性列表
	 *
	 * @return 更新记录的条数
	 */
	List<T> findByAndCondition(List<Condition> conditions, List<String> queryProperties);

	/**
	 * 根据多个eq条件更新信息 and连接
	 *
	 * @param conditionMap
	 * @param updateMap
	 *
	 * @return 更新记录的条数
	 */
	Long updateByParams(Map<String, Object> conditionMap, Map<String, Object> updateMap);

	/**
	 * 根据多个条件更新信息 and连接
	 *
	 * @param conditions
	 * @param updateMap
	 *
	 * @return 更新记录的条数
	 */
	Long updateByAndCondition(List<Condition> conditions, Map<String, Object> updateMap);

	/**
	 * 根据查询条件查询
	 * @param condition
	 * @param queryProperties 需要查询的实体类属性列表
	 *
	 * @return
	 * 
	 */
	List<T> findByCondition(Condition condition, List<String> queryProperties);

	/**
	 * 根据查询条件删除
	 *
	 * @param condition
	 * @return
	 */
	Long deleteByCondition(Condition condition);

	/**
	 * 根据查询条件更新
	 *
	 * @param condition
	 * @param updateMap
	 * @return
	 */
	Long updateByCondition(Condition condition, Map<String, Object> updateMap);

	/**
	 * 分组聚合查询
	 * @param condition
	 *            查询条件 为null则没有过滤条件
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
	 */
	List<String> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit);

	/**
	 * 分组聚合查询
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
	<E> List<E> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit, Class<E> resultClass);

	/**
	 * 数据中有子集，对子集中的数据进行分组统计。
	 *
	 * @param matchCondition
	 * @param unwindMap
	 *            将数组集合中的每一个值拆分成独立的文档
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param skip
	 *            传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @param limit
	 *            传null 表示不进行limit操作
	 * @return
	 */
	List<String> aggregatesQuery(Condition matchCondition, Map<String, String> unwindMap, String groupJson,
			Map<String, Integer> sortMap, Map<String, Integer> projectMap, Integer skip, Integer limit);

	/**
	 * 分组总记录数
	 *
	 * @param condition
	 * @param unwindMap
	 * @param groupJson
	 * @return
	 */
	Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson);

	/**
	 * 查询列表
	 *
	 * @param condition
	 *            查询条件 and连接
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param unwindMap
	 *            将数组集合中的每一个值拆分成独立的文档
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(Condition condition, Map<String, String> unwindMap, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, String groupJson, Page<T> page);

	/**
	 * 查询列表
	 *
	 * @param condition
	 *            查询条件 and连接
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param page
	 * @return
	 */
	PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, String groupJson, Page<T> page);

	/**
	 * 查询列表
	 *
	 * @param condition
	 *            查询条件 and连接
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param unwindMap
	 *            将数组集合中的每一个值拆分成独立的文档
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param skip
	 * @param limit
	 * @param clazz
	 *            需要序列化的对象
	 * @return
	 */
	<E> PageInfo<E> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson,
			Map<String, Integer> sortMap, Map<String, Integer> projectMap, Integer skip, Integer limit, Class<E> clazz);
}
