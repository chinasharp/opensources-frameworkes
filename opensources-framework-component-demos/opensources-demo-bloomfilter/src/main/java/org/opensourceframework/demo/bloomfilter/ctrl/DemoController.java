package org.opensourceframework.demo.bloomfilter.ctrl;

import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.bloomfilter.dao.eo.DemoEo;
import org.opensourceframework.demo.bloomfilter.service.IDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
@Api(tags = "Redisson BloomFilter 使用Demo ")
@RestController
@RequestMapping("/v1/demo/")
public class DemoController {
	@Autowired
	private IDemoService demoService;

	@PostMapping("/check_exist/{id}")
	@ApiOperation(value="使用布鲁过滤器,判断对象是否存在")
	public RestResponse<DemoRespDto> dubboSave(@PathVariable Long id){
		DemoEo demoEo = demoService.findById(id);

		DemoRespDto respDto = null;
		if(demoEo != null) {
			respDto = new DemoRespDto();
			BeanHelper.copyProperties(respDto, demoEo);
		}

		return RestResponse.buildSuccessResponse(respDto);
	}
}
