package org.opensourceframework.demo.seata.integration.order.mapper;

import org.opensourceframework.component.dao.base.BaseMapper;
import org.opensourceframework.demo.seata.integration.order.entity.Order;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface OrderMapper extends BaseMapper<Order, Long> {

    /**
     * 创建订单
     * @Param:  order 订单信息
     * @Return:
     */
    void createOrder(@Param("order") Order order);
}
