#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api;

import ${package}.api.dto.request.DemoReqDto;
import ${package}.api.dto.response.DemoRespDto;
import ${groupId}.base.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 操作类(查询以外的操作)Api示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags={"Demo 操作api"})
public interface IDemoApi {
	/**
	 * 保存或者更新
	 *
	 * @param demoReqDto
	 * @return
	 */
	@ApiOperation(value="保存或者更新Demo信息", notes="保存或者更新Demo信息")
	RestResponse<DemoRespDto> saveOrUpdate(@RequestBody DemoReqDto demoReqDto);

	/**
	 * 批量保存
	 *
	 * @param reqDtoList
	 * @return
	 */
	@ApiOperation(value="批量保存Demo信息", notes="批量保存Demo信息")
	RestResponse<List<DemoRespDto>> batchSave(@RequestBody List<DemoReqDto> reqDtoList);

	/**
	 * 更新不为空的属性
	 *
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value="更新reqDto中的所有属性", notes="更新reqDto中的所有属性")
	RestResponse<Void> updateWithNull(@RequestBody DemoReqDto reqDto);

	/**
	 * 更新所有属性
	 *
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value="更新reqDto中不为空的属性", notes="更新reqDto中不为空的属性")
	RestResponse<Void> updateNotNull(@RequestBody DemoReqDto reqDto);
}
