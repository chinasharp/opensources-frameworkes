package org.opensourceframework.demo.seata.integration.order.service;

import org.opensourceframework.demo.seata.integration.common.dto.AccountDTO;
import org.opensourceframework.demo.seata.integration.common.dto.OrderDTO;
import org.opensourceframework.demo.seata.integration.common.dubbo.AccountDubboService;
import org.opensourceframework.demo.seata.integration.common.enums.RspStatusEnum;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import org.opensourceframework.demo.seata.integration.order.dao.OrderDao;
import org.opensourceframework.demo.seata.integration.order.entity.Order;
import org.apache.dubbo.config.annotation.DubboReference;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.opensourceframework.starter.mybatis.base.service.impl.BizBaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Service
public class OrderServiceImpl extends BizBaseServiceImpl<Order, Long> implements IOrderService {

    @DubboReference(group = "opensources-user-account", version = "1.0.0", protocol = "dubbo",check = false,timeout = 60000)
    private AccountDubboService accountDubboService;

    @Autowired
    private OrderDao orderDao;

    /**
     * 创建订单
     * @Param:  OrderDTO  订单对象
     * @Return:  OrderDTO  订单对象
     */
    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        ObjectResponse<OrderDTO> response = new ObjectResponse<>();
        //扣减用户账户
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(orderDTO.getUserId());
        accountDTO.setAmount(orderDTO.getOrderAmount());
        ObjectResponse objectResponse = accountDubboService.decreaseAccount(accountDTO);

        //生成订单号
        orderDTO.setOrderNo(UUID.randomUUID().toString().replace("-",""));
        //生成订单
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        order.setCount(orderDTO.getOrderCount());
        order.setAmount(orderDTO.getOrderAmount().doubleValue());
        try {
            orderDao.insert(order);
        } catch (Exception e) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }

        if (objectResponse.getStatus() != 200) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.SUCCESS.getCode());
        response.setMessage(RspStatusEnum.SUCCESS.getMessage());
        return response;
    }

    /**
     * 获取操作的Dao Bean
     *
     * @return dao的操作bean
     */
    @Override
    public BizBaseDao<Order, Long> getBizDao() {
        return orderDao;
    }

    /**
     * 根据id列表查询数据列表
     *
     * @param idList 主键集合
     * @return 数据列表
     */
    @Override
    public List<Order> findByIds(List<Long> idList) {
        return null;
    }
}
