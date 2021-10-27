package org.opensourceframework.starter.restclient;

import org.opensourceframework.base.helper.codec.RSAHelper;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 验签工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RestSignUtil {
	public RestSignUtil() {
	}

	public static String sign(String appkey, String url, Map<String, String> queryMap, byte[] requestBody, String privateKey) {
		byte[] signData = genSignData(appkey, url, queryMap, requestBody);

		try {
			String sign = RSAHelper.sign(signData, privateKey);
			return sign;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] genSignData(String appkey, String url, Map<String, String> queryMap, byte[] requestBody) {
		try {
			byte[] appkeyBytes = appkey.getBytes(StandardCharsets.UTF_8);
			byte[] urlBytes = url.getBytes(StandardCharsets.UTF_8);
			String queryString = queryMapToString(queryMap);
			byte[] queryStringBytes = queryString.getBytes(StandardCharsets.UTF_8);
			byte[] bytes = new byte[appkeyBytes.length + urlBytes.length + requestBody.length + queryStringBytes.length];
			System.arraycopy(appkeyBytes, 0, bytes, 0, appkeyBytes.length);
			System.arraycopy(urlBytes, 0, bytes, appkeyBytes.length, urlBytes.length);
			System.arraycopy(requestBody, 0, bytes, appkeyBytes.length + urlBytes.length, requestBody.length);
			System.arraycopy(queryStringBytes, 0, bytes, appkeyBytes.length + urlBytes.length + requestBody.length, queryStringBytes.length);
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verify(String appkey, String url, Map<String, String> queryMap, byte[] requestBody, String publicKey, String sign) {
		byte[] signData = genSignData(appkey, url, queryMap, requestBody);

		try {
			return RSAHelper.verify(signData, publicKey, sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verify(String md5String, String publicKey, String sign) {
		try {
			byte[] verifyData = md5String.getBytes(StandardCharsets.UTF_8);
			return RSAHelper.verify(verifyData, publicKey, sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String queryMapToString(Map<String, String> queryMap) {
		if (queryMap.isEmpty()) {
			return "";
		} else {
			List<Map.Entry<String, String>> mappingList = new ArrayList(queryMap.entrySet());
			Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});
			StringBuilder sf = new StringBuilder();
			Iterator iterator = mappingList.iterator();

			while(iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry)iterator.next();
				sf.append(entry.getKey()).append(entry.getValue() == null ? "" : entry.getValue());
			}

			return sf.toString();
		}
	}
}
