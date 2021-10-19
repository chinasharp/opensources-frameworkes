package org.opensourceframework.component.dao.base;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.component.dao.contant.SqlStatementContant;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基础Dao实现类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public abstract class BaseDao<T extends BaseEo, PK extends Serializable> {
	protected Logger logger = LoggerFactory.getLogger(BaseDao.class);

	/**
	 * 获取Mybatis的所有Mapper
	 *
	 * @return Mybatis的所有Mapper
	 */
	public abstract Map<String, BaseMapper> getMappers();

	/**
	 * 获取当前T对应的Mapper
	 *
	 * @return
	 */
	public BaseMapper<T, PK> getMapper() {
		String tClassName = getTClassName();
		String mapperName = tClassName.substring(0, 1).toLowerCase() + tClassName.substring(1, tClassName.length() - 2) + "Mapper";
		BaseMapper<T, PK> mapper = getMappers().get(mapperName);
		if (null == mapper) {
			mapperName = tClassName.substring(0, tClassName.length() - 2) + "Mapper";
			mapper = getMappers().get(mapperName);
		}
		return mapper;
	}

	/**
	 * 获取当前Dao中的操作的实体类class
	 *
	 * @return
	 */
	public Class<T> getTClass() {
		Class entityClass = (Class) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return entityClass;
	}

	private String getTClassName() {
		return getTClass().getSimpleName();
	}

	private String getActualArgumentClassName(int i) {
		Class entityClass = (Class) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[i];
		return entityClass.getSimpleName();
	}

	/**
	 * 根据条件对象查询数据
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 列表数据
	 */
	public List<T> findList(T conditionEo ,  String[] tableColumnNames) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findList where:{}", JSON.toJSONString(conditionEo));
		}
		if(conditionEo == null){
			throw new BizException("condition eo is null");
		}

		conditionEo.setQueryTableColumnNames(tableColumnNames);
		return getMapper().findList(conditionEo);
	}

	/**
	 * 根据条件对象查询数据
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 列表数据
	 */
	public List<T> findList(T conditionEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findList where:{}", JSON.toJSONString(conditionEo));
		}
		return getMapper().findList(conditionEo);
	}

	/**
	 * 分页查询
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @param currentPage 当前页数
	 * @param pageSize  每页记录数
	 * @return 分页数据
	 */
	public PageInfo<T> findPage(T conditionEo, Integer currentPage, Integer pageSize) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findPage where:{} ,currentPage:{},pageSize:{} ", JSON.toJSONString(conditionEo), currentPage, pageSize);
		}
		currentPage = null == currentPage ? SqlStatementContant.DEFAULT_PAGE_NUMBER : currentPage;
		pageSize = null == pageSize ? SqlStatementContant.DEFAULT_PAGE_SIZE : pageSize;
		PageHelper.startPage(currentPage.intValue(), pageSize.intValue());
		List<T> list = getMapper().findList(conditionEo);
		return new PageInfo<T>(list);
	}

	/**
	 * 分页查询并缓存查询结果(目前未实现)
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @param currentPage 当前页数
	 * @param pageSize  每页记录数
	 * @param cacheable 是否缓存
	 * @return
	 */
	private PageInfo<T> findPage(T conditionEo, Integer currentPage, Integer pageSize, Boolean cacheable) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findPage where:{} ,currentPage , pageSize , cacheable", JSON.toJSONString(conditionEo), currentPage, pageSize, cacheable);
		}
		return findPage(conditionEo, currentPage, pageSize);
	}

	/**
	 * 默认分页查询每页10条记录,只查询第1页数据
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 分页数据对象
	 */
	public PageInfo<T> findPage(T conditionEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findById where:{}", JSON.toJSONString(conditionEo));
		}
		return findPage(conditionEo, SqlStatementContant.DEFAULT_PAGE_NUMBER, SqlStatementContant.DEFAULT_PAGE_SIZE);
	}

	/**
	 * 根据主键id查询
	 *
	 * @param pkId 主键id
	 * @return id对应的数据对象
	 */
	public T findById(PK pkId) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findById pkId:{}", pkId);
		}
		T record = getMapper().findById(getTClass(), pkId);
		return record;
	}

	/**
	 * 根据主键数组查询
	 *
	 * @param idArray 主键数组
	 * @return 列表数据
	 */
	public List<T> findByIds(PK[] idArray){
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findById idArray:{}", idArray);
		}
		List<T> eoList = getMapper().findByIds(getTClass() , idArray);
		return eoList;
	}

	/**
	 * 根据条件对象查询
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 满足条件的对象
	 */
	public T findOne(T conditionEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findOne whereEo:{}", JSON.toJSONString(conditionEo));
		}
		return getMapper().findOne(conditionEo);
	}

	/**
	 * 根据eo中不为空的属性值为条件 查询指定字段列表的值
	 *
	 * @param conditionEo
	 * @param tableColumnNames
	 * @return
	 */
	public T findOne(T conditionEo, String... tableColumnNames){
		if(conditionEo == null){
			throw new BizException("condition eo is null");
		}
		conditionEo.setQueryTableColumnNames(tableColumnNames);
		return getMapper().findOne(conditionEo);
	}

	/**
	 * 根据条件对象查询总条数
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 总记录数
	 */
	public Integer count(T conditionEo) {
		if (logger.isDebugEnabled()) {
			this.logger.debug("exec count whereEo:{}", JSON.toJSONString(conditionEo));
		}
		return getMapper().count(conditionEo);
	}

	/**
	 * 保存
	 *
	 * @param record 保存的实体对象
	 * @return 保存
	 */
	public Integer insert(T record) {
		if(record.getCreateTime() == null){
			record.setCreateTime(new Date());
		}

		if(record.getUpdateTime() == null){
			record.setUpdateTime(new Date());
		}
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec insert record {}", JSON.toJSONString(record));
		}
		return getMapper().insert(record);
	}

	/**
	 * 以updateEo中的id为条件,更新updateEo中不为空的属性值
	 *
	 * @param updateEo 更新对象
	 * @return 更新条数
	 */
	public Integer updateNotNull(T updateEo) {
		if (logger.isDebugEnabled()) {
			this.logger.debug("exec updateNotNull record:{}", JSON.toJSONString(updateEo));
		}
		int updateRows = getMapper().updateNotNull(updateEo);
		return updateRows;
	}

	/**
	 * 以updateEo中的id为条件,更新updateEo中的所有的属性值(包含为空的属性)
	 *
	 * @param updateEo 更新对象
	 * @return 更新条数
	 */
	public Integer updateWithNull(T updateEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec updateWithNull record:{}", JSON.toJSONString(updateEo));
		}
		int updateRows = getMapper().updateWithNull(updateEo);
		return updateRows;
	}

	/**
	 * 根据updateEo中的Condition条件 更新updateEo中不为空的属性值
	 *
	 * @param updateEo
	 * @return
	 */
	public Integer updateByCondition(T updateEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec condition record:{}", JSON.toJSONString(updateEo));
		}
		int updateRows = getMapper().updateByCondition(updateEo);
		return updateRows;
	}

	/**
	 * 根据conditionEo中不为空的属性和Condition为条件删除(物理删除)
	 *
	 * @param conditionEo 删除条件对象
	 * @return 删除条数
	 */
	public Integer delete(T conditionEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec delete record:{}", JSON.toJSONString(conditionEo));
		}
		return getMapper().delete(conditionEo);
	}

	/**
	 * 根据conditionEo中不为空的属性和Condition为条件删除(逻辑删除)
	 *
	 * @param conditionEo 删除条件对象
	 * @return 删除条数
	 */
	public Integer deleteLogic(T conditionEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec logicDelete record:{}", JSON.toJSONString(conditionEo));
		}
		return getMapper().deleteLogic(conditionEo);
	}

	/**
	 * 根据主键id删除(物理删除)
	 *
	 * @param id 主键id
	 * @return 删除条数
	 */
	public Integer deleteById(PK id) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec deleteById id:{}", id);
		}
		return getMapper().deleteById(getTClass(), id);
	}

	/**
	 * 根据主键id删除(逻辑删除)
	 *
	 * @param id 主键id
	 * @return 删除条数
	 */
	public Integer deleteLogicById(PK id) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec logicDeleteById id:{}", id);
		}
		return getMapper().deleteLogicById(getTClass(), id);
	}

	/**
	 * 根据whereEo中的Condition删除
	 *
	 * @param whereEo Condition条件对象
	 * @return 删除条数
	 */
	public Integer deleteByCondition(T whereEo) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec deleteByCondition condition:{}", whereEo.getCondition());
		}
		return getMapper().deleteByCondition(whereEo);
	}

	/**
	 * 根据主键id数组批量删除(物理删除)
	 *
	 * @param pkIdArray 主键id数组
	 * @return 删除条数
	 */
	public Integer deleteBatch(PK[] pkIdArray) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec deleteBatch idArray:{}", pkIdArray);
		}
		int delCount = 0;
		if (pkIdArray != null && pkIdArray.length > 0) {
			delCount = getMapper().deleteBatch(getTClass(), pkIdArray);
		}
		return delCount;
	}

	/**
	 * 根据主键id数组批量删除(逻辑删除)
	 *
	 * @param pkIdArray 主键id数组
	 * @return 删除条数
	 */
	public Integer deleteLogicBatch(PK[] pkIdArray) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec deleteLogicBatch idArray:{}", pkIdArray);
		}
		int delCount = 0;
		if (pkIdArray != null && pkIdArray.length > 0) {
			delCount = getMapper().deleteLogicBatch(getTClass(), pkIdArray);
		}
		return delCount;
	}

	/**
	 * 批量保存数据
	 *
	 * @param eoList 保存的对象列表
	 * @return 保存后的数据
	 */
	public List<T> insertBatch(List<T> eoList) {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec insertBatch eoList:{}", JSON.toJSONString(eoList));
		}
		if(CollectionUtils.isNotEmpty(eoList)){
			eoList.forEach(record ->{
				if(record.getCreateTime() == null){
					record.setCreateTime(new Date());
				}
				if(record.getUpdateTime() == null){
					record.setUpdateTime(new Date());
				}
			});
		}
		getMapper().insertBatch(eoList);
		return eoList;
	}

	/**
	 * 返回Eo集合的 id集合
	 *
	 * @param tList eo集合
	 * @return id集合
	 */
	private List<Long> getIds(List<T> tList) {
		List<Long> ids = new ArrayList();
		for (T t : tList) {
			ids.add(t.getId());
		}
		return ids;
	}

	/**
	 * 查询全部
	 *
	 * @return 数据列表
	 */
	public List<T> findAll() {
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findAll records");
		}
		return getMapper().findAll(getTClass());
	}

	/**
	 * 根据queryEo的查询条件condition 查找对象列表
	 *
	 * @param queryEo 查询条件对象
	 * @return 数据列表
	 */
	public List<T> findByCondition(T queryEo){
		if(logger.isDebugEnabled()) {
			this.logger.debug("exec findByCondition Condition:{}", queryEo.getCondition());
		}
		return getMapper().findByCondition(queryEo);
	}

	/**
	 * 根据类名和主键Id 删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkId    主键id
	 * @return
	 */
	public Integer deleteById(Class<?> clazz, PK pkId){
		return getMapper().deleteById(clazz  ,pkId);
	}

	/**
	 * 根据类名和主键Id数组 批量删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkIdArray    主键id数组
	 * @return 删除条数
	 */
	public Integer deleteBatch(Class<?> clazz, PK[] pkIdArray){
		return getMapper().deleteBatch(clazz  ,pkIdArray);
	}

	/**
	 * 根据类名和主键Id 逻辑删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkId    主键id
	 * @return 删除条数
	 */
	public Integer deleteLogicById(Class<?> clazz, PK pkId){
		return getMapper().deleteLogicById(clazz  ,pkId);
	}

	/**
	 * 根据类名和主键Id数组 批量逻辑删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkIdArray    主键id数组
	 * @return 删除条数
	 */
	public Integer deleteLogicBatch(Class<?> clazz, PK[] pkIdArray){
		return getMapper().deleteLogicBatch(clazz  ,pkIdArray);
	}

	/**
	 * 根据类名和主键Id 查询数据
	 *
	 * @param clazz 数据对应的类名
	 * @param pkId  主键id
	 * @return
	 */
	public T findById(Class<?> clazz, PK pkId){
		return getMapper().findById(clazz  ,pkId);
	}

	/**
	 * 根据类名和主键Id数组 查询数据
	 *
	 * @param clazz 数据对应的类名
	 * @param pkIds  主键id数组
	 * @return
	 */
	public List<T> findByIds(Class<?> clazz, PK[] pkIds){
		return getMapper().findByIds(clazz  ,pkIds);
	}

	/**
	 * 根据类名和主键Id数组 查询数据
	 *
	 * @param clazz
	 * @param pkIds
	 * @param isContainsDr 是否包含已逻辑删除的数据
	 * @return
	 */
	public List<T> findByIdsDr(Class<?> clazz, PK[] pkIds, Boolean isContainsDr){
		return getMapper().findByIdsDr(clazz  , pkIds ,isContainsDr);
	}

	/**
	 * 根据类名和主键Id 查询指定字段列表的值
	 *
	 * @param clazz 数据对应的类名
	 * @param pkId  主键id
	 * @param tableColumnNames 需要返回的列名
	 * @return
	 */
	public T findColumnById(Class<?> clazz, PK pkId, String... tableColumnNames){
		return getMapper().findColumnById(clazz  , pkId ,tableColumnNames);
	}

	/**
	 * 根据类名和主键Id 查询指定字段列表的值
	 *
	 * @param clazz 数据对应的类名
	 * @param pkIds  主键id数组
	 * @param tableColumnNames 需要返回的列名
	 * @return
	 */
	public List<T> findColumnByIds(Class<?> clazz, PK[] pkIds, String... tableColumnNames){
		return getMapper().findColumnByIds(clazz  , pkIds ,tableColumnNames);
	}

	/**
	 * 根据T中的不为null的属性值查询 只返回tableColumnName对应的数据列表
	 *
	 * @param t
	 * @return
	 */
	public List<T> findListColumn(T t, String... tableColumnNames){
		return null;
		//return getMapper().findListColumn( t , tableColumnNames);
	}

	/**
	 * 查询clazz对应表的记录总数
	 *
	 * @param clazz
	 * @return
	 */
	public Integer countByClazz(Class<?> clazz){
		return getMapper().countByClazz(clazz);
	}
}
