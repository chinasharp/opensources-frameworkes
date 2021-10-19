package org.opensourceframework.demo.netty.rpc.api.query;


import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.netty.rpc.api.dto.request.DemoUserReqDto;
import org.opensourceframework.demo.netty.rpc.api.dto.response.DemoUserRespDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags={"demoUser 查询Api"})
@Validated
public interface IDemoUserQueryApi {
	/**
	 * 根据id查询DemoUser信息
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="根据id查询DemoUser信息", notes="根据id查询DemoUser信息 id:主键id")
	RestResponse<DemoUserRespDto> findById(@NotNull(message = "id不能为空")  Long id);

	/**
	 * 根据id集合查询DemoUser信息
	 *
	 * @param idList
	 * @return
	 */
	@ApiOperation(value="根据id集合查询DemoUser列表信息", notes="根据id集合查询DemoUser列表信息 idList:主键id集合")
	RestResponse<DemoUserRespDto> findByIdList( List<Long> idList);

	/**
	 * 根据id字符串查询DemoUser信息
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "根据id字符串查询数据列表",notes = "根据id字符串查询数据列表 ids:主键id字符串以,隔开")
	RestResponse<List<DemoUserRespDto>> findByIds(String ids);

	/**
	 * 分页查询
	 *
	 * @param demoUserReqDto
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value = "根据demoUserReqDto中不为空的属性为条件 分页查询",notes = "根据demoUserReqDto中不为空的属性为条件 分页查询")
	RestResponse<PageInfo<DemoUserRespDto>> findByPage(DemoUserReqDto demoUserReqDto ,
                                                       Integer currentPage, Integer pageSize);
}
