package org.opensourceframework.commons.log;

import org.opensourceframework.base.helper.IpAddressHelper;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 生成requestId
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RequestId {
	private static final long SYSTEM_CURRENT_MILLIS = System.currentTimeMillis();
	private static final String IP = IpAddressHelper.resolveLocalIp();
	private static final AtomicLong lastId = new AtomicLong();

	public RequestId() {
	}

	public static String createReqId() {
		return hexIp(IP) + "-" + Long.toString(SYSTEM_CURRENT_MILLIS, 36).toUpperCase() + "-" + lastId.incrementAndGet();
	}

	private static String hexIp(String ip) {
		StringBuilder sb = new StringBuilder();
		String[] ipArray = ip.split("\\.");
		int length = ipArray.length;

		for (int i = 0; i < length; ++i) {
			String seg = ipArray[i];
			String h = Integer.toHexString(Integer.parseInt(seg));
			if (h.length() == 1) {
				sb.append("0");
			}

			sb.append(h);
		}

		return sb.toString();
	}
}
