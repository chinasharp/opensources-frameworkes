package org.opensourceframework.component.mongodb.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.component.mongodb.eo.BaseMongoEo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * MongoDB基类Dao
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 */
public interface IBaseMongoDao<T extends BaseMongoEo, PK extends Serializable> {

	/**
	 *  获取连接的Collection
	 *
	 * @return 所有的Collection名
	 */
    MongoCollection<Document> getConCollection();

	/**
	 * 查询
	 * 
	 * @param entity 保存对象
	 * @return T
	 */
    T find(T entity);

	/**
	 * 根据给定的属性列表查询entity属性列表的值
	 *
	 * @param entity 查询条件对象
	 * @param queryProperties  需要查询的实体类属性列表
	 *
	 * @return T
	 */
    T find(T entity, List<String> queryProperties);

	/**
	 * 通过Id查询
	 * 
	 * @param id 主键ID
	 * @return T
	 */
    T findById(PK id);

	/**
	 * 根据给定的属性列表查询entity属性列表的值
	 * @param id 主键ID
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return 查询结果对象
	 */
    T findById(PK id, List<String> queryProperties);

	/**
	 * 根据ID集合来查询
	 * 
	 * @param ids 主键id列表
	 * @return 列表数据
	 */
    List<T> findByIds(List<PK> ids);

	/**
	 * 根据ID集合来查询
	 * 
	 * @param ids 主键id列表
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return 列表数据
	 */
    List<T> findByIds(List<PK> ids, List<String> queryProperties);

	/**
	 * 查询列表
	 * 
	 * @param condition  查询条件
	 * @param page 分页参数对象
	 * @param sort 排序字段和排序规则
	 * @return 分页数据
	 */
    PageInfo<T> findListByPage(Bson condition, Bson sort, Page<T> page);

	/**
	 * 查询所有记录
	 * 
	 * @return 列表数据
	 */
    List<T> findAll();

	/**
	 * 查询所有记录
	 *
	 * @param queryProperties 需要查询的实体类属性列表
	 * @return 列表数据
	 */
    List<T> findAll(List<String> queryProperties);

	/**
	 * 查询总记录数
	 *
	 * @param condition 查询条件
	 * @return 数据总数
	 */
    Long count(Condition condition);

	/**
	 * 添加
	 * 
	 * @param entity
	 * @return PK 主键id
	 */
    PK insert(T entity);

	/**
	 * 删除
	 * 
	 * @param entity
	 * @return 删除条数
	 */
    Long delete(T entity);

	/**
	 * 根据Id删除
	 * 
	 * @param id
	 * @return 删除条数
	 */
    Long deleteById(PK id);

	/**
	 * 根据ID集合删除
	 * 
	 * @param ids 主键id列表
	 * @return 删除条数
	 */
    Long deleteByIds(List<PK> ids);

	/**
	 *
	 * 删除所有记录
	 *
	 * @return 删除条数
	 */
    Long deleteAll();

	/**
	 * 根据条件删除数据
	 *
	 * @param queryCond 查询条件
	 * @return 删除条数
	 */
    Long deleteByCondition(Bson queryCond);

	/**
	 * 检查数据是否已经存在
	 * 
	 * @param params 参数
	 * @return true/false
	 */
    Boolean check(Map<String, Serializable> params);

	/**
	 * 批量删除
	 * 
	 * @param entitys 实体对象集合
	 * @return 删除条数
	 */
    Long batchDelete(List<T> entitys);

	/**
	 * 批量插入
	 * 
	 * @param entitys 实体对象集合
	 * @return 插入条数
	 */
    Long batchInsert(List<T> entitys);

	/**
	 * 批量更新
	 * 
	 * @param entitys 实体对象集合
	 * @return 删除条数
	 */
    Long batchUpdate(List<T> entitys);

	/**
	 * 生成多个属性的索引
	 * @param fieldNames 属性名列表
	 *
	 * @return 索引Name
	 */
    List<String> createIndex(List<String> fieldNames);

	/**
	 * 根据查询条件查找对象
	 *
	 * @param queryCond bson对象封装查询条件
	 * @return 数据列表
	 */
    List<T> findByCondition(Bson queryCond);

	/**
	 * 根据查询条件查找对象
	 * @param queryCond bson对象封装查询条件
	 * @param queryProperties  需要查询的实体类属性列表
	 *
	 * @return 数据列表
	 */
    List<T> findByCondition(Bson queryCond, List<String> queryProperties);

	/**
	 * 更新所有属性 包括entity中为null的字段 为null的字段将更新为空白字符
	 * @param entity 更新的实体对象
	 *
	 * @return 更新条数
	 */
    Long updateWithNull(T entity);

