package org.opensourceframework.demo.mybatis.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.demo.mybatis.api.request.DemoUserReqDto;
import org.opensourceframework.demo.mybatis.api.response.DemoUserRespDto;
import org.opensourceframework.demo.mybatis.biz.dao.DemoShardingUserDao;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoShardingUserEo;
import org.opensourceframework.demo.mybatis.biz.service.IDemoShardingUserService;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Service
public class DemoShardingUserServiceImpl extends BizBaseServiceImpl<DemoShardingUserEo, Long> implements IDemoShardingUserService {
	@Autowired
	private DemoShardingUserDao demoUserDao;

	/**
	 * 获取操作的Dao Bean
	 */
	@Override
	public BizBaseDao<DemoShardingUserEo, Long> getBizDao() {
		return demoUserDao;
	}

	/**
	 * @param demoUserReqDto
	 */
	@Override
	public DemoUserRespDto saveOrUpdate(DemoUserReqDto demoUserReqDto) {
		DemoShardingUserEo demoShardingUserEo = new DemoShardingUserEo();
		BeanHelper.copyProperties(demoShardingUserEo , demoUserReqDto);

		if(demoShardingUserEo.getId() == null){
			demoUserDao.insert(demoShardingUserEo);
		}else{
			demoUserDao.updateNotNull(demoShardingUserEo);
		}

		DemoUserRespDto demoUserDto = new DemoUserRespDto();
		BeanHelper.copyProperties(demoUserDto , demoShardingUserEo);
		return demoUserDto;
	}

	@Override
	public List<DemoUserRespDto> batchSave(List<DemoUserReqDto> reqDtoList) {
		List<DemoShardingUserEo> eoList = Lists.newArrayList();
		BeanHelper.copyCollection(eoList , reqDtoList , DemoShardingUserEo.class);
		demoUserDao.insertBatch(eoList);
		List<DemoUserRespDto> respDtos = new ArrayList<>();
		BeanHelper.copyCollection(respDtos , eoList , DemoUserRespDto.class);
		return respDtos;
	}

	@Override
	public Integer updateWithNull(DemoUserReqDto reqDto) {
		DemoShardingUserEo demoShardingUserEo = new DemoShardingUserEo();
		BeanHelper.copyProperties(demoShardingUserEo , reqDto);
		return super.updateWithNull(demoShardingUserEo);
	}

	@Override
	public Integer updateNotNull(DemoUserReqDto reqDto) {
		DemoShardingUserEo demoShardingUserEo = new DemoShardingUserEo();
		BeanHelper.copyProperties(demoShardingUserEo , reqDto);
		return super.updateNotNull(demoShardingUserEo);
	}

	@Override
	public List<DemoUserRespDto> customMethod(DemoUserReqDto userReqDto) {
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
	public PageInfo<DemoShardingUserEo> findPageByMapper(DemoShardingUserEo queryEo, Integer currentPage, Integer pageSize) {
		PageInfo<DemoShardingUserEo> eoPageInfo = demoUserDao.findPageByMapper(queryEo , currentPage , pageSize);
		return eoPageInfo;
	}

	@Override
	public List<DemoShardingUserEo> findByIds(List<Long> idList) {
		List<DemoShardingUserEo> eoList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(idList)) {
			Long[] idArray = new Long[idList.size()];
			eoList =  getBizDao().findByIds(idList.toArray(idArray));
		}
		return eoList;
	}
}
