package org.opensourceframework.starter.mybatis.base.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 基本service实现类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *  
 */
public abstract class BizBaseServiceImpl<T extends BaseEo, PK extends Serializable> implements IBizBaseService<T , PK> {

	/**
	 * 获取操作的Dao Bean
	 *
	 * @return dao的操作bean
	 */
	public abstract BizBaseDao<T , PK> getBizDao();

	/**
	 * 根据Eo有值得属性为条件 查找对象列表
	 *
	 * @param queryEo 查询条件对象
	 * @return T对象列表
	 */
	@Override
	public List<T> findList(T queryEo) {
		return getBizDao().findList(queryEo);
	}

	/**
	 * 根据id查找对象
	 *
	 * @param id 主键Id
	 * @return T
	 */
	@Override
	public T findById(PK id) {
		return getBizDao().findById(id);
	}

	/**
	 * 根据queryEo的查询条件condition 查找对象列表
	 *
	 * @param queryEo 查询条件对象
	 * @return 数据列表
	 */
	@Override
	public List<T> findByCondition(T queryEo){
		return getBizDao().findByCondition(queryEo);
	}

	/**
	 * 更新 eo中不为空的属性 条件为主键Id
	 *
	 * @param updateEo 更新条件对象
	 * @return 更新条数
	 */
	@Override
	public Integer updateNotNull(T updateEo) {
		return getBizDao().updateNotNull(updateEo);
	}

	/**
	 * 更新eo中所有属性 条件为主键Id
	 *
	 * @param updateEo 更新条件对象
	 * @return 更新条数
	 */
	@Override
	public Integer updateWithNull(T updateEo) {
		return getBizDao().updateWithNull(updateEo);
	}

	/**
	 * 更新eo中所有不为空属性 条件为对象中的Condition
	 *
	 * @param whereEo 包含Condition更新条件,whereEo中有值的属性为set
	 * @return 更新条数
	 */
	@Override
	public Integer updateByCondition(T whereEo) {
		return getBizDao().updateByCondition(whereEo);
	}

	/**
	 * 根据id删除  (物理删除)
	 *
	 * @param id 主键id
	 * @return 删除条数
	 */
	@Override
	public Integer deleteById(PK id) {
		return getBizDao().deleteById(id);
	}

	/**
	 * 根据id删除  (逻辑删除)
	 *
	 * @param id 主键id
	 * @return 删除条数
	 */
	@Override
	public Integer deleteLogicById(PK id) {
		return getBizDao().deleteLogicById(id);
	}

	/**
	 * 批量物理删除
	 *
	 * @param idArray 主键数组
	 * @return 删除条数
	 */
	@Override
	public Integer deleteByIds(PK[] idArray) {
		return getBizDao().deleteBatch(idArray);
	}

	/**
	 * 批量逻辑删除
	 *
	 * @param idArray 主键数组
	 * @return 删除条数
	 */
	@Override
	public Integer deleteLogicByIds(PK[] idArray) {
		return getBizDao().deleteLogicBatch(idArray);
	}

	/**
	 * 根据Eo的Condition删除
	 *
	 * @param whereEo 包含有condition的条件eo
	 * @return 删除条数
	 */
	@Override
	public Integer deleteByCondition(T whereEo) {
		return getBizDao().deleteByCondition(whereEo);
	}

	/**
	 * 插入数据
	 *
	 * @param eo 需要保存的eo对象
	 */
	@Override
	public void insert(T eo) {
		getBizDao().insert(eo);
	}

	/**
	 * 批量插入数据
	 *
	 * @param eoList 对象列表
	 */
	@Override
	public void insertBatch(List<T> eoList) {
		getBizDao().insertBatch(eoList);
	}

	/**
	 * 根据record不为null的属性和record中的Condition为条件,分页查询
	 *
	 * @param record record对象中有值属性和Condition为查询条件
	 * @param currentPage 当前页码
	 * @param pageSize    每页数据条数
	 * @return T的分页数据对象
	 */
	@Override
	public PageInfo<T> findPage(T record, Integer currentPage, Integer pageSize) {
		return getBizDao().findPage(record , currentPage , pageSize);
	}

	/**
	 * 根据id数组查询数据列表
	 *
	 * @param idArray 主键数组
	 * @return 数据列表
	 */
	@Override
	public List<T> findByIds(PK[] idArray) {
		List<T> eoList = Lists.newArrayList();
		if(idArray != null && idArray.length > 0){
			eoList = getBizDao().findByIds(idArray);
		}
		return eoList;
	}

	@Override
	public List<T> findByIds(List<PK> idList) {
		List<T> list = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(idList)) {
			list = findByIds((PK[])idList.toArray());
		}
		return list;
	}

	/**
	 * 根据条件对象查询总条数
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 总记录数
	 */
	@Override
	public Integer count(T conditionEo) {
		return getBizDao().count(conditionEo);
	}
}
