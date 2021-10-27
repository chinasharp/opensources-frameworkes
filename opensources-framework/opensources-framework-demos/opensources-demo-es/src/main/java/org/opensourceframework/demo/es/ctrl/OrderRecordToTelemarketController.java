package org.opensourceframework.demo.es.ctrl;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.es.api.request.OrderTelemarketReqDto;
import org.opensourceframework.demo.es.api.respones.OrderTelemarketRespDto;
import org.opensourceframework.demo.es.service.IOrderRecordToTelemarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("es 文档示例")
@RestController
@RequestMapping("/es/order_telemarket")
public class OrderRecordToTelemarketController {
	@Autowired
	private IOrderRecordToTelemarketService orderTelemarketService;

	@PostMapping("/query")
	@ApiOperation(value="ES 条件查询", notes="ES条件查询")
	public RestResponse<List<OrderTelemarketRespDto>> queryList(@RequestBody OrderTelemarketReqDto orderTelemarketReqDto){
		RestResponse<List<OrderTelemarketRespDto>> restResponse = orderTelemarketService.queryList(orderTelemarketReqDto);
		return restResponse;
	}


	@PostMapping("/queryPage")
	@ApiOperation(value="ES分页查询", notes="ES分页查询")
	public RestResponse<PageInfo<OrderTelemarketRespDto>> queryPage(@RequestBody OrderTelemarketReqDto orderTelemarketReqDto ,
			@RequestParam(name = "pageNum", defaultValue = "1", required = false) Integer pageNum,
			@RequestParam(name = "pageSize",defaultValue = "10",required = false) Integer pageSize){
		return orderTelemarketService.queryPage(orderTelemarketReqDto , pageNum , pageSize);
	}
}
