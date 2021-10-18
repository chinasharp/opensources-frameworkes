package org.opensourceframework.component.dao.base;


import org.opensourceframework.base.eo.BaseEo;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Mybatis Mapper基类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface BaseMapper<T extends BaseEo , PK extends Serializable> {
	/**
	 * 保存
	 * @param t
	 * @return
	 */
	@InsertProvider(type = SqlTemplate.class, method = "insert")
	Integer insert(T t);

	/**
	 * 批量保存
	 * @param eoList
	 * @return
	 */
	@InsertProvider(type = SqlTemplate.class, method = "insertBatch")
	Integer insertBatch(@Param("eoList") List<T> eoList);

	/**
	 * 更新所有属性,包含对象中的为null的属性
	 * 
	 * @param t
	 * @return
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateWithNull")
	Integer updateWithNull(T t);

	/**
	 * 只更新值不为null的属性
	 *
	 * @param t
	 * @return
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateNotNull")
	Integer updateNotNull(T t);

	/**
	 * 根据Eo的SqlFilter为where条件 更新t中不为null的属性
	 *
	 * @param t
	 * @return
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateByCondition")
	Integer updateByCondition(T t);

	/**
	 * 物理删除
	 *
	 * @param clazz
	 * @param pkId
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteById")
	Integer deleteById(Class<?> clazz, PK pkId);

	/**
	 * 物理批量删除
	 *
	 * @param clazz
	 * @param pkIdArray
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteBatch")
	Integer deleteBatch(Class<?> clazz, PK[] pkIdArray);

	/**
	 * 逻辑删除(将dr更新为1)
	 *
	 * @param clazz
	 * @param pkId
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteLogicById")
	Integer deleteLogicById(Class<?> clazz, PK pkId);

	/**
	 * 批量逻辑删除(将dr更新为1)
	 *
	 * @param clazz
	 * @param pkIdArray
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteLogicBatch")
	Integer deleteLogicBatch(Class<?> clazz, PK[] pkIdArray);

	/**
	 * 物理删除
	 * 
	 * @param whereEo
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "delete")
	Integer delete(T whereEo);

	/**
	 * 逻辑删除
	 *
	 * @param whereEo
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteLogic")
	Integer deleteLogic(T whereEo);


	/**
	 * 根据Eo的SqlFilter为where条件删除
	 *
	 * @param whereEo
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteByCondition")
	Integer deleteByCondition(T whereEo);

	/**
	 * 根据主键Id查询记录(不包含已逻辑删除的记录)
	 *
	 * @param clazz
	 * @param pkId
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findById")
	T findById(Class<?> clazz, PK pkId);

	/**
	 * 根据主键Ids查询记录列表(不包含已逻辑删除的记录)
	 *
	 * @param clazz
	 * @param pkIds
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findByIds")
	List<T> findByIds(Class<?> clazz, PK[] pkIds);

	/**
	 * 根据主键Ids查询记录列表
	 *
	 * @param clazz
	 * @param pkIds
	 * @param isContainsDr 是否包含逻辑删除的数据
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findByIdsDr")
	List<T> findByIdsDr(Class<?> clazz, PK[] pkIds, Boolean isContainsDr);

	/**
	 * 根据id查询指定字段列表的值 不包含已逻辑删除的数据
	 *
	 * @param clazz
	 * @param pkId
	 * @param tableColumnNames
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findColumnById")
	T findColumnById(Class<?> clazz, PK pkId, String... tableColumnNames);

	/**
	 * 根据类名和主键Id 查询指定字段列表的值
	 *
	 * @param clazz 数据对应的类名
	 * @param pkIds  主键id数组
	 * @param tableColumnNames 需要返回的列名
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findColumnByIds")
	List<T> findColumnByIds(Class<?> clazz, PK[] pkIds, String... tableColumnNames);

	/**
	 * 查询
	 *
	 * @param t
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findOne")
	T findOne(T t);

	/**
	 * 根据eo中的SqlFilter的设置 查询符合条件的主键列表
	 *
	 * @param conditionEo
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findIdByCondition")
	List<Long> findIdByCondition(T conditionEo);

	/**
	 * 根据queryEo的查询条件condition 查找对象列表
	 *
	 * @param queryEo
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findByCondition")
	List<T> findByCondition(T queryEo);

	/**
	 * 根据T中的不为null的属性值查询  返回所有字段数据列表
	 *
	 * @param t
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findList")
	List<T> findList(T t);

	/**
	 * 根据t中不为空的属性值为条件查询 指定limit的数据
	 *
	 * @param t
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findPageList")
	List<T> findPageList(T t, Integer currentPage, Integer pageSize);

	/**
	 * 根据t中SqlFilter为条件查询 指定limit的数据
	 *
	 * @param t
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findPageListByCondition")
	List<T> findPageListByCondition(T t, Integer currentPage, Integer pageSize);

	/**
	 * 查询所有数据
	 *
	 * @param clazz
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findAll")
	List<T> findAll(Class<?> clazz);

	/**
	 * 查询clazz对应表的记录总数
	 *
	 * @param clazz
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "countByClazz")
	Integer countByClazz(Class<?> clazz);

	/**
	 * 根据t中不为空的属性为条件查询记录总数
	 *
	 * @param t
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "count")
	Integer count(T t);

	/**
	 * 根据Condition为条件查询记录总数
	 *
	 * @param t
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "countByCondition")
	Integer countByCondition(T t);

	/**
	 * 根据拆分键更新,不更新为null的属性
	 *
	 * @param eo
	 * @return
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateNotNullByShardingEo")
	Integer updateNotNullByShardingEo(T eo);

	/**
	 * 根据拆分键更新,更新所有属性,包含为空的属性
	 *
	 * @param eo
	 * @return
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateWithNullByShardingEo")
	Integer updateWithNullByShardingEo(T eo);

	/**
	 * 根据拆分键和其它属性值查询eo
	 *
	 * @param eo
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findListByShardingEo")
	List<T> findListByShardingEo(T eo , String... tableColumnNames);

	/**
	 * 根据拆分键查询数据
	 *
	 * @param shardingVal  拆分键值
	 * @param clazz        实体对象类名
	 * @param tableColumnNames   需要返回值的字段名
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findListByShardingKey")
	<E> List<E> findListByShardingKey(Object shardingVal , Class<E> clazz , String... tableColumnNames);

	/**
	 * 根据拆分键删除(物理删除)
	 *
	 * @param eo
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteByShardingEo")
	Integer deleteByShardingEo(T eo);


	/**
	 * 根据拆分键删除(物理删除)
	 *
	 * @param shardingVal
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteByShardingKey")
	Integer deleteByShardingKey(Object shardingVal , Class clazz);

	/**
	 * 根据拆分键删除(逻辑删除)
	 *
	 * @param eo
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteLogicByShardingEo")
	Integer deleteLogicByShardingEo(T eo);

	/**
	 * 根据拆分键删除(逻辑删除)
	 *
	 * @param shardingVal
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteLogicByShardingKey")
	Integer deleteLogicByShardingKey(Object shardingVal);

	/**
	 * 不带分表键的查询
	 *
	 * @param whereMap key为条件 value为条件值
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "findNotWithShardingKey")
	List<T> findNotWithShardingKey(Map<String , Object> whereMap);

	/**
	 * 不带分表键的查询
	 *
	 * @param paramMap 包含whereMap、updateMap
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "updateNotWithShardingKey")
	Integer updateNotWithShardingKey(Map<String , Object> paramMap);

	/**
	 * 不带分表键的查询
	 *
	 * @param whereMap key为条件 value为条件值
	 * @return
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteNotWithShardingKey")
	Integer deleteNotWithShardingKey(Map<String , Object> whereMap);
}

