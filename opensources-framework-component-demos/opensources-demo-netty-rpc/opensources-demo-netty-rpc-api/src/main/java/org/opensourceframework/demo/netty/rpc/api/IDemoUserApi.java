package org.opensourceframework.demo.netty.rpc.api;

import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.netty.rpc.api.dto.request.DemoUserReqDto;
import org.opensourceframework.demo.netty.rpc.api.dto.response.DemoUserRespDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags={"demoUser 操作api"})
public interface IDemoUserApi {
	/**
	 * 保存或者更新
	 *
	 * @param demoUserReqDto
	 * @return
	 */
	@ApiOperation(value="保存或者更新DemoUser信息", notes="保存或者更新DemoUser信息")
	RestResponse<DemoUserRespDto> saveOrUpdate(DemoUserReqDto demoUserReqDto);

	/**
	 * 批量保存
	 *
	 * @param reqDtoList
	 * @return
	 */
	@ApiOperation(value="批量保存DemoUser信息", notes="批量保存DemoUser信息")
	RestResponse<List<DemoUserRespDto>> batchSave(List<DemoUserReqDto> reqDtoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value="更新reqDto中的所有属性", notes="更新reqDto中的所有属性")
	RestResponse<Void> updateWithNull(DemoUserReqDto reqDto);

	/**
	 * 更新所有属性
	 *
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value="更新reqDto中不为空的属性", notes="更新reqDto中不为空的属性")
	RestResponse<Void> updateNotNull(DemoUserReqDto reqDto);
}
