package org.opensourceframework.base.helper.codec;

import com.google.common.base.Strings;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * 图片 和 Base64字符串 相互转换帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class Base64Helper {
	private static Random random;

	/**
	 * IMG文件转化为Base64字符串
	 *
	 * @param filePath
	 * @return
	 */
	public static String imgToBase64(String filePath) {
		InputStream in = null;
		byte[] data = null;

		try {
			in = new FileInputStream(filePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Encode(data);
	}

	/**
	 * Base64字符串转化为图片文件
	 *
	 * @param base64
	 * @param filePath
	 * @return
	 */
	public static boolean base64ToImg(String base64, String filePath) {
		if (base64 == null) {
			return false;
		}

		try {
			byte[] b = base64Decode(base64);
			for (int i = 0; i < b.length; i++) {
				if (b[i] < 0) {
					int tempIndex = i;
					byte[] tempBytes = b;
					tempBytes[tempIndex] = ((byte) (tempBytes[tempIndex] + 256));
				}
			}

			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			OutputStream out = new FileOutputStream(file);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
		}

		return false;
	}

	/**
	 * Byte数组转化为Base64字符串
	 *
	 * @param data
	 * @return
	 */
	public static String base64Encode(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	/**
	 * 字符串转化为Base64字符串
	 *
	 * @param data
	 * @return
	 */
	public static String base64Encode(String data) {
		assert (!Strings.isNullOrEmpty(data));

		try {
			return Base64.encodeBase64String(data.getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Base64字符串转化为Byte数组
	 *
	 * @param data
	 * @return
	 */
	public static byte[] base64Decode(String data) {
		assert (!Strings.isNullOrEmpty(data));
		return Base64.decodeBase64(data);
	}

	/**
	 * Base64字符串转化为字符串
	 *
	 * @param data
	 * @return
	 */
	public static String base64DecodeToStr(String data) {
		assert (!Strings.isNullOrEmpty(data));
		try {
			return new String(Base64.decodeBase64(data), StandardCharsets.UTF_8);
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 *
	 * @param src
	 * @return
	 */
	public static String md5Hex(String src) {
		return DigestUtils.md5Hex(src);
	}

	public static String md5Hex(String src, String prefix) {
		StringBuilder builder = new StringBuilder();
		builder.append(prefix).append(DigestUtils.md5Hex(src));
		return builder.toString();
	}

	public static String uuid() {
		return uuid("");
	}

	public static String uuid(String prefix) {
		return prefix.concat(UUID.randomUUID().toString());
	}

	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 获取唯一字符串
	 *
	 * @return
	 */
	public static String uniqueCode() {
		return md5Hex(uuid());
	}


	private static String randomNum(int length) {
		if (null == random) {
			try {
				random = SecureRandom.getInstance("SHA1PRNG");
				random.setSeed(System.nanoTime());
			} catch (NoSuchAlgorithmException ex) {
				if (random == null) {
					random = new Random(System.nanoTime());
				}
			}
		}

		Double p = Double.valueOf(Math.pow(10.0D, length));

		long min = p.longValue();

		long nextLong = random.nextLong();
		long value = (nextLong < 0L ? -nextLong : nextLong) % min;


		String fm = "%0" + length + "d";

		return String.format(fm, Long.valueOf(value));
	}
}
