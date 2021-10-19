package org.opensourceframework.demo.seata.integration.order.dao;

import org.opensourceframework.demo.seata.integration.order.mapper.OrderMapper;
import org.opensourceframework.demo.seata.integration.order.entity.Order;
import org.opensourceframework.starter.mybatis.base.dao.BizBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Repository
public class OrderDao extends BizBaseDao<Order, Long> {
	private static Logger logger = LoggerFactory.getLogger(Order.class);
	@Resource
	private OrderMapper orderMapper;
}
