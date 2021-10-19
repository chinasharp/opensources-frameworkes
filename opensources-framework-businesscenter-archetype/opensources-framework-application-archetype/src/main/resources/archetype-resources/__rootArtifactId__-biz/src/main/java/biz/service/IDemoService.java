#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.service;

import ${groupId}.center.${bizName}.api.dto.request.DemoReqDto;
import ${groupId}.center.${bizName}.api.dto.response.DemoRespDto;
import ${groupId}.common.rest.RestResponse;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IDemoService {
	RestResponse<DemoRespDto> saveOrUpdate(DemoReqDto demoUserReqDto);
}