	/**
	 * 更新所有属性 不包括entity中为null的属性
	 *
	 * @param entity 更新的实体对象
	 * @return 更新条数
	 */
    Long updateNotWithNull(T entity);

	/**
	 * 根据Id更新dataMap数据
	 * @param id 主键id
	 *
	 * @param updateMap
	 * @return 更新条数
	 */
    Long updateById(PK id, Map<String, Object> updateMap);

	/**
	 * 根据Id集合更新dataMap数据
	 *
	 * @param ids 主键id集合 条件
	 * @param updateMap 更新的属性名与值
	 * @return 更新记录数
	 */
    Long updateByIds(List<PK> ids, Map<String, Object> updateMap);

	/**
	 * 更新所有属性
	 *
	 * @param id 主键id
	 * @param dataMap 需要更新的属性和属性对应的值
	 * @return 更新记录数
	 */
    Long updateByIdForDataMap(PK id, Map<String, Object> dataMap);

	/**
	 * 更新所有属性
	 * @param ids 主键id集合
	 * @param dataMap 需要更新的属性和属性对应的值
	 *
	 * @return 更新条数
	 */
    Long updateByIdsForDataMap(List<PK> ids, Map<String, Object> dataMap);

	/**
	 * 根据多个eq条件更新信息 and连接
	 * @param conditionMap 条件map
	 * @param updateMap  需要更新的属性和属性对应的值
	 *
	 * @return 更新记录数
	 */
    Long updateByParams(Map<String, Object> conditionMap, Map<String, Object> updateMap);

	/**
	 * 根据多个条件更新信息 and连接
	 * @param conditions 条件集合
	 * @param updateMap 更新的属性名和属性值
	 * @return 更新记录的条数
	 */
    Long updateByConditions(List<Condition> conditions, Map<String, Object> updateMap);


	/**
	 *  根据查询条件查询
	 * @param condition 查询条件
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return 数据列表
	 */
    List<T> findByCondition(Condition condition, List<String> queryProperties);

	/**
	 * 根据条件更新
	 *
	 * @param condition 更新条件
	 * @param updateMap 更新属性名与属性值
	 * @return 更新条数
	 */
    Long updateByCondition(Condition condition, Map<String, Object> updateMap);

	/**
	 * 根据条件删除
	 *
	 * @param condition 删除条件
	 * @return 删除条数
	 */
    Long deleteByCondition(Condition condition);

	/**
	 * 查询指定实体类的信息
	 *
	 * @param clazz 查询的实体类名
	 * @param condition 查询条件
	 * @return 数据列表
	 */
    <E> List<E> query(Class<E> clazz, Condition condition);

	/**
	 * 根据条件查询指定实体类
	 * @param clazz 查询的实体类名
	 * @param condition 查询条件
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return 数据列表
	 */
    <E> List<E> query(Class<E> clazz, Condition condition, List<String> queryProperties);

	/**
	 * 
	 * @param condition
	 *            查询条件
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
	 * @return 结果字符串列表
	 */
    List<String> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap, Integer skip
            , Integer limit);

	/**
	 *
	 *
	 * @param condition 查询条件
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
	 * @return 数据列表
	 */
    <E> List<E> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap, Integer skip,
                                Integer limit, Class<E> resultClass);

	/**
	 * 数据中有子集，对子集中的数据进行分组统计。
	 *
	 * @param matchCondition 查询条件
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
	 * @return 结果字符串列表
	 */
    List<String> aggregatesQuery(Condition matchCondition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
                                 Map<String, Integer> projectMap, Integer skip, Integer limit);

	/**
	 * 查询列表
	 *
	 * @param condition
	 *            查询条件 and连接
	 * @param unwindMap
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param page
	 * @return 分页数据
	 */
    PageInfo<T> findListByPage(Condition condition, Map<String, String> unwindMap, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
                               String groupJson, Page<T> page);

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
	 * @return 分页数据
	 */
    PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Map<String, Integer> projectMap, String groupJson, Page<T> page);

	/**
	 * 查询列表
	 *
	 * @param condition
	 *            查询条件 and连接
	 * @param unwindMap
	 * @param sortMap
	 *            排序 e.g key:name value:1或者-1 参见SortOperationalEnum
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param clazz
	 *            需要序列化的对象
	 * @return 分页数据
	 */
    <E> PageInfo<E> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap, Map<String,
            Integer> projectMap, Integer skip, Integer limit, Class<E> clazz);

	/**
	 * 分组总记录数
	 * 
	 * @param condition 查询条件
	 * @param unwindMap unwinmap的属性名与属性值
	 * @param groupJson 分组的原生mongoDB语句
	 * @return 记录数
	 */
    Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson);
}
