package org.opensourceframework.base.microservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SpringCloudMicroServiceContext extends AbstractMicroServiceContext {
    private final Map<String, String> mapContext = new ConcurrentHashMap();

    public SpringCloudMicroServiceContext() {
    }

    private String convertKey(String key) {
        return createContextKey(key.toLowerCase());
    }

    @Override
    public void setAttachment(String key, String attachment) {
        if (attachment != null) {
            this.mapContext.put(this.convertKey(key), attachment);
        } else {
            this.removeAttachment(key);
        }

    }

    @Override
    public void removeAttachment(String key) {
        this.mapContext.remove(this.convertKey(key));
    }

    @Override
    public String getAttachment(String key) {
        return this.mapContext.get(this.convertKey(key));
    }

    @Override
    public void removeContext() {
        this.mapContext.clear();
    }

    @Override
    public Map<String, String> getAttachments() {
        return this.mapContext;
    }
}
