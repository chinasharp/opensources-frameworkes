package org.opensourceframework.center.demo.biz.mq.processor;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.biz.cache.DemoCache;
import org.opensourceframework.center.demo.biz.dao.eo.DemoUserEo;
import org.opensourceframework.center.demo.biz.mq.handler.DemoHandler;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.common.log.LoggerFactory;
import org.opensourceframework.component.mq.annotation.MQSubscribe;
import org.opensourceframework.component.mq.api.IMessageProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * MQ consumer订阅示例
 * 2种示例,一种使用直接使用常量,一种使用${}读取配置文件,或者nacos配置
 * 严禁将有数据库事务相关的处理放在Processor类中,请放在Handler中
 *
 * @author yu.ce@foxmail.com
 *
 * @since 1.0.0
 *
 */
@Component
@MQSubscribe(topic = "${mq.demouser.subscribe.registryvo.topic}", tag = "${mq.demouser.subscribe.registryvo.tag}" ,
		consumer = "${mq.demouser.subscribe.registryvo.consumer}")
public class DemoProcessor implements IMessageProcessor<MessageVo> {
	private static final Logger logger = LoggerFactory.getLogger(DemoProcessor.class);
	@Resource
	private DemoHandler demoHandler;

	@Autowired
	private DemoCache demoCache;

	/**
	 * 处理消息
	 *
	 * @param messageVo
	 * @return
	 */
	@Override
	public MessageResponse process(MessageVo messageVo) {
		logger.info("Subscribe MQ Message:{}" , JSON.toJSONString(messageVo));
		String msgContent = messageVo.getMsgContent();
		DemoUserReqDto reqDto = JSON.parseObject(msgContent , DemoUserReqDto.class);
		DemoUserEo demoUserEo = null;
		boolean rollBack = false;
		try {
			demoUserEo = demoHandler.handler(reqDto);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return MessageResponse.ERROR;
		}

		try {
			demoCache.setCache(demoUserEo);
			return MessageResponse.SUCCESS;
		}catch (Exception e){
			demoHandler.rollBackHandler(demoUserEo.getId());
			return MessageResponse.ERROR;
		}
	}
}
