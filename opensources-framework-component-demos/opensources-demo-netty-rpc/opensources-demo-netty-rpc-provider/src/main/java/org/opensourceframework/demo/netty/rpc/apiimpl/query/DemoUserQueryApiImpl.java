package org.opensourceframework.demo.netty.rpc.apiimpl.query;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.netty.rpc.api.dto.request.DemoUserReqDto;
import org.opensourceframework.demo.netty.rpc.api.dto.response.DemoUserRespDto;
import org.opensourceframework.demo.netty.rpc.api.query.IDemoUserQueryApi;

import java.util.List;

/**
 * Demo 查询操作Api实现类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class DemoUserQueryApiImpl implements IDemoUserQueryApi {
	/**
	 * 根据id查询DemoUser信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public RestResponse<DemoUserRespDto> findById(Long id){
		return null;
	}

	/**
	 * 根据id集合查询DemoUser信息
	 *
	 * @param idList
	 * @return
	 */
	@Override
	public RestResponse<DemoUserRespDto> findByIdList(List<Long> idList){
		return null;
	}

	/**
	 * 根据id字符串查询DemoUser信息
	 *
	 * @param ids
	 * @return
	 */
	@Override
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
	public RestResponse<PageInfo<DemoUserRespDto>> findByPage(DemoUserReqDto demoUserReqDto, Integer currentPage,
			Integer pageSize) {
		return null;
	}
}
