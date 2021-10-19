#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.apiimpl.query;

import com.github.pagehelper.PageInfo;
import ${package}.api.dto.request.DemoReqDto;
import ${package}.api.dto.response.DemoRespDto;
import ${package}.api.query.IDemoQueryApi;
import ${groupId}.common.rest.RestResponse;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Api实现示例
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@DubboService(group = "${symbol_dollar}{dubbo.service.group}", version = "${symbol_dollar}{dubbo.service.version}", protocol = "dubbo")
@RestController
@RequestMapping("/v1/query")
public class DemoQueryApiImpl implements IDemoQueryApi {
	/**
	 * 根据id查询Demo信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	@GetMapping("/findById/{id}")
	public RestResponse<DemoRespDto> findById(Long id){
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
	public RestResponse<DemoRespDto> findByIdList(List<Long> idList){
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
	public RestResponse<List<DemoRespDto>> findByIds(String ids){
		return null;
	}

	/**
	 * 分页查询
	 *
	 * @param demoReqDto
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	@PostMapping(value = "/findPage" , produces = "application/json")
	public RestResponse<PageInfo<DemoRespDto>> findPage(DemoReqDto demoReqDto,Integer currentPage,Integer pageSize){
		return null;
	}
}
