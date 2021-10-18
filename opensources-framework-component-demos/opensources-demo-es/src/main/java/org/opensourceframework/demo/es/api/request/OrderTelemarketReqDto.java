package org.opensourceframework.demo.es.api.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/4/25 下午7:46
 */
@ApiModel(value = "OrderTelemarketReqDto",description = "请求参数对象")
public class OrderTelemarketReqDto implements Serializable {
	@ApiModelProperty(name = "orderNo" , value = "订单号")
	private String orderNo;

	@ApiModelProperty(name = "telemarketId" , value = "电销单id")
	private String telemarketId;

	public OrderTelemarketReqDto() {
	}

	public OrderTelemarketReqDto(String orderNo, String telemarketId) {
		this.orderNo = orderNo;
		this.telemarketId = telemarketId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTelemarketId() {
		return telemarketId;
	}

	public void setTelemarketId(String telemarketId) {
		this.telemarketId = telemarketId;
	}
}
