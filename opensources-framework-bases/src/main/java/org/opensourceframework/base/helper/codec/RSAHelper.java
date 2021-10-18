package org.opensourceframework.base.helper.codec;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class RSAHelper {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	private static final int MAX_ENCRYPT_BLOCK = 117;
	private static final int MAX_DECRYPT_BLOCK = 128;

	public static Map<String, Key> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Key> keyMap = new HashMap(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
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
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return base64Encode(signature.sign());
	}


	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = base64Decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(base64Decode(sign));
	}


	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = base64Decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, privateK);
		return decryptedData(encryptedData , cipher);
	}

	private static byte[] decryptedData(byte[] data , Cipher cipher) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int dataLen = data.length;
		byte[] decryptedData;
		try {
			int offSet = 0;
			int i = 0;
			while (dataLen - offSet > 0) {
				byte[] cache;
				if (dataLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, dataLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			decryptedData = out.toByteArray();
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return decryptedData;
	}


	public static byte[] decryptByPrivateKey(String base64EncryptedData, String privateKey) throws Exception {
		byte[] base64DecodeData = base64Decode(base64EncryptedData);
		return decryptByPrivateKey(base64DecodeData, privateKey);
	}


	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		byte[] keyBytes = base64Decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, publicK);
		return decryptedData(encryptedData , cipher);
	}


	public static byte[] decryptByPublicKey(String base64EncryptedData, String publicKey) throws Exception {
		byte[] decodeData = base64Decode(base64EncryptedData);
		return decryptByPublicKey(decodeData, publicKey);
	}


	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = base64Decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, publicK);
		return encryptedData(data , cipher);
	}

	private static byte[] encryptedData(byte[] data , Cipher cipher) throws Exception{
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] encryptedData;
		try {
			int offSet = 0;
			int i = 0;

			while (inputLen - offSet > 0) {
				byte[] cache;
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			encryptedData = out.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return encryptedData;
	}


	public static String encryptByPublicKeyString(byte[] data, String publicKey) throws Exception {
		byte[] encryptData = encryptByPublicKey(data, publicKey);
		return base64Encode(encryptData);
	}


	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = base64Decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, privateK);
		return encryptedData(data , cipher);
	}


	public static String encryptByPrivateKeyString(byte[] data, String privateKey) throws Exception {
		byte[] encryptData = encryptByPrivateKey(data, privateKey);
		return base64Encode(encryptData);
	}


	public static String getPrivateKey(Map<String, Key> keyMap) throws Exception {
		Key key = keyMap.get("RSAPrivateKey");
		return base64Encode(key.getEncoded());
	}


	public static String getPublicKey(Map<String, Key> keyMap) throws Exception {
		Key key = keyMap.get("RSAPublicKey");
		return base64Encode(key.getEncoded());
	}

	private static String base64Encode(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	private static byte[] base64Decode(String base64Data) {
		return Base64.decodeBase64(base64Data);
	}
}
