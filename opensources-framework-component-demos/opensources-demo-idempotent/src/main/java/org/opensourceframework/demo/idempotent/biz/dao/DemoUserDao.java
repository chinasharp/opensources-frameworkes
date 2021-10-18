package org.opensourceframework.demo.idempotent.biz.dao;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.opensourceframework.component.dao.contant.SqlStatementContant;
import org.opensourceframework.demo.idempotent.biz.dao.eo.DemoUserEo;
import org.opensourceframework.demo.idempotent.biz.dao.mapper.DemoUserMapper;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
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
public class DemoUserDao extends BizBaseDao<DemoUserEo, Long> {
	private static Logger logger = LoggerFactory.getLogger(DemoUserDao.class);
	@Autowired
	private DemoUserMapper demoUserMapper;

	public PageInfo<DemoUserEo> findPageByMapper(DemoUserEo queryEo, Integer currentPage, Integer pageSize) {
		logger.info("exec findPage where:{} ,currentPage , pageSize ", JSON.toJSONString(queryEo), currentPage, pageSize);
		currentPage = null == currentPage ? SqlStatementContant.DEFAULT_PAGE_NUMBER : currentPage;
		pageSize = null == pageSize ? SqlStatementContant.DEFAULT_PAGE_SIZE : pageSize;
		PageHelper.startPage(currentPage.intValue(), pageSize.intValue());
		List<DemoUserEo> eoList = demoUserMapper.findPage(queryEo);
		return new PageInfo<>(eoList);
	}
}
