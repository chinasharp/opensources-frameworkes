#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.service.impl;

import ${package}.biz.feignclients.feignservice.DemoFeignService;
import ${package}.biz.service.IDemoService;
import ${groupId}.center.${dependency-center-name}.api.dto.request.DemoReqDto;
import ${groupId}.center.${dependency-center-name}.api.dto.response.DemoRespDto;
import ${groupId}.base.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Service
public class DemoServiceImpl implements IDemoService {
	@Lazy
	private DemoFeignService demoFeignService;

	public DemoServiceImpl(DemoFeignService demoFeignService) {
		this.demoFeignService = demoFeignService;
	}

	@Override
	public RestResponse<DemoRespDto> saveOrUpdate(DemoReqDto demoReqDto){
		return demoFeignService.saveOrUpdate(demoReqDto);
	}
}
