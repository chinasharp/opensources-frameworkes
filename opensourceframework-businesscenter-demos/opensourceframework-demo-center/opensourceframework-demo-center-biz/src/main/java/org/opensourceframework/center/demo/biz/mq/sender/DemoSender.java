package org.opensourceframework.center.demo.biz.mq.sender;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.biz.mq.registryvo.DemoRegistryVo;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.component.mq.api.IMessageSender;
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
	 * 普通消息发送 示例
	 *
	 * @param reqDto
	 */
	public void sendMessage(DemoUserReqDto reqDto){
		MessageVo messageVo = new MessageVo();
		messageVo.setBizCode("demo_user");
		messageVo.setBizName("mq user");
		messageVo.setMsgContent(JSON.toJSONString(reqDto));
		// 使用常量
		//messageSender.sendMessage(MQConstant.DEMO_USER_TOPIC, MQConstant.DEMO_USER_TAG , messageVo);
		// 使用nacos配置
		messageSender.sendMessage(demoUserRegistryVo.getTopic(), demoUserRegistryVo.getTag() , messageVo);
	}
}
