package org.opensourceframework.starter.mybatis.base.service;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * service层基础接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IBizBaseService<T , PK extends Serializable> {
	/**
	 * 根据Eo有值得属性为条件 查找对象列表
	 *
	 * @param queryEo 查询条件对象
	 * @return 对象列表
	 */
	List<T> findList(T queryEo);

	/**
	 * 根据id查找对象
	 *
	 * @param id 主键Id
	 * @return T
	 */
	T findById(PK id);

	/**
	 * 根据id列表查询数据列表
	 *
	 * @param idList 主键集合
	 * @return 数据列表
	 */
	List<T> findByIds(List<PK> idList);

	/**
	 * 根据id数组查询数据列表
	 *
	 * @param idArray 主键数组
	 * @return 数据列表
	 */
	List<T> findByIds(PK[] idArray);

	/**
	 * 根据queryEo的查询条件condition 查找对象列表
	 *
	 * @param queryEo 查询条件对象
	 * @return 数据列表
	 */
	List<T> findByCondition(T queryEo);

	/**
	 * 更新 eo中不为空的属性 条件为主键Id
	 *
	 * @param updateEo 更新条件对象
	 * @return  更新条数
	 */
	Integer updateNotNull(T updateEo);

	/**
	 * 更新eo中所有属性 条件为主键Id
	 *
	 * @param updateEo 更新条件对象
	 * @return  更新条数
	 */
	Integer updateWithNull(T updateEo);

	/**
	 * 更新eo中所有不为空属性 条件为对象中的SqlFilters
	 *
	 * @param whereEo 更新条件对象
	 * @return 更新条数
	 */
	Integer updateByCondition(T whereEo);

	/**
	 * 根据id删除  (物理删除)
	 *
	 * @param id 主键ID
	 * @return 删除条数
	 */
	Integer deleteById(PK id);

	/**
	 * 根据id删除  (逻辑删除 不真实删除,只是更新dr=1)
	 *
	 * @param id 主键Id
	 * @return 删除条数
	 */
	Integer deleteLogicById(PK id);

	/**
	 * 批量物理删除
	 *
	 * @param idArray 主键数组
	 * @return 删除条数
	 */
	Integer deleteByIds(PK[] idArray);

	/**
	 * 批量逻辑删除
	 *
	 * @param idArray 主键数组
	 * @return 删除条数
	 */
	Integer deleteLogicByIds(PK[] idArray);

	/**
	 * 批量逻辑删除
	 *
	 * @param whereEo 带condition的条件eo
	 * @return 删除条数
	 */
	Integer deleteByCondition(T whereEo);

	/**
	 * 插入数据
	 *
	 * @param eo 保存的数据对象
	 */
	void insert(T eo);

	/**
	 * 批量插入数据
	 *
	 * @param eoList 数据列表
	 */
	void insertBatch(List<T> eoList);

	/**
	 * 查询记录总数
	 *
	 * @param eo
	 * @return
	 */
	Integer count(T eo);

	/**
	 * 根据record不为null的属性和record中的SqlFilter为条件,分页查询
	 *
	 * @param record 查询条件对象
	 * @param currentPage 当前页码
	 * @param pageSize 每页数据条数
	 * @return 分页数据信息
	 */
	PageInfo<T> findPage(T record, Integer currentPage, Integer pageSize);
}
