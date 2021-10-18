package org.opensourceframework.commons.rest;

import org.opensourceframework.base.exception.BaseRuntimeException;
import org.opensourceframework.base.exception.ExceptionCode;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ResourceException extends BaseRuntimeException {
	private String causeIp;
	private String causeModule;

	public static String getModuleName() {
		return System.getProperty("spring.application.name");
	}

	public ResourceException() {
		this(ExceptionCode.FAIL.getCode(), ExceptionCode.FAIL.getMsg());
	}

	public ResourceException(int code, String message) {
		super(code, message);
	}

	public ResourceException(int code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public String getCauseIp() {
		return this.causeIp;
	}

	public void setCauseIp(String causeIp) {
		this.causeIp = causeIp;
	}

	public String getCauseModule() {
		return this.causeModule;
	}

	public void setCauseModule(String causeModule) {
		this.causeModule = causeModule;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ip:").append(OpensourceFrameworkSystem.getIp()).append(" Application:").append(getCauseModule());
		sb.append(" case:").append(getRawMessage());
		return sb.toString();
	}
}
