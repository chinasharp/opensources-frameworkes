package org.opensourceframework.base.exception;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.rest.RestResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础运行异常类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	protected static final Map<Integer, String> mapMessage = new ConcurrentHashMap<>();
	public static final int SYSTEM_INTERNAL_ERROR = 10001;
	public static final int PARAMETER_ERROR = 10002;
	public static final int RECORD_DUPLICATED = 11001;
	public static final int RECORD_NOTEXIST = 11002;
	public static final int APIGATEWAY_ERROR = 99999;
	protected Integer code;
	protected String errMsg;

	public BaseRuntimeException(String message) {
		this(10001, message);
	}

	public BaseRuntimeException(String message, Throwable e) {
		this(10001, message, e);
	}

	public BaseRuntimeException(int code, String message) {
		super("{\"code\":\"" + code + "\", \"message\":\"" + message + "\"}");
		this.code = code;
		this.errMsg = message;
	}

	public BaseRuntimeException(int code, String message, Throwable e) {
		super("{\"code\":\"" + code + "\", \"message\":\"" + message + "\"}", e);
		this.code = code;
		this.errMsg = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getRawMessage() {
		return this.errMsg;
	}

	public String toRestResponseJson() {
		return JSON.toJSONString(new RestResponse(this.code, this.errMsg,null));
	}

	public static String getMessage(int code) {
		return mapMessage.get(code);
	}

	static {
		mapMessage.put(10001, "系统内部错误");
		mapMessage.put(10002, "业务参数异常");
		mapMessage.put(11001, "记录重复");
		mapMessage.put(11002, "记录不存在");
	}
}
