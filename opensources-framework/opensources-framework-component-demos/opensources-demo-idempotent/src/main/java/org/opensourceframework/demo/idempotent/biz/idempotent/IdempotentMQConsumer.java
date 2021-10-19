package org.opensourceframework.demo.idempotent.biz.idempotent;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.center.demo.api.dto.request.user.DemoUserReqDto;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.idempotent.biz.cache.DemoUserCache;
import org.opensourceframework.demo.idempotent.biz.dao.eo.DemoUserEo;
import org.opensourceframework.demo.idempotent.biz.mq.handler.DemoUserHandler;
import org.opensourceframework.commons.log.LoggerFactory;
import org.opensourceframework.component.idempotent.annotation.IdempotentHandler;
import org.opensourceframework.component.mq.annotation.MQSubscribe;
import org.opensourceframework.component.mq.api.IMessageProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MQ consumer 消息幂等(解决重复消费)
 *
 * @author yu.ce@foxmail.com
 *
 * @since 1.0.0
 *
 */
@MQSubscribe(topic = "${mq.demouser.subscribe.registryvo.topic}", tag = "${mq.demouser.subscribe.registryvo.tag}" ,
		consumer = "${mq.demouser.subscribe.registryvo.consumer}")
@Component
public class IdempotentMQConsumer implements IMessageProcessor<MessageVo> {
	private static final Logger loger = LoggerFactory.getLogger(IdempotentMQConsumer.class);
	private static java.util.concurrent.atomic.AtomicLong consumerCount = new AtomicLong(0);


	@Resource
	private DemoUserHandler demoUserHandler;

	@Autowired
	private DemoUserCache demoUserCache;

	/**
	 * 处理消息
	 *
	 * @param messageVo
	 *        waitTimeout 幂等锁获取的最大等待时间
	 *        leaseTime   幂等锁上锁的最大时间
	 *        expireTime  幂等数据的失效时间
	 *        单位都为秒
	 * @return
	 */
	@Override
	@IdempotentHandler(parameter = "messageVo" , properties = "messageId" ,waitTimeout= 300 , leaseTime = 300, validTime = 3600)
	public MessageResponse process(MessageVo messageVo) {
		consumerCount.incrementAndGet();
		MessageResponse messageResponse = null;
		loger.info("Subscribe MQ Message:{}" , JSON.toJSONString(messageVo));
		String msgContent = messageVo.getMsgContent();
		DemoUserReqDto reqDto = JSON.parseObject(msgContent , DemoUserReqDto.class);
		DemoUserEo demoUserEo = null;
		Boolean isError = false;
		try {
			demoUserEo = demoUserHandler.handler(reqDto);
		} catch (Exception e) {
			messageResponse =  MessageResponse.ERROR;
			isError = true;
			loger.error("consumer messgae error.reason:{}" , ExceptionUtils.getStackTrace(e));
		}

		if(!isError) {
			try {
				demoUserCache.setCache(demoUserEo);
				messageResponse = MessageResponse.SUCCESS;
			} catch (Exception e) {
				demoUserHandler.rollBackHandler(demoUserEo.getId());
				messageResponse = MessageResponse.ERROR;
			}
		}
		loger.info("consumer times :{}" , consumerCount.get() );
		return messageResponse;
	}
}
