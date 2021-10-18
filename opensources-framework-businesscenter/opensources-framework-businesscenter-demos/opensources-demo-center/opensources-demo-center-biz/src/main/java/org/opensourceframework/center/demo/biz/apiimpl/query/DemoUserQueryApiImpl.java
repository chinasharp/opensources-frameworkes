package org.opensourceframework.center.demo.biz.apiimpl.query;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.center.demo.api.dto.request.user.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.user.DemoUserRespDto;
import org.opensourceframework.center.demo.api.query.user.IDemoUserQueryApi;
import org.opensourceframework.base.rest.RestResponse;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Api实现示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@DubboService(group = "${dubbo.service.group}", version = "${dubbo.service.version}", protocol = "dubbo")
@RestController
@RequestMapping("/v1/query")
public class DemoUserQueryApiImpl implements IDemoUserQueryApi {
	/**
	 * 根据id查询Demo信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	@GetMapping("/findById/{id}")
	public RestResponse<DemoUserRespDto> findById(Long id){
		return null;
	}

	/**
	 * 根据id集合查询Demo信息
	 *
	 * @param idList
	 * @return
	 */
	@Override
	@PostMapping("/findByIdList")
	public RestResponse<DemoUserRespDto> findByIdList(List<Long> idList){
		return null;
	}

	/**
	 * 根据id字符串查询Demo信息
	 *
	 * @param ids
	 * @return
	 */
	@Override
	@PostMapping(value = "/findByIds" , produces = "application/json")
	public RestResponse<List<DemoUserRespDto>> findByIds(String ids){
		return null;
	}

	/**
	 * 分页查询
	 *
	 * @param demoUserReqDto
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	@PostMapping(value = "/findPage" , produces = "application/json")
	public RestResponse<PageInfo<DemoUserRespDto>> findPage(DemoUserReqDto demoUserReqDto, Integer currentPage, Integer pageSize){
		return null;
	}
}
