package org.opensourceframework.demo.bloomfilter.service.impl;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.demo.bloomfilter.cache.DemoCache;
import org.opensourceframework.demo.bloomfilter.service.IDemoService;
import org.opensourceframework.demo.bloomfilter.dao.DemoDao;
import org.opensourceframework.demo.bloomfilter.dao.eo.DemoEo;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service实现示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Service
public class DemoServiceImpl extends BizBaseServiceImpl<DemoEo, Long> implements IDemoService {
	private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
	@Autowired
	private DemoDao demoUserDao;

	@Autowired
	private DemoCache demoCache;

	/**
	 * 获取操作的Dao Bean
	 */
	@Override
	public BizBaseDao<DemoEo, Long> getBizDao() {
		return demoUserDao;
	}

	@Override
	public DemoEo findById(Long pkId){
		DemoEo demoEo = null;
		Boolean isExist = demoCache.checkExist(pkId);
		logger.info("Data isExist:{}" , isExist);
		if(isExist) {
			demoEo = demoCache.getCache(pkId);
			logger.info("Query By Redis Cache Data:{}" , JSON.toJSONString(demoEo));
			if(demoEo == null) {
				demoEo = demoUserDao.findById(pkId);
				logger.info("Query By DB Data:{}" , JSON.toJSONString(demoEo));
				if(demoEo == null){
					demoEo = new DemoEo();
					demoEo.setId(pkId);
					demoEo.setDbNullFlag(true);
					logger.info("Create New Data For Cache Penetration:{}" , JSON.toJSONString(demoEo));
				}
				demoCache.addCache(demoEo);
			}

			if(demoEo != null && demoEo.getDbNullFlag()){
				demoEo = null;
			}
		}
		logger.info("Result Data:{}" , JSON.toJSONString(demoEo));
		return demoEo;
	}

}
