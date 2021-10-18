package org.opensourceframework.application.demo.biz.service.impl;

import org.opensourceframework.application.demo.biz.feignclients.feignservice.DemoFeignService;
import org.opensourceframework.application.demo.biz.service.IDemoService;
import org.opensourceframework.center.demo.api.dto.request.user.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.user.DemoUserRespDto;
import org.opensourceframework.base.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Service
public class DemoServiceImpl implements IDemoService {
	@Lazy
	@Autowired
	private DemoFeignService demoUserFeignService;

	@Override
	public RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto){
		return demoUserFeignService.saveOrUpdate(demoUserReqDto);
	}


}
