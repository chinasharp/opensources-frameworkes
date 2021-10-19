package org.opensourceframework.application.demo.biz.ctrl;

import org.opensourceframework.application.demo.biz.service.IDemoService;
import org.opensourceframework.center.demo.api.op.user.IUserDemoApi;
import org.opensourceframework.center.demo.api.dto.request.user.DemoUserReqDto;
import org.opensourceframework.center.demo.api.dto.response.user.DemoUserRespDto;
import org.opensourceframework.base.rest.RestResponse;
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
@RequestMapping("/v1/demo/application")
public class DemoController {
	@Autowired
	private IDemoService demoUserService;

	@Autowired
	private RestTemplate restTemplate;

	@DubboReference(group = "${provider.user.service.group}" , version = "${provider.user.service.version}" , protocol = "${provider.user.service.protocol}")
	private IUserDemoApi demoUserApi;

	@PostMapping("/feignSave")
	@ApiOperation(value="使用feign调用保存用户信息服务,注册中心为Nacos", notes="使用feign调用保存用户信息服务")
	public RestResponse<DemoUserRespDto> feignSave(@RequestBody DemoUserReqDto demoUserReqDto){
		return demoUserService.saveOrUpdate(demoUserReqDto);
	}

	@PostMapping("/loadBalancedRestSave")
	@ApiOperation(value="使用SpringCloud的LoadBalanced RestTemplate调用保存用户信息服务,注册中心为Nacos", notes="使用SpringCloud的LoadBalanced RestTemplate调用保存用户信息服务")
	public RestResponse<DemoUserRespDto> loadBalancedSave(@RequestBody DemoUserReqDto demoUserReqDto){
		HttpHeaders header = new HttpHeaders();
		HttpEntity httpEntity = new HttpEntity(demoUserReqDto , header);
		//修改成正确的地址
		ResponseEntity<Object> responseEntity =
				restTemplate.postForEntity("http://opensources-demo-center/v1/demouser/saveOrUpdate",httpEntity , Object.class);
		Object body = responseEntity.getBody();
		RestResponse response = RestResponse.buildSuccessResponse(body);
		return response;

	}

	@PostMapping("/dubboSave")
	@ApiOperation(value="使用Dubbo调用保存用户信息服务,注册中心为Nacos", notes="使用Dubbo调用保存用户信息服务,注册中心为Nacos")
	public RestResponse<DemoUserRespDto> dubboSave(@RequestBody DemoUserReqDto demoUserReqDto){
		return demoUserApi.saveOrUpdate(demoUserReqDto);
	}

}
