package org.opensourceframework.demo.seata.integration.storage.dao;

import org.opensourceframework.demo.seata.integration.storage.mapper.StorageMapper;
import org.opensourceframework.demo.seata.integration.storage.entity.Storage;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Repository
public class StorageDao extends BizBaseDao<Storage, Long> {
	private static Logger logger = LoggerFactory.getLogger(StorageDao.class);
	@Resource
	private StorageMapper storageMapper;

	/**
	 * 扣减商品库存
	 * @Param: commodityCode 商品code  count扣减数量
	 * @Return:
	 */
	public Integer decreaseStorage(String commodityCode,Integer count){
		return storageMapper.decreaseStorage(commodityCode , count);
	}
}
