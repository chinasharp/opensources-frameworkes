#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.dao;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ${package}.biz.dao.eo.DemoEo;
import ${package}.biz.dao.mapper.DemoMapper;
import ${groupId}.component.dao.contant.SqlStatementContant;
import ${groupId}.starter.mybatis.base.dao.BizBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Dao层操作类示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Repository
public class DemoDao extends BizBaseDao<DemoEo, Long> {
	private static Logger logger = LoggerFactory.getLogger(DemoDao.class);
	@Resource
	private DemoMapper demoUserMapper;

	public PageInfo<DemoEo> findPageByMapper(DemoEo queryEo, Integer currentPage, Integer pageSize) {
		logger.info("exec findPage where:{} ,currentPage , pageSize ", JSON.toJSONString(queryEo), currentPage, pageSize);
		currentPage = null == currentPage ? SqlStatementContant.DEFAULT_PAGE_NUMBER : currentPage;
		pageSize = null == pageSize ? SqlStatementContant.DEFAULT_PAGE_SIZE : pageSize;
		PageHelper.startPage(currentPage.intValue(), pageSize.intValue());
		List<DemoEo> eoList = demoUserMapper.findPage(queryEo);
		return new PageInfo<>(eoList);
	}

	public DemoEo findByMapperXml(Long id){
		return demoUserMapper.findByMapperXml(id);
	}

	public DemoEo saveByMybatisXml(DemoEo eo){
		demoUserMapper.saveByMyBatisXml(eo);
		return eo;
	}
}
