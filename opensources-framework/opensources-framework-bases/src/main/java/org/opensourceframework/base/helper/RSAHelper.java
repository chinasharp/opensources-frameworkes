package org.opensourceframework.base.helper;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 加解密帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RSAHelper {
	public static final String KEY_ALGORITHM_RSA = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	private static final String PUBLIC_KEY_RSA = "RSAPublicKey";
	private static final String PRIVATE_KEY_RSA = "RSAPrivateKey";
	private static final int MAX_ENCRYPT_BLOCK = 117;
	private static final int MAX_DECRYPT_BLOCK = 128;

	public RSAHelper() {
	}

	public static Map<String, Key> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		Map<String, Key> keyMap = new HashMap(2);
		keyMap.put(PUBLIC_KEY_RSA, publicKey);
		keyMap.put(PRIVATE_KEY_RSA, privateKey);
		return keyMap;
	}

	public static String[] genKeyPairString() throws Exception {
		String[] keyPair = new String[2];
		Map<String, Key> keyMap = genKeyPair();
		keyPair[0] = getPrivateKey(keyMap);
		keyPair[1] = getPublicKey(keyMap);
		return keyPair;
	}

	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = base64Decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return base64Encode(signature.sign());
	}

	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = base64Decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(base64Decode(sign));
	}

	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = base64Decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Throwable throwable = null;

		try {
			int offSet = 0;

			for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
				byte[] cache;
				if (inputLen - offSet > 128) {
					cache = cipher.doFinal(encryptedData, offSet, 128);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}

				out.write(cache, 0, cache.length);
				++i;
			}

			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} catch (Throwable t) {
			throwable = t;
			throw t;
		} finally {
			if (out != null) {
				if (throwable != null) {
					try {
						out.close();
					} catch (Throwable t) {
						throwable.addSuppressed(t);
					}
				} else {
					out.close();
				}
			}

		}
	}

	public static byte[] decryptByPrivateKey(String base64EncryptedData, String privateKey) throws Exception {
		byte[] base64DecodeData = base64Decode(base64EncryptedData);
		return decryptByPrivateKey(base64DecodeData, privateKey);
	}

	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		byte[] keyBytes = base64Decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Throwable throwable = null;

		try {
			int offSet = 0;

			for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
				byte[] cache;
				if (inputLen - offSet > 128) {
					cache = cipher.doFinal(encryptedData, offSet, 128);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}

				out.write(cache, 0, cache.length);
				++i;
			}

			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} catch (Throwable t) {
			throwable = t;
			throw t;
		} finally {
			if (out != null) {
				if (throwable != null) {
					try {
						out.close();
					} catch (Throwable t) {
						throwable.addSuppressed(t);
					}
				} else {
					out.close();
				}
			}

		}
	}

	public static byte[] decryptByPublicKey(String base64EncryptedData, String publicKey) throws Exception {
		byte[] decodeData = base64Decode(base64EncryptedData);
		return decryptByPublicKey(decodeData, publicKey);
	}

	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = base64Decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Throwable throwable = null;

		try {
			int offSet = 0;

			for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
				byte[] cache;
				if (inputLen - offSet > 117) {
					cache = cipher.doFinal(data, offSet, 117);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}

				out.write(cache, 0, cache.length);
				++i;
			}

			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Throwable t) {
			throwable = t;
			throw t;
		} finally {
			if (out != null) {
				if (throwable != null) {
					try {
						out.close();
					} catch (Throwable t) {
						throwable.addSuppressed(t);
					}
				} else {
					out.close();
				}
			}

		}
	}

	public static String encryptByPublicKeyString(byte[] data, String publicKey) throws Exception {
		byte[] encryptData = encryptByPublicKey(data, publicKey);
		return base64Encode(encryptData);
	}

	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = base64Decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Throwable throwable = null;

		try {
			int offSet = 0;

			for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
				byte[] cache;
				if (inputLen - offSet > 117) {
					cache = cipher.doFinal(data, offSet, 117);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}

				out.write(cache, 0, cache.length);
				++i;
			}

			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Throwable t) {
			throwable = t;
			throw t;
		} finally {
			if (out != null) {
				if (throwable != null) {
					try {
						out.close();
					} catch (Throwable t) {
						throwable.addSuppressed(t);
					}
				} else {
					out.close();
				}
			}

		}
	}

	public static String encryptByPrivateKeyString(byte[] data, String privateKey) throws Exception {
		byte[] encryptData = encryptByPrivateKey(data, privateKey);
		return base64Encode(encryptData);
	}

	public static String getPrivateKey(Map<String, Key> keyMap) throws Exception {
		Key key = keyMap.get(PRIVATE_KEY_RSA);
		return base64Encode(key.getEncoded());
	}

	public static String getPublicKey(Map<String, Key> keyMap) throws Exception {
		Key key = keyMap.get(PUBLIC_KEY_RSA);
		return base64Encode(key.getEncoded());
	}

	private static String base64Encode(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	private static byte[] base64Decode(String base64Data) {
		return Base64.getDecoder().decode(base64Data);
	}
}
