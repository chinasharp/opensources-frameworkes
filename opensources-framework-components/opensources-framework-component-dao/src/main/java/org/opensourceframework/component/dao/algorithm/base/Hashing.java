package org.opensourceframework.component.dao.algorithm.base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashing
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2019/2/25 5:30 PM
 */
public interface Hashing {
	Hashing MURMUR_HASH = new MurmurHash();
	ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<MessageDigest>();

	Hashing MD5 = new Hashing() {
		@Override
		public long hash(String key) {
			return hash(SafeEncoder.encode(key));
		}

		@Override
		public long hash(byte[] key) {
			try {
				if (md5Holder.get() == null) {
					md5Holder.set(MessageDigest.getInstance("MD5"));
				}
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("++++ no md5 algorythm found");
			}
			MessageDigest md5 = md5Holder.get();

			md5.reset();
			md5.update(key);
			byte[] bKey = md5.digest();
			long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8) | bKey[0] & 0xFF;
			return res;
		}
	};

	/**
	 *
	 * @param key
	 * @return
	 */
	long hash(String key);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	long hash(byte[] key);
}
