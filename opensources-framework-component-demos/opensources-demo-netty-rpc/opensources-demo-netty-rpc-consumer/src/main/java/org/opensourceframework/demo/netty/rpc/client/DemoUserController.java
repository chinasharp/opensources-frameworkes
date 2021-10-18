package org.opensourceframework.demo.netty.rpc.client;

import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.netty.rpc.api.IDemoUserApi;
import org.opensourceframework.demo.netty.rpc.api.dto.request.DemoUserReqDto;
import org.opensourceframework.demo.netty.rpc.api.dto.response.DemoUserRespDto;
import org.opensourceframework.netty.rpc.core.annotation.RpcConsumer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 */
@Api(tags = "在Application端中使用Netty Rpc调用 ")
@RestController
@RequestMapping("/v1/demo")
public class DemoUserController {
	@RpcConsumer
	private IDemoUserApi demoUserApi;

	@PostMapping("/nettyRpcInvoke")
	@ApiOperation(value="使用netty进行Rpc调用demo", notes="使用netty进行Rpc调用demo")
	public RestResponse<DemoUserRespDto> nettyRpcInvoke(@RequestBody DemoUserReqDto demoUserReqDto){
		return demoUserApi.saveOrUpdate(demoUserReqDto);
	}
}
