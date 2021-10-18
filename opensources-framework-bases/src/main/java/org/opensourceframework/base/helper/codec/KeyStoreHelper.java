package org.opensourceframework.base.helper.codec;

import org.apache.commons.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 公/私钥生成帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class KeyStoreHelper {
	public static final String KEY_STORE = "JKS";
	public static final String X509 = "X.509";
	private final KeyStore keyStore;

	private KeyStoreHelper(KeyStore keyStore) {
		this.keyStore = keyStore;
	}

	public static KeyStoreHelper getKeyStore(InputStream is, String password) throws Exception {
		KeyStoreHelper ksHelper = new KeyStoreHelper(KeyStore.getInstance("JKS"));
		ksHelper.getKeyStore().load(is, password.toCharArray());
		return ksHelper;
	}

	public static KeyStoreHelper getKeyStore(String keyStorePath, String password) throws Exception {
		KeyStoreHelper kutil = new KeyStoreHelper(KeyStore.getInstance("JKS"));

		FileInputStream is = new FileInputStream(keyStorePath);
		Throwable localThrowable3 = null;
		try {
			kutil.getKeyStore().load(is, password.toCharArray());
		} catch (Throwable localThrowable1) {
			localThrowable3 = localThrowable1;
			throw localThrowable1;
		} finally {
			if (is != null) {
				if (localThrowable3 != null) {
					try {
						is.close();
					} catch (Throwable localThrowable2) {
						localThrowable3.addSuppressed(localThrowable2);
					}
				} else {
					is.close();
				}
			}
		}
		return kutil;
	}

	public KeyStore getKeyStore() {
		return this.keyStore;
	}

	/**
	 * 获取私钥
	 *
	 * @param alias
	 * @param keyPass
	 * @return
	 * @throws Exception
	 */
	public PrivateKey getPrivateKey(String alias, String keyPass) throws Exception {
		PrivateKey key = (PrivateKey) this.keyStore.getKey(alias, keyPass.toCharArray());
		return key;
	}

	/**
	 * 获取公钥
	 *
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	private PublicKey getPublicKey(String alias) throws Exception {
		PublicKey key = this.keyStore.getCertificate(alias).getPublicKey();
		return key;
	}


	/**
	 * 获取私钥字符串
	 *
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public String getPublicKeyStr(String alias) throws Exception {
		PublicKey key = getPublicKey(alias);
		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * 获取公钥字符串
	 *
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public String getStrPrivateKey(String alias, String keyPass) throws Exception {
		PrivateKey key = getPrivateKey(alias, keyPass);
		return Base64.encodeBase64String(key.getEncoded());
	}
}
