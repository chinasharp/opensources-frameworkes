package org.opensourceframework.base.helper.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**
 * MD5工具类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MD5Helper {
	private static final Logger logger = LoggerFactory.getLogger(MD5Helper.class);

	private static final String KEY_MD5 = "MD5";

	private static final String[] STR_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


	public static String getMd5ByString(String sourceString) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(KEY_MD5);
			digest.update(sourceString.getBytes(StandardCharsets.UTF_8));
			byte[] b = digest.digest();
			StringBuilder buf = new StringBuilder();
			for (int offset = 0; offset < b.length; offset++) {
				int i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString().toUpperCase();
		} catch (Exception e) {
			logger.error("获取该字符串的MD5值失败！", e);
		}
		return result;
	}


	public static String getMD5ByInputStream(InputStream is) {
		StringBuffer md5 = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] dataBytes = new byte['Ѐ'];

			int nread = 0;
			while ((nread = is.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			byte[] mdbytes = md.digest();


			for (int i = 0; i < mdbytes.length; i++) {
				md5.append(Integer.toString((mdbytes[i] & 0xFF) + 256, 16).substring(1));
			}
		} catch (Exception e) {
			logger.error("获取该输入流的MD5值失败！", e);
		}
		return md5.toString().toUpperCase();
	}


	public static String getMD5ByFile(File file) {
		String md5 = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			md5 = getMD5ByInputStream(fis);
		} catch (FileNotFoundException e) {
			logger.error("获取该文件的MD5值失败！", e);
		}
		if(md5 != null) {
			return md5.toUpperCase();
		}else {
			return null;
		}
	}


	public static String getMD5Code(String strObj) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			return byteToString(md.digest(strObj.getBytes()));
		} catch (Exception e) {
			logger.error("前端jquery.md5.js加密 对应 后端MD5加密失败！", e);
		}
		return null;
	}


	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return STR_DIGITS[iD1] + STR_DIGITS[iD2];
	}


	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	/*
	public static void main(String[] args) throws Exception{
		String source = "000000";
		System.out.println(getMd5ByString(source));
	}
    */
}
