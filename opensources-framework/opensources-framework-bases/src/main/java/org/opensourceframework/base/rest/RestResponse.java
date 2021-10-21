package org.opensourceframework.base.rest;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.opensourceframework.base.constants.CommonCanstant;

import java.io.Serializable;
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
	@ApiModelProperty( name = "code", value = "请求响应码")
	protected Integer code;

	/**
	 * 响应信息
	 */
	@ApiModelProperty( name = "message", value = "请求响应信息")
	protected String message;

	/**
	 * 返回数据
	 */
	@ApiModelProperty( name = "data", value = "请求响应数据")
	protected T data;

	@JsonIgnore
	@JSONField(serialize = false)
	protected String exceptCauseIp;
	@JSONField(serialize = false)
	protected String exceptCauseApp;
	@JSONField(serialize = false)
	protected String exceptClass;

	public static final RestResponse<Void> SUCESS = new RestResponse(CommonCanstant.SUCCESS_CODE, CommonCanstant.SUCESS_MSG, null);
	public static final RestResponse<Void> FAILURE = new RestResponse(CommonCanstant.FAILURE_CODE, CommonCanstant.FAILURE_MSG, null);
	public static final RestResponse<Void>	ERROR =  new RestResponse(CommonCanstant.FAILURE_CODE, CommonCanstant.FAILURE_MSG, null);

	public static final RestResponse<Boolean> TRUE = new RestResponse(CommonCanstant.SUCCESS_CODE, CommonCanstant.SUCESS_MSG, CommonCanstant.TRUE);
	public static final RestResponse<Boolean> FALSE = new RestResponse(CommonCanstant.SUCCESS_CODE, CommonCanstant.SUCESS_MSG, CommonCanstant.FALSE);


	public RestResponse() {
		this(null);
	}

	public RestResponse(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public RestResponse(T data) {
		this.code = CommonCanstant.SUCCESS_CODE;
		this.message = CommonCanstant.SUCESS_MSG;
		this.data = data;
	}

	public RestResponse(Integer code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static <T> RestResponse error(String errorMessage){return new RestResponse(CommonCanstant.ERROR_CODE, errorMessage);}
	public static <T> RestResponse error(Integer errorCode ,String errorMessage){return new RestResponse(errorCode, errorMessage);}
	public static <T> RestResponse success(Integer code , String message){return new RestResponse(code, message);}
	public static <T> RestResponse success(Integer code , String message , T data){return new RestResponse(code ,message , data); }
	public static <T> RestResponse success(T data){return new RestResponse(CommonCanstant.SUCCESS_CODE ,CommonCanstant.SUCESS_MSG , data);}
	public static <T> RestResponse fail(Integer failCode ,String failMessage){
		return new RestResponse(failCode, failMessage);
	}

	public static <T> RestResponse<T> build(Integer code, String message, T data) {
		return new RestResponse(code, message, data);
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap();
		map.put("code", this.code);
		map.put("message", this.message);
		map.put("data", this.data);
		return map;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
	public boolean isSuccess() {
		return this != null && this.getCode() == 0;
	}

	@Override
	public String toString() {
		return "RestResponse [code=" + this.code + ", message=" + this.message + ", data=" + this.data + "]";
	}
}
