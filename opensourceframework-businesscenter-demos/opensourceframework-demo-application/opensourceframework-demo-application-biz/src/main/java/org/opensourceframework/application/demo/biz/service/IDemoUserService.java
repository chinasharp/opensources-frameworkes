package org.opensourceframework.application.demo.biz.service;

import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.DemoUserRespDto;
import org.opensourceframework.base.rest.RestResponse;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IDemoUserService {
	RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto);
}
