package org.opensourceframework.base.exception;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public enum ExceptionCode {
	SUCCESS(0, "操作成功"),
	FAIL(100000, "操作失败"),
	NOT_SUPPORTED(100001, "渠道不支持该接口"),
	INVOKE(-2, "接口调用失败"),
	VALIDATION_FAIL(1000, "数据验证失败"),
	INVALID_PARAM(2000, "参数不合法"),
	DAO_ERR(60000, " 数据库访问异常"),
	SYSTEM_ERR(100001, "系统异常，请联系管理员");

	private final int code;
	private final String msg;

	ExceptionCode(int val, String info) {
		this.code = val;
		this.msg = info;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}
}
