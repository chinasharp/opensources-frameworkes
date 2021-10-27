package org.opensourceframework.common.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ApiSignatureException extends HttpStatusCodeException {
	private int reason;
	private static final long serialVersionUID = 1L;

	public ApiSignatureException(HttpStatus statusCode, String msg) {
		super(statusCode, msg);
	}

	public ApiSignatureException(HttpStatus statusCode, int reason, String msg) {
		super(statusCode, msg);
		this.reason = reason;
	}

	public int getReason() {
		return this.reason;
	}
}
