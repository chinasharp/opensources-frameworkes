package org.opensourceframework.commons.http;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 包装request请求,重写HttpServletRequestWrapper的getReader 和 getInputStream 使其能重复读取
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class HttpRequestWrapper extends HttpServletRequestWrapper {
	private final byte[] bodyByte;

	private final String bodyString;

	public HttpRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		this.bodyString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
		this.bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);
	}

	public String getBodyString() {
		return this.bodyString;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(bodyByte);

		return new ServletInputStream() {

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

}
