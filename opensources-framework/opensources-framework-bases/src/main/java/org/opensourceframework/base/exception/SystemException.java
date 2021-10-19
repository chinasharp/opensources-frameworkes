package org.opensourceframework.base.exception;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SystemException extends BaseRuntimeException {
	public SystemException(int code, String message) {
		super(code, message);
	}

	public SystemException(Throwable ex) {
		super(ExceptionCode.SYSTEM_ERR.getCode(), ex.getMessage(), ex);
	}

	public SystemException(String info) {
		super(ExceptionCode.FAIL.getCode(), info);
	}
}
