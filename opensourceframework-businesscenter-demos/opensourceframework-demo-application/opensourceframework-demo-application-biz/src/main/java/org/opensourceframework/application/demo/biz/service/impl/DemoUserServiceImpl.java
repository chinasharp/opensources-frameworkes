package org.opensourceframework.application.demo.biz.service.impl;

import org.opensourceframework.application.demo.biz.feignclients.feignservice.DemoUserFeignService;
import org.opensourceframework.application.demo.biz.service.IDemoUserService;
import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.DemoUserRespDto;
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
public class DemoUserServiceImpl implements IDemoUserService {
	@Lazy
	@Autowired
	private DemoUserFeignService demoUserFeignService;

	@Override
	public RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto){
		return demoUserFeignService.saveOrUpdate(demoUserReqDto);
	}


}
