package org.opensourceframework.base.microservice;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public abstract class  AbstractMicroServiceContext implements MicroServiceContext {
    public AbstractMicroServiceContext() {
    }

    public static String createContextKey(String key) {
        return key != null && key.startsWith("x-opensources-context-") ? key : "x-opensources-context-" + key;
    }
}
