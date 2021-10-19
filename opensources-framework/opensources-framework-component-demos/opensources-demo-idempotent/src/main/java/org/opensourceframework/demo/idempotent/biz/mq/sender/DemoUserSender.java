package org.opensourceframework.demo.idempotent.biz.mq.sender;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.rest.MessageResponse;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.api.IMessageSender;
import org.opensourceframework.demo.idempotent.biz.mq.registryvo.DemoUserRegistryVo;
import org.opensourceframework.demo.idempotent.dto.request.user.DemoUserReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class DemoUserSender {
	@Autowired
	private IMessageSender messageSender;

	@Autowired
	private DemoUserRegistryVo demoUserRegistryVo;

	public MessageResponse sendMessage(DemoUserReqDto reqDto){
		MessageVo messageVo = new MessageVo();
		messageVo.setBizCode("demo_user");
		messageVo.setBizName("mq user");
		messageVo.setMsgContent(JSON.toJSONString(reqDto));
		return messageSender.sendMessage(demoUserRegistryVo.getTopic(), demoUserRegistryVo.getTag() , messageVo);
	}
}
