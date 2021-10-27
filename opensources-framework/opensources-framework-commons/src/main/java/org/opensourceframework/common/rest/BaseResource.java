package org.opensourceframework.common.rest;

import org.opensourceframework.base.exception.ExceptionCode;
import org.opensourceframework.base.rest.ApiResult;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BaseResource {
	public BaseResource() {
	}

	public <T> ApiResult<T> makeResult(int code, String msg, T res) {
		return new ApiResult(code, msg, res);
	}

	public <T> ApiResult<T> makeResult(ExceptionCode status, T res) {
		return new ApiResult(status.getCode(), status.getMsg(), res);
	}

	public <T> ApiResult<T> makeResult(ExceptionCode status, String detail) {
		return new ApiResult(status.getCode(), status.getMsg() + "：" + detail, null);
	}

	public <T> ApiResult<T> success(String msg, T res) {
		return this.makeResult(ExceptionCode.SUCCESS.getCode(), msg, res);
	}

	public <T> ApiResult<T> success(T res) {
		return this.makeResult(ExceptionCode.SUCCESS, res);
	}

	public <T> ApiResult<T> fail(ExceptionCode error) {
		return this.makeResult(error, null);
	}

	public <T> ApiResult<T> fail(ExceptionCode error, String detail) {
		return this.makeResult(error, detail);
	}

	public <T> ApiResult<T> fail(int code, String msg) {
		return this.makeResult(code, msg, null);
	}

	public <T> ApiResult<T> fail(int code, String msg, T res) {
		return this.makeResult(code, msg, res);
	}

	public <T> ApiResult<T> fail(String msg) {
		return this.makeResult(ExceptionCode.FAIL.getCode(), msg, null);
	}

	public <T> ApiResult<T> fail() {
		return this.makeResult(ExceptionCode.FAIL, null);
	}
}
