package org.opensourceframework.component.es.base;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.constants.CommonCanstant;
import org.opensourceframework.component.es.helper.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础ES dao
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/25 下午1:42
 */
public abstract class BaseEsDao<T extends BaseEsEo, PK extends Serializable>  {
	private static final Logger logger = LoggerFactory.getLogger(BaseEsDao.class);

	protected abstract ESTemplate getESTemplate();

	/**
	 * 根据条件查询数据
	 *
	 * @param queryParams
	 * @return
	 */
	public List<T> findList(Map<String , Object> queryParams){
		return getESTemplate().findByParams(getTClass() , queryParams);
	}

	/**
	 * 根据条件对象查询数据
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 列表数据
	 */
	private List<T> findList(T conditionEo ,  String[] tableColumnNames) {
		return null;
	}

	/**
	 * 根据条件对象查询数据
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 列表数据
	 */
	private List<T> findList(T conditionEo) {
		return null;
	}

	public PageInfo<T> findPage(Map<String , Object> queryParams , Integer currentPage, Integer pageSize){
		currentPage = null == currentPage ? CommonCanstant.DEFAULT_PAGE_NUMBER : currentPage;
		pageSize = null == pageSize ? CommonCanstant.DEFAULT_PAGE_SIZE : pageSize;
		List<T> tList = getESTemplate().findByPage(getTClass() , queryParams , (currentPage -1) * pageSize , pageSize);
		long count = getESTemplate().findCount(getTClass() , queryParams);
		return buildPageInfo(tList , currentPage , pageSize , count);
	}

	/**
	 * 分页查询
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @param currentPage 当前页数
	 * @param pageSize  每页记录数
	 * @return 分页数据
	 */
	private PageInfo<T> findPage(T conditionEo, Integer currentPage, Integer pageSize) {
		if(logger.isDebugEnabled()) {
			logger.debug("exec findPage where:{} ,currentPage:{},pageSize:{} ", JSON.toJSONString(conditionEo), currentPage, pageSize);
		}
		currentPage = null == currentPage ? CommonCanstant.DEFAULT_PAGE_NUMBER : currentPage;
		pageSize = null == pageSize ? CommonCanstant.DEFAULT_PAGE_SIZE : pageSize;
		List<T> list = getESTemplate().findList(conditionEo);
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
		return null;
	}

	/**
	 * 默认分页查询每页10条记录,只查询第1页数据
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 分页数据对象
	 */
	private PageInfo<T> findPage(T conditionEo) {
		return null;
	}

	/**
	 * 根据主键id查询
	 *
	 * @param pkId 主键id
	 * @return id对应的数据对象
	 */
	private T findById(PK pkId) {
		return null;
	}

	/**
	 * 根据主键数组查询
	 *
	 * @param idArray 主键数组
	 * @return 列表数据
	 */
	private List<T> findByIds(PK[] idArray){
		return null;
	}

	/**
	 * 根据条件对象查询
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 满足条件的对象
	 */
	private T findOne(T conditionEo) {
		return null;
	}

	/**
	 * 根据eo中不为空的属性值为条件 查询指定字段列表的值
	 *
	 * @param conditionEo
	 * @param tableColumnNames
	 * @return
	 */
	private T findOne(T conditionEo, String... tableColumnNames){
		return null;
	}

	/**
	 * 根据条件对象查询总条数
	 *
	 * @param conditionEo 条件对象,查询条件为eo中值不为空的属性和Confition属性
	 * @return 总记录数
	 */
	private Integer count(T conditionEo) {
		return null;
	}

	/**
	 * 保存
	 *
	 * @param record 保存的实体对象
	 * @return 保存
	 */
	private Integer insert(T record) {
		return null;
	}

	/**
	 * 以updateEo中的id为条件,更新updateEo中不为空的属性值
	 *
	 * @param updateEo 更新对象
	 * @return 更新条数
	 */
	private Integer updateNotNull(T updateEo) {
		return null;
	}

	/**
	 * 以updateEo中的id为条件,更新updateEo中的所有的属性值(包含为空的属性)
	 *
	 * @param updateEo 更新对象
	 * @return 更新条数
	 */
	private Integer updateWithNull(T updateEo) {
		return null;
	}

	/**
	 * 根据updateEo中的Condition条件 更新updateEo中不为空的属性值
	 *
	 * @param updateEo
	 * @return
	 */
	private Integer updateByCondition(T updateEo) {
		return null;
	}

	/**
	 * 根据conditionEo中不为空的属性和Condition为条件删除(物理删除)
	 *
	 * @param conditionEo 删除条件对象
	 * @return 删除条数
	 */
	private Integer delete(T conditionEo) {
		return null;
	}

	/**
	 * 根据conditionEo中不为空的属性和Condition为条件删除(逻辑删除)
	 *
	 * @param conditionEo 删除条件对象
	 * @return 删除条数
	 */
	private Integer deleteLogic(T conditionEo) {
		return null;
	}

	/**
	 * 根据主键id删除(物理删除)
	 *
	 * @param id 主键id
	 * @return 删除条数
	 */
	private Integer deleteById(PK id) {
		return null;
	}

