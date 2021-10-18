#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.biz.ctrl;

import ${package}.biz.service.IDemoService;
import ${groupId}.center.${bizName}.api.IDemoApi;
import ${groupId}.center.${bizName}.api.dto.request.DemoReqDto;
import ${groupId}.center.${bizName}.api.dto.response.DemoRespDto;
import ${groupId}.common.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags = "Dubbo/SpringCloud-Alibaba/Nacos等 在Application端中混合使用Demo ")
@RestController
@RequestMapping("/v1/user/application")
public class DemoController {
	@Autowired
	private IDemoService demoUserService;

	@Autowired
	private RestTemplate restTemplate;

	@DubboReference(group = "${symbol_dollar}{provider.user.service.group}" , version = "${symbol_dollar}{provider.user.service.version}" , protocol = "${symbol_dollar}{provider.user.service.protocol}")
	private IDemoApi demoUserApi;

	@PostMapping("/feignSave")
	@ApiOperation(value="使用feign调用保存用户信息服务,注册中心为Nacos", notes="使用feign调用保存用户信息服务")
	public RestResponse<DemoRespDto> feignSave(@RequestBody DemoReqDto demoUserReqDto){
		return demoUserService.saveOrUpdate(demoUserReqDto);
	}

	@PostMapping("/loadBalancedRestSave")
	@ApiOperation(value="使用SpringCloud的LoadBalanced RestTemplate调用保存用户信息服务,注册中心为Nacos", notes="使用SpringCloud的LoadBalanced RestTemplate调用保存用户信息服务")
	public RestResponse<DemoRespDto> loadBalancedSave(@RequestBody DemoReqDto demoUserReqDto){
		HttpHeaders header = new HttpHeaders();
		HttpEntity httpEntity = new HttpEntity(demoUserReqDto , header);
		//修改成正确的地址
		ResponseEntity<Object> responseEntity =
				restTemplate.postForEntity("http://opensources-demo-center/v1/demouser/saveOrUpdate",httpEntity , Object.class);
		Object body = responseEntity.getBody();
		RestResponse response = RestResponse.success(body);
		return response;

	}

	@PostMapping("/dubboSave")
	@ApiOperation(value="使用Dubbo调用保存用户信息服务,注册中心为Nacos", notes="使用Dubbo调用保存用户信息服务,注册中心为Nacos")
	public RestResponse<DemoRespDto> dubboSave(@RequestBody DemoReqDto demoUserReqDto){
		return demoUserApi.saveOrUpdate(demoUserReqDto);
	}

}
