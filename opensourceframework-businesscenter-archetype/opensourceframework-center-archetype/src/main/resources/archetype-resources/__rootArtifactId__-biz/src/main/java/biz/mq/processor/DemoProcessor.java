#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.mq.processor;

import com.alibaba.fastjson.JSON;
import ${package}.api.dto.request.DemoReqDto;
import ${package}.biz.cache.DemoCache;
import ${package}.biz.dao.eo.DemoEo;
import ${package}.biz.mq.handler.DemoHandler;
import ${groupId}.base.rest.MessageResponse;
import ${groupId}.base.vo.MessageVo;
import ${groupId}.common.log.LoggerFactory;
import ${groupId}.component.mq.annotation.MQSubscribe;
import ${groupId}.component.mq.api.IMessageProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * MQ consumer订阅示例
 * 2种示例,一种使用直接使用常量,一种使用${symbol_dollar}{}读取配置文件,或者nacos配置
 * 严禁将有数据库事务相关的处理放在Processor类中,请放在Handler中
 *
 * @author yu.ce@foxmail.com
 *
 * @since 1.0.0
 *
 */
@Component
@MQSubscribe(topic = "${symbol_dollar}{mq.${bizName}.subscribe.registryvo.topic}", tag = "${symbol_dollar}{mq.${bizName}.subscribe.registryvo.tag}" ,
		consumer = "${symbol_dollar}{mq.${bizName}.subscribe.registryvo.consumer}")
public class DemoProcessor implements IMessageProcessor<MessageVo> {
	private static final Logger loger = LoggerFactory.getLogger(DemoProcessor.class);
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
		loger.info("Subscribe MQ Message:{}" , JSON.toJSONString(messageVo));
		String msgContent = messageVo.getMsgContent();
		DemoReqDto reqDto = JSON.parseObject(msgContent , DemoReqDto.class);
		DemoEo demoUserEo = null;
		boolean rollBack = false;
		try {
			demoUserEo = demoHandler.handler(reqDto);
		} catch (Exception e) {
			loger.error(ExceptionUtils.getStackTrace(e));
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
