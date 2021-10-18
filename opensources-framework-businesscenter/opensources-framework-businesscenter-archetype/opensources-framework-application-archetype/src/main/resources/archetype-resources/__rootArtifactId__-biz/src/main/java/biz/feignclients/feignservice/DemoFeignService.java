#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.feignclients.feignservice;

import ${groupId}.center.${bizName}.api.dto.request.DemoReqDto;
import ${groupId}.center.${bizName}.api.dto.response.DemoRespDto;
import ${groupId}.common.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@FeignClient(name="${symbol_dollar}{provider.user.service.group}" , path = "/v1/op")
public interface DemoFeignService {
	/**
	 * 保存或者更新
	 *
	 * @param demoUserReqDto
	 * @return
	 */
	@PostMapping(value={"/saveOrUpdate"}, produces={"application/json"})
	@ApiOperation(value="保存或者更新Demo信息", notes="保存或者更新Demo信息")
	RestResponse<DemoRespDto> saveOrUpdate(DemoReqDto demoUserReqDto);

	/**
	 * 批量保存
	 *
	 * @param reqDtoList
	 * @return
	 */
	@PostMapping(value={"/batchSave"}, produces={"application/json"})
	@ApiOperation(value="批量保存Demo信息", notes="批量保存Demo信息")
	RestResponse<List<DemoRespDto>> batchSave(List<DemoReqDto> reqDtoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value={"/updateWithNull"}, produces={"application/json"})
	@ApiOperation(value="更新reqDto中的所有属性", notes="更新reqDto中的所有属性")
	RestResponse<Void> updateWithNull(DemoReqDto reqDto);

	/**
	 * 更新所有属性
	 *
	 * @param reqDto
	 * @return
	 */
	@PostMapping(value={"/updateNotNull"}, produces={"application/json"})
	@ApiOperation(value="更新reqDto中不为空的属性", notes="更新reqDto中不为空的属性")
	RestResponse<Void> updateNotNull(DemoReqDto reqDto);
}
