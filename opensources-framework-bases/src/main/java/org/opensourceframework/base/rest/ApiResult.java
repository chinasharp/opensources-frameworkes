package org.opensourceframework.base.rest;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ApiResult<T> extends RestResponse<T> {
	private String sign;

	public ApiResult(T res) {
		super(res);
	}

	public ApiResult(int code, String msg) {
		super(code, msg);
	}

	public ApiResult(int code, String msg, T res) {
		super(code, msg, res);
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
