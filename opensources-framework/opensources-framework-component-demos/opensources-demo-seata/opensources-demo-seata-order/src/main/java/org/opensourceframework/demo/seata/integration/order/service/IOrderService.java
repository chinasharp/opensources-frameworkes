package org.opensourceframework.demo.seata.integration.order.service;

import org.opensourceframework.demo.seata.integration.common.dto.OrderDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.demo.seata.integration.order.entity.Order;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;

/**
 *  创建订单
 *
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
public interface IOrderService extends IBizBaseService<Order, Long> {

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO);
}
