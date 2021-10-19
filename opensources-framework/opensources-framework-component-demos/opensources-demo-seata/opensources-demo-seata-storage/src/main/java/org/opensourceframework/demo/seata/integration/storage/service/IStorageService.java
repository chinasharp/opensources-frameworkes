package org.opensourceframework.demo.seata.integration.storage.service;

import org.opensourceframework.demo.seata.integration.common.dto.CommodityDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.demo.seata.integration.storage.entity.Storage;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;

/**
 * 仓库服务
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface IStorageService extends IBizBaseService<Storage, Long> {

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStorage(CommodityDTO commodityDTO);
}
