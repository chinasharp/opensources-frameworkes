package org.opensourceframework.demo.mq.transaction.mq.sender;

import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.demo.mq.transaction.mq.registryvo.DemoRegistryVo;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.opensourceframework.component.mq.vo.TransMessageVo;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息发送示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class DemoSender {
	@Autowired
	private IMessageSender messageSender;

	@Resource
	private DemoRegistryVo demoUserRegistryVo;

	/**
	 * 事务消息发送 使用默认的TransactionListenter示例
	 *
	 * @param messageVo
	 */
	public MessageResponse sendTransactionMessage(TransMessageVo messageVo){
		return messageSender.sendTransactionMessage(demoUserRegistryVo.getTopic(), demoUserRegistryVo.getTag() , messageVo);
	}

	/**
	 * 事务消息发送 使用自定义的TransactionListenter示例
	 *
	 * @param messageVo
	 */
	public MessageResponse sendTransactionMessage(TransMessageVo messageVo , TransactionListener transactionListener){
		return messageSender.sendTransactionMessage(demoUserRegistryVo.getTopic(), demoUserRegistryVo.getTag() , messageVo , transactionListener , null);
	}
}
