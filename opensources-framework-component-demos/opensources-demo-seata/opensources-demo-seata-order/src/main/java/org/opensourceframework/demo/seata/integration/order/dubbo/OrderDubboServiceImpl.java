package org.opensourceframework.demo.seata.integration.order.dubbo;


import org.opensourceframework.demo.seata.integration.common.dto.OrderDTO;
import org.opensourceframework.demo.seata.integration.common.dubbo.OrderDubboService;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.demo.seata.integration.order.service.IOrderService;
import io.seata.core.context.RootContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@DubboService(version = "1.0.0",protocol = "${dubbo.protocol.id}",group = "${dubbo.application.id}")
public class OrderDubboServiceImpl implements OrderDubboService {

    @Autowired
    private IOrderService orderService;

    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        System.out.println("全局事务id ：" + RootContext.getXID());
        return orderService.createOrder(orderDTO);
    }
}
