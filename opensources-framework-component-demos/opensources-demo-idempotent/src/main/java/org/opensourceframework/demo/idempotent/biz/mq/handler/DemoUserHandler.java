package org.opensourceframework.demo.idempotent.biz.mq.handler;

import org.opensourceframework.center.demo.api.dto.request.user.DemoUserReqDto;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.demo.idempotent.biz.dao.eo.DemoUserEo;
import org.opensourceframework.demo.idempotent.biz.service.IDemoUserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * MQ消息处理类示例,严禁将有数据库相关的处理放在Processor类中
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Component
public class DemoUserHandler {
	@Resource
	private IDemoUserService demoUserService;

	@Transactional(rollbackFor = Exception.class)
	public DemoUserEo handler(DemoUserReqDto reqDto){
		DemoUserEo demoUserEo = new DemoUserEo();
		BeanHelper.copyProperties(demoUserEo , reqDto);
		demoUserService.saveOrUpdate(demoUserEo);
		return demoUserEo;
	}

	public void rollBackHandler(Long pkId){
		demoUserService.deleteById(pkId);
	}
}
