package org.opensourceframework.demo.seata.integration.common.dubbo;

import org.opensourceframework.demo.seata.integration.common.dto.CommodityDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;

/**
 * 仓库服务
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
public interface StorageDubboService {

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStorage(CommodityDTO commodityDTO);
}
