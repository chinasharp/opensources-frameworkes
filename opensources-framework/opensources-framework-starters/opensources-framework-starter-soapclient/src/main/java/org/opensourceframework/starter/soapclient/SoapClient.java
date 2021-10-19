package org.opensourceframework.starter.soapclient;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SoapClient {
	private static final Logger logger = LoggerFactory.getLogger(SoapClient.class);

	private final HttpClient httpClient;

	public SoapClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
