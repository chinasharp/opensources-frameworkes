package org.opensourceframework.demo.lock.ctrl;

import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.lock.distributedlock.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Api(tags = "各类锁使用Demo ")
@RestController
@RequestMapping("/v1/demo/lock")
public class LockDemoController {
	@Autowired
	private RedisLock redisLock;

	@PostMapping("/redis")
	@ApiOperation(value="使用redis的分布式锁", notes="使用redis的分布式锁")
	public RestResponse<Void> dubboSave(){
		redisLock.cutPayment("opensourceframework");
		return RestResponse.SUCESS;
	}

}
