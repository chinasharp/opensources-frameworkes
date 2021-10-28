#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import ${package}.biz.dao.DemoDao;
import ${package}.biz.dao.eo.DemoEo;
import ${package}.biz.service.IDemoService;
import ${groupId}.starter.mybatis.base.dao.BizBaseDao;
import ${groupId}.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service实现示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Service
public class DemoServiceImpl extends BizBaseServiceImpl<DemoEo, Long> implements IDemoService {
	@Autowired
	private DemoDao demoUserDao;

	/**
	 * 获取操作的Dao Bean
	 */
	@Override
	public BizBaseDao<DemoEo, Long> getBizDao() {
		return demoUserDao;
	}

	/**
	 * 保存或更新
	 *
	 * @param eo
	 */
	@Transactional
	@Override
	public DemoEo saveOrUpdate(DemoEo eo) {
		if(eo.getId() == null){
			demoUserDao.insert(eo);
		}else{
			int count = demoUserDao.updateNotNull(eo);
			if(count == 0){
				demoUserDao.insert(eo);
			}
		}
		return eo;
	}

	@Transactional
	@Override
	public List<DemoEo> batchSave(List<DemoEo> eoList) {
		demoUserDao.insertBatch(eoList);
		return eoList;
	}

	@Transactional
	@Override
	public Integer updateWithNull(DemoEo eo) {
		return super.updateWithNull(eo);
	}

	@Transactional
	@Override
	public Integer updateNotNull(DemoEo eo) {
		return super.updateNotNull(eo);
	}

	@Override
	public List<DemoEo> customMethod(DemoEo eo) {
		return null;
	}

	/**
	 * 使用Mybatis的注解@Select @Update @Insert实现
	 *
	 * @param queryEo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo<DemoEo> findPageByMapper(DemoEo queryEo, Integer currentPage, Integer pageSize) {
		PageInfo<DemoEo> eoPageInfo = demoUserDao.findPageByMapper(queryEo , currentPage , pageSize);
		return eoPageInfo;
	}

	@Override
	public List<DemoEo> findByIds(List<Long> idList) {
		List<DemoEo> eoList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(idList)) {
			Long[] idArray = new Long[idList.size()];
			eoList =  getBizDao().findByIds(idList.toArray(idArray));
		}
		return eoList;
	}


	@Override
	public DemoEo findByMapperXml(Long id){
		return demoUserDao.findByMapperXml(id);
	}

	@Transactional
	@Override
	public DemoEo saveByMybatisXml(DemoEo eo){
		return demoUserDao.saveByMybatisXml(eo);
	}
}
