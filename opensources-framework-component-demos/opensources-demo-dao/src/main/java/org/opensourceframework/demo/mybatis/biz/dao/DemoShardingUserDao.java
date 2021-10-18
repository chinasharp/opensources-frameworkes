package org.opensourceframework.demo.mybatis.biz.dao;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.component.dao.contant.SqlStatementContant;
import org.opensourceframework.demo.mybatis.biz.dao.eo.DemoShardingUserEo;
import org.opensourceframework.demo.mybatis.biz.dao.mapper.DemoShardingUserMapper;
import org.opensourceframework.starter.mybatis.base.dao.BaseShardingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Repository
public class DemoShardingUserDao extends BaseShardingDao<DemoShardingUserEo, Long> {
	private static Logger logger = LoggerFactory.getLogger(DemoShardingUserDao.class);
	@Autowired
	private DemoShardingUserMapper demoShardingUserMapper;

	public PageInfo<DemoShardingUserEo> findPageByMapper(DemoShardingUserEo queryEo, Integer currentPage, Integer pageSize) {
		logger.info("exec findPage where:{} ,currentPage , pageSize ", JSON.toJSONString(queryEo), currentPage, pageSize);
		currentPage = null == currentPage ? SqlStatementContant.DEFAULT_PAGE_NUMBER : currentPage;
		pageSize = null == pageSize ? SqlStatementContant.DEFAULT_PAGE_SIZE : pageSize;
		PageHelper.startPage(currentPage.intValue(), pageSize.intValue());
		List<DemoShardingUserEo> eoList = demoShardingUserMapper.findPage(queryEo);
		return new PageInfo<>(eoList);
	}
}
