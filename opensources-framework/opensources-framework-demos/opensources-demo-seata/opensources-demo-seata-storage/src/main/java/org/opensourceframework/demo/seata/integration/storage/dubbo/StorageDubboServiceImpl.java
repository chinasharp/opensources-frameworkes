package org.opensourceframework.demo.seata.integration.storage.dubbo;

import org.opensourceframework.demo.seata.integration.common.dto.CommodityDTO;
import org.opensourceframework.demo.seata.integration.common.dubbo.StorageDubboService;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.demo.seata.integration.storage.service.IStorageService;
import io.seata.core.context.RootContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/23 16:13
 */
@DubboService(version = "1.0.0",protocol = "${dubbo.protocol.id}",group = "${dubbo.application.id}")
public class StorageDubboServiceImpl implements StorageDubboService {

    @Autowired
    private IStorageService storageService;

    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        System.out.println("全局事务id ：" + RootContext.getXID());
        return storageService.decreaseStorage(commodityDTO);
    }
}
