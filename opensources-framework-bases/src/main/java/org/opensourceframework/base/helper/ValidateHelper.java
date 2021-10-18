package org.opensourceframework.base.helper;

import java.util.regex.Pattern;

/**
 * 常见字符串验证帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ValidateHelper {
	public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";
	public static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
	public static final String REGEX_MOBILE = "^1[3|4|5|7|8|9][0-9]{9}$";
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	public static final String REGEX_CHINESE = "^[一-龥],{0,}$";
	public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
	public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

	public static boolean isUsername(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	public static boolean isPassword(String password) {
		return Pattern.matches(REGEX_PASSWORD, password);
	}


	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}


	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}


	public static boolean isChinese(String chinese) {
		return Pattern.matches(REGEX_CHINESE, chinese);
	}


	public static boolean isIDCard(String idCard) {
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}


	public static boolean isUrl(String url) {
		return Pattern.matches(REGEX_URL, url);
	}


	public static boolean isIPAddr(String ipAddr) {
		return Pattern.matches(REGEX_IP_ADDR, ipAddr);
	}
}
