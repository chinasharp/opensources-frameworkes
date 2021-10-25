package org.opensourceframework.center.demo.biz.mq.handler;


import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.biz.dao.eo.DemoUserEo;
import org.opensourceframework.center.demo.biz.service.IDemoService;
import org.opensourceframework.base.helper.BeanHelper;
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
	public DemoUserEo handler(DemoUserReqDto reqDto){
		DemoUserEo demoUserEo = new DemoUserEo();
		BeanHelper.copyProperties(demoUserEo , reqDto);
		demoService.saveOrUpdate(demoUserEo);
		return demoUserEo;
	}

	public void rollBackHandler(Long pkId){
		demoService.deleteById(pkId);
	}
}
