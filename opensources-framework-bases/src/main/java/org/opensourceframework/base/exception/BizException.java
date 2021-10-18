package org.opensourceframework.base.exception;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class BizException extends BaseRuntimeException {
	public BizException(String info) {
		super(ExceptionCode.FAIL.getCode(), info);
	}

	public BizException(int errCode, String errMsg) {
		super(errCode, errMsg);
	}
}
