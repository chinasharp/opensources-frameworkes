package org.opensourceframework.demo.es.service;

import com.github.pagehelper.PageInfo;
import org.opensourceframework.base.rest.RestResponse;
import org.opensourceframework.demo.es.api.request.OrderTelemarketReqDto;
import org.opensourceframework.demo.es.api.respones.OrderTelemarketRespDto;

import java.util.List;

/**
 * * @author yu.ce@foxmail.com
 * 
 */


public interface IOrderRecordToTelemarketService {
	RestResponse<List<OrderTelemarketRespDto>> queryList(OrderTelemarketReqDto orderTelemarketReqDto);

	RestResponse<PageInfo<OrderTelemarketRespDto>> queryPage(OrderTelemarketReqDto orderTelemarketReqDto , Integer pageNum , Integer pageSize);
}
