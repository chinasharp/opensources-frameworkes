package org.opensourceframework.demo.idempotent.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.opensourceframework.demo.idempotent.biz.dao.DemoUserDao;
import org.opensourceframework.demo.idempotent.biz.dao.eo.DemoUserEo;
import org.opensourceframework.demo.idempotent.biz.service.IDemoUserService;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Service
public class DemoUserServiceImpl extends BizBaseServiceImpl<DemoUserEo, Long> implements IDemoUserService {
	@Autowired
	private DemoUserDao demoUserDao;

	/**
	 * 获取操作的Dao Bean
	 */
	@Override
	public BizBaseDao<DemoUserEo, Long> getBizDao() {
		return demoUserDao;
	}

	/**
	 * 保存或更新
	 *
	 * @param eo
	 */
	@Override
	public DemoUserEo saveOrUpdate(DemoUserEo eo) {
		if(eo.getId() == null){
			demoUserDao.insert(eo);
		}else{
			demoUserDao.updateNotNull(eo);
		}
		return eo;
	}

	@Override
	public List<DemoUserEo> batchSave(List<DemoUserEo> eoList) {
		demoUserDao.insertBatch(eoList);
		return eoList;
	}

	@Override
	public Integer updateWithNull(DemoUserEo eo) {
		return super.updateWithNull(eo);
	}

	@Override
	public Integer updateNotNull(DemoUserEo eo) {
		return super.updateNotNull(eo);
	}

	@Override
	public List<DemoUserEo> customMethod(DemoUserEo eo) {
		return null;
	}

	/**
	 *
	 *
	 * @param queryEo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo<DemoUserEo> findPageByMapper(DemoUserEo queryEo, Integer currentPage, Integer pageSize) {
		PageInfo<DemoUserEo> eoPageInfo = demoUserDao.findPageByMapper(queryEo , currentPage , pageSize);
		return eoPageInfo;
	}

	@Override
	public List<DemoUserEo> findByIds(List<Long> idList) {
		List<DemoUserEo> eoList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(idList)) {
			Long[] idArray = new Long[idList.size()];
			eoList =  getBizDao().findByIds(idList.toArray(idArray));
		}
		return eoList;
	}
}
