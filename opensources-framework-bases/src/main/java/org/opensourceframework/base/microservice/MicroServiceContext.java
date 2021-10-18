package org.opensourceframework.base.microservice;

import java.util.Map;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface MicroServiceContext {
    String CONTEXT_PREFIX = "x-opensources-context-";

    /**
     * 设置附加属性
     *
     * @param key
     * @param attachment
     */
    void setAttachment(String key, String attachment);

    /**
     * 获取附加属性
     *
     * @param key
     * @return
     */
    String getAttachment(String key);

    /**
     * 移出附加属性
     *
     * @param key
     */
    void removeAttachment(String key);

    /**
     * 获取所有附加属性
     *
     * @return
     */
    Map<String, String> getAttachments();

    /**
     * 清除所有
     */
    void removeContext();
}
