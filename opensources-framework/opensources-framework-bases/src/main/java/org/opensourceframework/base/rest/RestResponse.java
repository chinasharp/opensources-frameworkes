package org.opensourceframework.base.rest;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.opensourceframework.base.constants.CommonCanstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ApiModel(value = "RestResponse",description = "请求响应对象")
public class RestResponse<T> implements Serializable {
	/**
	 * 响应码
	 */
	@ApiModelProperty( name = "errCode", value = "发送响应码")
	protected int errCode;

    @ApiModelProperty( name = "resultStatus", value = "结果状态码")
	protected String resultStatus;

	/**
	 * 响应信息
	 */
	@ApiModelProperty( name = "errMsg", value = "发送响应信息")
	protected String errMsg;

	/**
	 * 返回数据
	 */
	@ApiModelProperty( name = "data", value = "发送响应数据")
	protected T data;

	@JsonIgnore
	@JSONField(serialize = false)
	protected String exceptCauseIp;
	@JSONField(serialize = false)
	protected String exceptCauseApp;
	@JSONField(serialize = false)
	protected String exceptClass;

	public static final RestResponse<Void> VOID = new RestResponse(null);
	public static final RestResponse<Boolean> TRUE = new RestResponse(CommonCanstant.SUCCESS_CODE, CommonCanstant.SUCESS_MSG, Boolean.TRUE);
	public static final RestResponse<Boolean> FALSE = new RestResponse(CommonCanstant.FAILURE_CODE, CommonCanstant.FAILURE_MSG, Boolean.FALSE);

	public RestResponse() {
		this(null);
	}

	public RestResponse(T data) {
		this.errCode = CommonCanstant.SUCCESS_CODE;
		this.errMsg = CommonCanstant.SUCESS_MSG;
		this.data = data;
	}

	public RestResponse(int errCode, String failMessage) {
		this.errCode = errCode;
		this.errMsg = failMessage;
	}

	public static <T> RestResponse error(String errorMessage){
		return new RestResponse(CommonCanstant.ERROR_CODE, errorMessage);
	}

	public static <T> RestResponse instance(int errCode , String errorMessage){
		return new RestResponse(errCode, errorMessage);
	}
	public static <T> RestResponse instance(int errCode ,String resultStatus , String errorMessage){
		return new RestResponse(errCode,resultStatus , errorMessage);
	}

	public static <T> RestResponse fail(int errCode ,String failMessage){
		return new RestResponse(errCode, failMessage);
	}


	public RestResponse(int errCode, String errMsg, T data) {
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.data = data;
	}
	
	public static <T> RestResponse<T> build(int errCode, String errMsg, T data) {
		return new RestResponse(errCode, errMsg, data);
	}

	public RestResponse(int errCode, String resultStatus, String errMsg) {
		this.errCode = errCode;
		this.resultStatus = resultStatus;
		this.errMsg = errMsg;
	}

	public static <T> RestResponse<T> buildSuccessResponse(String errMsg, T data) {
		return new RestResponse(0, errMsg, data);
	}

	public static <T> RestResponse<T> buildSuccessResponse(T data) {
		return buildSuccessResponse("操作成功", data);
	}


	public static RestResponse<Long> createLong(Long value) {
		return new RestResponse(value);
	}

	public static RestResponse<Short> createShort(Short value) {
		return new RestResponse(value);
	}

	public static RestResponse<Integer> createInteger(Integer value) {
		return new RestResponse(value);
	}

	public static RestResponse<Float> createFloat(Float value) {
		return new RestResponse(value);
	}

	public static RestResponse<Double> createDouble(Double value) {
		return new RestResponse(value);
	}

	public static RestResponse<BigDecimal> createBigDecimal(BigDecimal value) {
		return new RestResponse(value);
	}

	public static RestResponse<Object> createObject(Object obj) {
		return new RestResponse(obj);
	}

	public String getExceptCauseIp() {
		return this.exceptCauseIp;
	}

	public void setExceptCauseIp(String exceptCauseIp) {
		this.exceptCauseIp = exceptCauseIp;
	}

	public String getExceptCauseApp() {
		return this.exceptCauseApp;
	}

	public void setExceptCauseApp(String exceptCauseApp) {
		this.exceptCauseApp = exceptCauseApp;
	}

	public String getExceptClass() {
		return this.exceptClass;
	}

	public void setExceptClass(String exceptClass) {
		this.exceptClass = exceptClass;
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap();
		map.put("errCode", this.errCode);
		map.put("errMsg", this.errMsg);
		map.put("data", this.data);
		return map;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public int getErrCode() {
		return this.errCode;
	}
	public boolean isSuccess() {
		return this != null && this.getErrCode() == 0;
	}

	@Override
	public String toString() {
		return "RestResponse [errCode=" + this.errCode + ", errMsg=" + this.errMsg + ", data=" + this.data + "]";
	}
}
