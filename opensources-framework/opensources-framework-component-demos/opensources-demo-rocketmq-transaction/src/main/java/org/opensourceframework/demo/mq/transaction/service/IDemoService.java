package org.opensourceframework.demo.mq.transaction.service;

import org.opensourceframework.demo.mq.transaction.dao.eo.DemoEo;
import org.opensourceframework.starter.mybatis.base.service.IBizBaseService;

/**
 * Service接口示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IDemoService extends IBizBaseService<DemoEo, Long> {
	/**
	 * 保存/更新
	 *
	 * @param eo
	 * @return
	 */
	DemoEo save(DemoEo eo);

	/**
	 * MQ 消息事务保存 使用系统默认的TransactionListenter
	 *
	 * @param eo
	 * @return
	 */
	DemoEo saveByDefMQTransaction(DemoEo eo);

	/**
	 * MQ 消息事务保存 使用自定义的TransactionListenter
	 *
	 * @param eo
	 * @return
	 */
	DemoEo saveByCustomMQTransaction(DemoEo eo);
}
