package org.opensourceframework.base.helper.codec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密解密帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class AESHelper {
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		SecretKey secretKey = buildSecretKey(encryptKey);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(1, secretKey);
		return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
	}

	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		return Base64.getEncoder().encodeToString(aesEncryptToBytes(content, encryptKey));
	}

	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		SecretKey secretKey = buildSecretKey(decryptKey);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(2, secretKey);
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes);
	}

	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
		return aesDecryptByBytes(Base64.getDecoder().decode(encryptStr), decryptKey);
	}

	private static SecretKey buildSecretKey(String cipherKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(cipherKey.getBytes());
		kgen.init(128, random);
		SecretKey secretKey = new SecretKeySpec(kgen.generateKey().getEncoded(), "AES");
		return secretKey;
	}
}
