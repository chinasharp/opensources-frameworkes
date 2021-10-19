package org.opensourceframework.component.dao.algorithm.base;

import java.nio.charset.StandardCharsets;

/**
 * UTF-8 解码与编码
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2019/2/25 5:30 PM
 * @see   redis.clients.jedis.util.SafeEncoder;
 *
 */
public class SafeEncoder {
    private SafeEncoder() {
        throw new InstantiationError("Must not instantiate this class");
    }

    public static byte[][] encodeMany(String... strs) {
        byte[][] many = new byte[strs.length][];

        for(int i = 0; i < strs.length; ++i) {
            many[i] = encode(strs[i]);
        }

        return many;
    }

    public static byte[] encode(String str) {
        if (str == null) {
            throw new RuntimeException("value sent cannot be null");
        } else {
            return str.getBytes(StandardCharsets.UTF_8);
        }
    }

    public static String encode(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
