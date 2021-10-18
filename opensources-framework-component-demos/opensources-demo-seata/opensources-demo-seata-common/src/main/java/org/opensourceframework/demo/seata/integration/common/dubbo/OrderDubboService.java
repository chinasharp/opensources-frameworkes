package org.opensourceframework.demo.seata.integration.common.dubbo;

import org.opensourceframework.demo.seata.integration.common.dto.OrderDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;

/**
 * 订单服务接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
public interface OrderDubboService {

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO);
}
