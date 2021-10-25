package org.opensourceframework.application.demo.biz.feignclients.feignservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.opensourceframework.application.demo.biz.feignclients.callback.DemoUserFeignServiceFallbackFactory;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.DemoUserRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags={"demoUser 操作api"})
@FeignClient(name="${provider.demo.service.group}" , path = "/v1/op" ,
		contextId = "org.opensourceframework.application.demo.biz.feignclients.feignservice.DemoUserFeignService",
		fallbackFactory = DemoUserFeignServiceFallbackFactory.class)
public interface DemoUserFeignService {
	/**
	 * 保存或者更新
	 *
	 * @param demoUserReqDto
	 * @return
	 */
	@PostMapping(value={"/saveOrUpdate"}, produces={"application/json"})
	@ApiOperation(value="保存或者更新Demo信息", notes="保存或者更新Demo信息")
	RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto);

	/**
	 * 批量保存
	 *
	 * @param reqDtoList
	 * @return
	 */
	@PostMapping(value={"/batchSave"}, produces={"application/json"})
	@ApiOperation(value="批量保存Demo信息", notes="批量保存Demo信息")
	RestResponse<List<DemoUserRespDto>> batchSave(List<DemoUserReqDto> reqDtoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value={"/updateWithNull"}, produces={"application/json"})
	@ApiOperation(value="更新reqDto中的所有属性", notes="更新reqDto中的所有属性")
	RestResponse<Void> updateWithNull(DemoUserReqDto reqDto);

	/**
	 * 更新所有属性
	 *
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value={"/updateNotNull"}, produces={"application/json"})
	@ApiOperation(value="更新reqDto中不为空的属性", notes="更新reqDto中不为空的属性")
	RestResponse<Void> updateNotNull(DemoUserReqDto reqDto);
}
