package org.opensourceframework.center.demo.api.op;

import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.DemoUserRespDto;
import org.opensourceframework.base.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 操作类(查询以外的操作)Api示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags={"Demo 操作api"})
public interface IUserDemoApi {
	/**
	 * 保存或者更新
	 *
	 * @param demoUserReqDto
	 * @return
	 */
	@ApiOperation(value="保存或者更新Demo信息", notes="保存或者更新Demo信息")
	RestResponse<DemoUserRespDto> saveOrUpdate(@RequestBody DemoUserReqDto demoUserReqDto);

	/**
	 * 批量保存
	 *
	 * @param reqDtoList
	 * @return
	 */
	@ApiOperation(value="批量保存Demo信息", notes="批量保存Demo信息")
	RestResponse<List<DemoUserRespDto>> batchSave(@RequestBody List<DemoUserReqDto> reqDtoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value="更新reqDto中的所有属性", notes="更新reqDto中的所有属性")
	RestResponse<Void> updateWithNull(@RequestBody DemoUserReqDto reqDto);

	/**
	 * 更新所有属性
	 *
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value="更新reqDto中不为空的属性", notes="更新reqDto中不为空的属性")
	RestResponse<Void> updateNotNull(@RequestBody DemoUserReqDto reqDto);
}
