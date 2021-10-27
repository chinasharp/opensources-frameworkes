package org.opensourceframework.demo.seata.integration.storage.service;

import org.opensourceframework.demo.seata.integration.common.dto.CommodityDTO;
import org.opensourceframework.demo.seata.integration.common.enums.RspStatusEnum;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.demo.seata.integration.storage.dao.StorageDao;
import org.opensourceframework.demo.seata.integration.storage.entity.Storage;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  库存服务实现类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Service
public class StorageServiceImpl extends BizBaseServiceImpl<Storage, Long> implements IStorageService {

    @Autowired
    private StorageDao storageDao;

    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        int storage = storageDao.decreaseStorage(commodityDTO.getCommodityCode(), commodityDTO.getCount());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (storage > 0){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }

    /**
     * 获取操作的Dao Bean
     *
     * @return dao的操作bean
     */
    @Override
    public BizBaseDao<Storage, Long> getBizDao() {
        return storageDao;
    }
}
