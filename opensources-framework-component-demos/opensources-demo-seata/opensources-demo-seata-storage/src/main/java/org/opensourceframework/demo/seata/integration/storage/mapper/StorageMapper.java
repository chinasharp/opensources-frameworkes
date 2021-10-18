package org.opensourceframework.demo.seata.integration.storage.mapper;

import org.opensourceframework.component.dao.base.BaseMapper;
import org.opensourceframework.demo.seata.integration.storage.entity.Storage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface StorageMapper extends BaseMapper<Storage, Long> {

    /**
     * 扣减商品库存
     * @Param: commodityCode 商品code  count扣减数量
     * @Return:
     */
    Integer decreaseStorage(@Param("commodityCode") String commodityCode, @Param("count") Integer count);
}