	/**
	 * 根据主键id删除(逻辑删除)
	 *
	 * @param id 主键id
	 * @return 删除条数
	 */
	private Integer deleteLogicById(PK id) {
		return null;
	}

	/**
	 * 根据whereEo中的Condition删除
	 *
	 * @param whereEo Condition条件对象
	 * @return 删除条数
	 */
	private Integer deleteByCondition(T whereEo) {
		return null;
	}

	/**
	 * 根据主键id数组批量删除(物理删除)
	 *
	 * @param pkIdArray 主键id数组
	 * @return 删除条数
	 */
	private Integer deleteBatch(PK[] pkIdArray) {
		return null;
	}

	/**
	 * 根据主键id数组批量删除(逻辑删除)
	 *
	 * @param pkIdArray 主键id数组
	 * @return 删除条数
	 */
	private Integer deleteLogicBatch(PK[] pkIdArray) {
		return null;
	}

	/**
	 * 批量保存数据
	 *
	 * @param eoList 保存的对象列表
	 * @return 保存后的数据
	 */
	private List<T> insertBatch(List<T> eoList) {
		return null;
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
	private List<T> findAll() {
		if(logger.isDebugEnabled()) {
			logger.debug("exec findAll records");
		}
		return null;
	}

	/**
	 * 根据queryEo的查询条件condition 查找对象列表
	 *
	 * @param queryEo 查询条件对象
	 * @return 数据列表
	 */
	private List<T> findByCondition(T queryEo){
		if(logger.isDebugEnabled()) {
			logger.debug("exec findByCondition Condition:{}", queryEo.getCondition());
		}
		return null;
	}

	/**
	 * 根据类名和主键Id 删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkId    主键id
	 * @return
	 */
	private Integer deleteById(Class<?> clazz, PK pkId){
		return null;
	}

	/**
	 * 根据类名和主键Id数组 批量删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkIdArray    主键id数组
	 * @return 删除条数
	 */
	private Integer deleteBatch(Class<?> clazz, PK[] pkIdArray){
		return null;
	}

	/**
	 * 根据类名和主键Id 逻辑删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkId    主键id
	 * @return 删除条数
	 */
	private Integer deleteLogicById(Class<?> clazz, PK pkId){
		return null;
	}

	/**
	 * 根据类名和主键Id数组 批量逻辑删除数据
	 *
	 * @param clazz   删除数据对应的类名
	 * @param pkIdArray    主键id数组
	 * @return 删除条数
	 */
	private Integer deleteLogicBatch(Class<?> clazz, PK[] pkIdArray){
		return null;
	}

	/**
	 * 根据类名和主键Id 查询数据
	 *
	 * @param clazz 数据对应的类名
	 * @param pkId  主键id
	 * @return
	 */
	private T findById(Class<?> clazz, PK pkId){
		return null;
	}

	/**
	 * 根据类名和主键Id数组 查询数据
	 *
	 * @param clazz 数据对应的类名
	 * @param pkIds  主键id数组
	 * @return
	 */
	private List<T> findByIds(Class<?> clazz, PK[] pkIds){
		return null;
	}

	/**
	 * 根据类名和主键Id数组 查询数据
	 *
	 * @param clazz
	 * @param pkIds
	 * @param isContainsDr 是否包含已逻辑删除的数据
	 * @return
	 */
	private List<T> findByIdsDr(Class<?> clazz, PK[] pkIds, Boolean isContainsDr){
		return null;
	}

	/**
	 * 根据类名和主键Id 查询指定字段列表的值
	 *
	 * @param clazz 数据对应的类名
	 * @param pkId  主键id
	 * @param tableColumnNames 需要返回的列名
	 * @return
	 */
	private T findColumnById(Class<?> clazz, PK pkId, String... tableColumnNames){
		return null;
	}

	/**
	 * 根据类名和主键Id 查询指定字段列表的值
	 *
	 * @param clazz 数据对应的类名
	 * @param pkIds  主键id数组
	 * @param tableColumnNames 需要返回的列名
	 * @return
	 */
	private List<T> findColumnByIds(Class<?> clazz, PK[] pkIds, String... tableColumnNames){
		return null;
	}

	/**
	 * 根据T中的不为null的属性值查询 只返回tableColumnName对应的数据列表
	 *
	 * @param t
	 * @return
	 */
	private List<T> findListColumn(T t, String... tableColumnNames){
		return null;
	}

	/**
	 * 查询clazz对应表的记录总数
	 *
	 * @param clazz
	 * @return
	 */
	private Long countByClazz(Class<?> clazz){
		return getESTemplate().findCount(clazz , null);
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


	private String getTIndex(){
		return ConfigHelper.getIndex(getTClass());
	}

	private PageInfo<T> buildPageInfo(List<T> dataList , Integer currentPage, Integer pageSize , Long count){
		PageInfo<T> pageInfo = new PageInfo<>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setPageNum(currentPage);
		pageInfo.setList(dataList);
		pageInfo.setTotal(count);
		return pageInfo;
	}
}
