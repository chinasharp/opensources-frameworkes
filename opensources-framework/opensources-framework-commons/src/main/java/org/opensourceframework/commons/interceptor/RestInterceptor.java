package org.opensourceframework.commons.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求拦截接口
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface RestInterceptor extends HandlerInterceptor {
    /**
     * 需要拦截的path
     * @return
     */
    String[] includePath();

    /**
     * 不需要拦截的path
     * @return
     */
    String[] excludePath();

    /**
     * 添加拦截的path
     * @param paths
     */
    void addIncludePaths(String[] paths);

    /**
     * 添加不拦截的path
     *
     * @param paths
     */
    void addExcludePaths(String[] paths);
}
