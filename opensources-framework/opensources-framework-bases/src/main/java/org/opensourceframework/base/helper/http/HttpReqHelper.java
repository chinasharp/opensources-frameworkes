package org.opensourceframework.base.helper.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * 模拟Http请求帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class HttpReqHelper {
	private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");

	public static String get(String url) throws IOException {
		return get(url, null);
	}

	public static String get(String url, Map<String, String> headers) throws IOException {
		return fetch("GET", url, null, headers);
	}

	public static String post(String url, String body, Map<String, String> headers) throws IOException {
		return fetch("POST", url, body, headers);
	}

	public static String post(String url, String body) throws IOException {
		return post(url, body, null);
	}

	public static String postForm(String url, Map<String, String> params) throws IOException {
		return postForm(url, params, null);
	}

	public static String postForm(String url, Map<String, String> params,
			Map<String, String> headers) throws IOException {
		if (headers == null) {
			headers = new HashMap();
		}
		headers.put("Content-Type", "application/x-www-form-urlencoded");


		String body = "";
		boolean first;
		if (params != null) {
			first = true;
			for(Map.Entry<String, String> entry : params.entrySet()){
				if (first) {
					first = false;
				} else {
					body = body.concat("&");
				}
				String value = entry.getValue();
				body = body.concat(URLEncoder.encode(entry.getKey(), "UTF-8")).concat("=");
				body = body.concat(URLEncoder.encode(value, "UTF-8"));
			}
		}

		return post(url, body, headers);
	}

	public static String put(String url, String body, Map<String, String> headers) throws IOException {
		return fetch("PUT", url, body, headers);
	}

	public static String put(String url, String body) throws IOException {
		return put(url, body, null);
	}

	public static String delete(String url, Map<String, String> headers) throws IOException {
		return fetch("DELETE", url, null, headers);
	}

	public static String delete(String url) throws IOException {
		return delete(url, null);
	}

	public static String appendQueryParams(String url, Map<String, String> params) throws IOException {
		String fullUrl = url;
		boolean first;
		if (params != null) {
			first = fullUrl.indexOf('?') == -1;
			for(Map.Entry<String , String> entry : params.entrySet()){
				if (first) {
					fullUrl = fullUrl.concat("?");
					first = false;
				} else {
					fullUrl = fullUrl.concat("&");
				}
				String value = entry.getValue();
				fullUrl = fullUrl + URLEncoder.encode(entry.getKey(), "UTF-8") + '=';
				fullUrl = fullUrl + URLEncoder.encode(value, "UTF-8");
			}
		}

		return fullUrl;
	}

	public static Map<String, String> getQueryParams(String url) throws IOException {
		Map<String, String> params = new HashMap();

		int start = url.indexOf('?');
		while (start != -1) {
			int equals = url.indexOf('=', start);
			String param = "";
			if (equals != -1) {
				param = url.substring(start + 1, equals);
			} else {
				param = url.substring(start + 1);
			}

			String value = "";
			if (equals != -1) {
				start = url.indexOf('&', equals);
				if (start != -1) {
					value = url.substring(equals + 1, start);
				} else {
					value = url.substring(equals + 1);
				}
			}

			params.put(URLDecoder.decode(param, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
		}

		return params;
	}

	public static String removeQueryParams(String url) throws IOException {
		int q = url.indexOf('?');
		if (q != -1) {
			return url.substring(0, q);
		}
		return url;
	}

	public static String fetch(String method, String url, String body, Map<String, String> headers) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);


		if (method != null) {
			conn.setRequestMethod(method);
		}


		if (headers != null) {
			for(Map.Entry<String , String> entry : headers.entrySet()){
				conn.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}


		if (body != null) {
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(body.getBytes());
			os.flush();
			os.close();
		}
		boolean isGzip = false;
		String charset = null;

		if (conn.getResponseCode() == 200) {


			String contentEncoding = conn.getHeaderField("Content-Encoding");
			if ((contentEncoding != null) && (contentEncoding.equalsIgnoreCase("gzip"))) {
				isGzip = true;
			}
			String contentType = conn.getHeaderField("Content-Type");
			charset = getCharsetFromContentType(contentType);
		}
		InputStream is = conn.getInputStream();
		String response = streamToString(is, charset, isGzip);
		is.close();


		if (conn.getResponseCode() == 301) {
			String location = conn.getHeaderField("Location");
			return fetch(method, location, body, headers);
		}

		return response;
	}

	static String getCharsetFromContentType(String contentType) {
		if (contentType == null){
			return null;
		}

		Matcher m = charsetPattern.matcher(contentType);
		if (m.find()) {
			String charset = m.group(1).trim();
			charset = charset.replace("charset=", "");
			if (charset.length() == 0) {
				return null;
			}
			try {
				if (("gb2312".equalsIgnoreCase(charset)) || ("gbk".equalsIgnoreCase(charset))) {
					charset = "gb18030";
				}
				if (Charset.isSupported(charset)) {
					return charset;
				}
				charset = charset.toUpperCase(Locale.ENGLISH);
				if (Charset.isSupported(charset)) {
					return charset;
				}
			} catch (IllegalCharsetNameException e) {
				return null;
			}
		}
		return null;
	}


	public static String streamToString(InputStream is, String charset, boolean isGzip) throws IOException {
		if ((charset == null) || ("".equals(charset.trim()))) {
			charset = "utf-8";
		}
		BufferedReader reader = null;
		if (isGzip) {
			reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(is), charset));
		} else {
			reader = new BufferedReader(new InputStreamReader(is, charset));
		}
		String line = "";
		StringBuffer result = new StringBuffer();
		while (null != (line = reader.readLine())) {
			if (!"".equals(line)) {
				result.append(line).append("\r\n");
			}
		}

		return result.toString();
	}
}
