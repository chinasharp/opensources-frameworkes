package org.opensourceframework.starter.mybatis.base.dao;

import com.google.common.collect.Lists;
import org.opensourceframework.base.db.Condition;
import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.base.helper.ReflectHelper;
import org.opensourceframework.component.dao.algorithm.HashModTable;
import org.opensourceframework.component.dao.algorithm.HashingTable;
import org.opensourceframework.component.dao.annotation.ShardingTable;
import org.opensourceframework.component.dao.contant.SqlStatementContant;
import org.opensourceframework.starter.mybatis.base.threadquery.MultiHashTableExecutor;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分表基类Dao
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseShardingDao<T extends BaseEo , PK extends Serializable> extends BizBaseDao<T ,PK> {
	/**
	 * 实体版本号,当改变DTO的时候通过版免序列化异常问题
	 */
	protected final static String POJO_VERSION = ":2.0:";

	private static final int HASH_MOD = 1;
	private static final int HASH_HASHING = 2;

	@Override
	public List<T> insertBatch(List<T> records) {
		Class<?> eoClass = getEoClass();
		if(eoClass.isAnnotationPresent(ShardingTable.class) && eoClass.isAnnotationPresent(Table.class)){
			String maintable = eoClass.getAnnotation(Table.class).name();
			ShardingTable rdsSharding = eoClass.getAnnotation(ShardingTable.class);
			int algorithm = rdsSharding.algorithm();
			int tableTotal = rdsSharding.tableCount();
			String shardingProperty = rdsSharding.property();
			if(StringUtils.isEmpty(shardingProperty)){
				throw new RuntimeException("not set sharding property for " + eoClass.getName());
			}

			Map<String , List<T>> tableMap = new HashMap<>();
			for(T eo : records){
				String subTableName = null;
				Object splitValue = ReflectHelper.getFieldValue(eo , shardingProperty);
				if (splitValue != null) {
					//有查分键的,使用hash算法得出子表名
					if(HASH_MOD == algorithm) {
						//hash mod算法
						subTableName = HashModTable.getSplitTableName(maintable, splitValue, tableTotal);
					}else{
						//hashing算法
						subTableName = HashingTable.getSplitTableName(maintable, splitValue, tableTotal);
					}
					List<T> eoList = tableMap.get(subTableName);
					boolean isPutListToMap = false;
					if(eoList == null){
						eoList = new ArrayList<>();
						isPutListToMap = true;
					}
					eoList.add(eo);
					if (isPutListToMap) {
						tableMap.put(subTableName , eoList);
					}
				}else{
					throw new RuntimeException("sharding property's value is null, property:" + shardingProperty);
				}
			}
			for(Map.Entry<String , List<T>> entry : tableMap.entrySet()){
				super.insertBatch(entry.getValue());
			}
			return records;
		}else {
			return super.insertBatch(records);
		}
	}

	/**
	 * 获取Eo class
	 *
	 * @param
	 * @return
	 */
	private Class<T> getEoClass() {
		Class entityClass = getGenericClass();
		return entityClass;
	}

	/**
	 * 获取泛型类型，如果不存在则返回null
	 *
	 * @param
	 * @return
	 */
	private Class<T> getGenericClass() {
		Type t = this.getClass().getGenericSuperclass();
		if(t == null){
			t = this.getClass().getGenericInterfaces()[0];
		}
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			return (Class<T>) p[0];
		}
		return null;
	}

	/**
	 * 根据拆分键和其它属性值查询eo
	 *
	 * @param queryEo 查询参数对象
	 * @return 列表数据
	 */
	public List<T> findListByShardingEo(T queryEo){
		return super.findList(queryEo);
	}

	/**
	 * 根据拆分键和其它属性值查询eo
	 *
	 * @param queryEo 查询参数对象
	 * @param tableColumnNames 需要返回数据列
	 * @return 列表数据
	 */
	public List<T> findListByShardingEo(T queryEo , String[] tableColumnNames){
		return super.findListColumn(queryEo , tableColumnNames);
	}

	/**
	 * 根据拆分键查询数据
	 *
	 * @param shardingValue  拆分键值
	 * @return  列表数据
	 */
	public List<T> findListByShardingKey(Object shardingValue){
		return this.findListByShardingKey(shardingValue , null);
	}

	/**
	 * 根据拆分键查询数据
	 *
	 * @param shardingValue      拆分键值
	 * @param tableColumnNames   需要返回值的字段名
	 * @return  列表数据
	 */
	public List<T> findListByShardingKey(Object shardingValue ,  String[] tableColumnNames){
		try {
			String shardingProperty = getShardingProperty(getEoClass());
			T t = getEoClass().newInstance();
			ReflectHelper.setFieldValue(t , shardingProperty , shardingValue);
			t.setQueryTableColumnNames(tableColumnNames);
			return super.findList(t);
		}catch(Exception e){
			throw new BizException(e.getMessage());
		}
	}

	/**
	 * 根据拆分键和Eo中不为空的属性为条件删除(物理删除)
	 *
	 * @param eo
	 * @return
	 */
	public int deleteByShardingEo(T eo){
		String shardingProperty = getShardingProperty(getEoClass());
		Object shardingValue = ReflectHelper.getFieldValue(eo , shardingProperty);
		if(shardingValue == null){
			throw new BizException("deleteByShardingEo error. sharding key's value is null");
		}
		return getMapper().deleteByShardingEo(eo);
	}

	/**
	 * 根据拆分键删除(物理删除)
	 *
	 * @param shardingValue 拆分键值
	 * @return
	 */
	public int deleteByShardingKey(Object shardingValue){
		if(shardingValue == null){
			throw new BizException("deleteByShardingKey error. sharding key's value is null");
		}

		try {
			String shardingProperty = getShardingProperty(getEoClass());
			T t = getEoClass().newInstance();
			ReflectHelper.setFieldValue(t , shardingProperty , shardingValue);
			return delete(t);
		}catch(Exception e){
			throw new BizException(e.getMessage());
		}
	}

	private String getShardingProperty(Class<?> clazz){
		if(!clazz.isAnnotationPresent(ShardingTable.class)){
			throw new BizException("not found @ShardingTable for this class. class's name:" + clazz.getName());
		}
		ShardingTable shardingTable = clazz.getAnnotation(ShardingTable.class);
		String shardingProperty = shardingTable.property();
		if(StringUtils.isBlank(shardingProperty)){
			throw new BizException("not found @ShardingTable's property for class:" +  clazz.getName());
		}
		return shardingProperty;
	}

	/**
	 * 根据拆分键删除(逻辑删除)
	 *
	 * @param eo
	 * @return
	 */
	public int deleteLogicByShardingEo(T eo){
		return getMapper().deleteLogicByShardingEo(eo);
	}

	/**
	 * 根据拆分键删除(逻辑删除)
	 *
	 * @param shardingValue 拆分键值
	 * @return
	 */
	public int deleteLogicByShardingKey(Object shardingValue){
		if(shardingValue == null){
			throw new BizException("deleteLogicByShardingKey error. sharding key's value is null");
		}

		try {
			String shardingProperty = getShardingProperty(getEoClass());
			T t = getEoClass().newInstance();
			ReflectHelper.setFieldValue(t , shardingProperty , shardingValue);
			return deleteLogicByShardingEo(t);
		}catch(Exception e){
			throw new BizException(e.getMessage());
		}
	}

	/**
	 * 根据拆分键更新,不更新为null的属性
	 *
	 * @param eo
	 * @return
	 */
	public Integer updateNotNullByShardingEo(T eo){
		return getMapper().updateNotNullByShardingEo(eo);
	}

	/**
	 * 根据拆分键更新,更新所有属性,包含为空的属性
	 *
	 * @param eo
	 * @return
	 */
	public Integer updateWithNullByShardingEo(T eo){
		return getMapper().updateWithNullByShardingEo(eo);
	}


	private List<T> findNotWithShardingKey(Map<String , Object> whereMap){
		if(MapUtils.isNotEmpty(whereMap)){
			return getMapper().findNotWithShardingKey(whereMap);
		}else{
			logger.warn("exec findNotWithShardingKey:whereMap is empty.");
			return Lists.newArrayList();
		}
	}

	/**
	 * 根据paramMap 不带拆分键的查询
	 *
	 * @param paramMap  逻辑关系为and
	 * @return
	 */
	private Integer updateNotWithShardingKey(Map<String , Object> paramMap){
		Integer updateCount = 0;
		Map<String , Object> whereMap = (Map<String , Object>)paramMap.get(SqlStatementContant.WHERE_MAP_KEY);
		Map<String , Object> updateMap = (Map<String , Object>)paramMap.get(SqlStatementContant.UPDATE_MAP_KET);
		if(MapUtils.isNotEmpty(whereMap) && MapUtils.isNotEmpty(updateMap)){
			updateCount = getMapper().updateNotWithShardingKey(paramMap);
		}else{
			logger.warn("exec updateNotWithShardingKey:whereMap is empty or updateMap is empty.");
		}
		if(updateCount == null){
			updateCount = 0;
		}else{
			logger.info("updateCount:{}" , updateCount);
		}
		return updateCount;
	}

	/**
	 * 根据paramMap 不带拆分建删除
	 *
	 * @param whereMap
	 * @return
	 */
	private Integer deleteNotWithShardingKey(Map<String , Object> whereMap){
		Integer deleteCount = 0;
		if(MapUtils.isNotEmpty(whereMap)){
			deleteCount =  getMapper().deleteNotWithShardingKey(whereMap);
		}else{
			logger.warn("exec deleteNotWithShardingKey:whereMap is empty.");
		}
		return deleteCount;
	}

	/**
	 * 不带分表键的分表查询
	 *
	 * @param whereMap key为条件 value为条件值
	 * @return
	 */
	public List<T> findShardingDataNoShardingKey(Map<String , Object> whereMap){
		if(MapUtils.isEmpty(whereMap)){
			throw new BizException("exec findShardingDataNoShardingKey error. whereMap is empty");
		}
		MultiHashTableExecutor multiHashTableExecutor = new MultiHashTableExecutor(this);
		List<T> eoList = multiHashTableExecutor.execSelect(getEoClass() , whereMap);
		return eoList;
	}

	/**
	 * 不带分表键的分表查询
	 *
	 * @param condition 条件对象
	 * @param limit 限制条数 默认500
	 * @return
	 */
	public List<T> findShardingDataNoShardingKey(Condition condition , Integer limit){
		if(condition == null){
			throw new BizException("exec findShardingDataNoShardingKey error. condition is empty");
		}
		MultiHashTableExecutor multiHashTableExecutor = new MultiHashTableExecutor(this);
		List<T> eoList = multiHashTableExecutor.execSelect(getEoClass() , condition , limit);
		return eoList;
	}

	/**
	 * 不带分表键的分表更新
	 *
	 * @param whereMap   key为条件 value为条件值
	 * @param updateMap  更新属性  value为更新值
	 * @return
	 */
	public Integer updateShardingDataNoShardingKey(Map<String , Object> whereMap , Map<String ,Object> updateMap){
		if(MapUtils.isEmpty(whereMap)){
			throw new BizException("exec updateShardingDataNoShardingKey error. whereMap is empty");
		}
		MultiHashTableExecutor multiHashTableExecutor = new MultiHashTableExecutor(this);

		List<Integer> countList =  multiHashTableExecutor.execUpdate(getEoClass() , whereMap ,updateMap);
		Long count = countList.stream().collect(Collectors.summingInt(value -> value)).longValue();
		return count.intValue();
	}

	/**
	 * 不带分表键的分表删除
	 *
	 * @param whereMap key为条件 value为条件值
	 * @return
	 */
	public Integer deleteShardingDataNoShardingKey(Map<String , Object> whereMap){
		if(MapUtils.isEmpty(whereMap)){
			throw new BizException("exec findShardingDataNoShardingKey error. whereMap is empty");
		}
		MultiHashTableExecutor multiHashTableExecutor = new MultiHashTableExecutor(this);
		List<Integer> countList = multiHashTableExecutor.execDelate(getEoClass() , whereMap);
		Long deleteCount = countList.stream().collect(Collectors.summingInt(value -> value)).longValue();
		return deleteCount.intValue();
	}

	/**
	 * 不带分表键的分表删除
	 *
	 * @param condition 条件对象
	 * @return
	 */
	public Integer deleteShardingDataNoShardingKey(Condition condition){
		if(condition == null){
			throw new BizException("exec deleteShardingDataNoShardingKey error. condition is empty");
		}
		MultiHashTableExecutor multiHashTableExecutor = new MultiHashTableExecutor(this);

		List<Integer> countList =  multiHashTableExecutor.execDelate(getEoClass() , condition);
		Long deleteCount = countList.stream().collect(Collectors.summingInt(value -> value)).longValue();
		return deleteCount.intValue();
	}
}
