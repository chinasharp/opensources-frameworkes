package org.opensourceframework.demo.mq.transaction.mq.handler;


import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.demo.mq.transaction.dao.eo.DemoEo;
import org.opensourceframework.demo.mq.transaction.dto.request.user.DemoUserReqDto;
import org.opensourceframework.demo.mq.transaction.service.IDemoService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * MQ消息处理类示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class DemoHandler {
	@Resource
	private IDemoService demoService;

	@Transactional(rollbackFor = Exception.class)
	public DemoEo handler(DemoUserReqDto reqDto){
		DemoEo demoUserEo = new DemoEo();
		BeanHelper.copyProperties(demoUserEo , reqDto);
		demoService.save(demoUserEo);
		return demoUserEo;
	}

	public void rollBackHandler(Long pkId){
		demoService.deleteById(pkId);
	}
}
