package org.opensourceframework.center.demo.api.query;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.center.demo.api.dto.request.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.DemoUserRespDto;
import org.opensourceframework.base.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 查询类Api示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags={"demoUser 查询Api"})
public interface IDemoUserQueryApi {
	/**
	 * 根据id查询Demo信息
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="根据id查询Demo信息", notes="根据id查询Demo信息 id:主键id")
	RestResponse<DemoUserRespDto> findById(@NotNull(message = "id不能为空") @PathVariable(name = "id") Long id);

	/**
	 * 根据id集合查询Demo信息
	 *
	 * @param idList
	 * @return
	 */
	@ApiOperation(value="根据id集合查询Demo列表信息", notes="根据id集合查询Demo列表信息 idList:主键id集合")
	RestResponse<DemoUserRespDto> findByIdList(@RequestBody List<Long> idList);

	/**
	 * 根据id字符串查询Demo信息
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "根据id字符串查询数据列表",notes = "根据id字符串查询数据列表 ids:主键id字符串以,隔开")
	RestResponse<List<DemoUserRespDto>> findByIds(@RequestParam(name = "ids", required = true) String ids);

	/**
	 * 分页查询
	 *
	 * @param demoUserReqDto
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value = "根据demoReqDto中不为空的属性为条件 分页查询",notes = "根据demoReqDto中不为空的属性为条件 分页查询")
	RestResponse<PageInfo<DemoUserRespDto>> findPage(@RequestBody DemoUserReqDto demoUserReqDto,
													 @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
													 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);
}
