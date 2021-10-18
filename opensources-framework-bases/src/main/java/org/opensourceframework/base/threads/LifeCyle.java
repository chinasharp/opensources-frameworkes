package org.opensourceframework.base.threads;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface LifeCyle {
    void start();

    void stop();

    void pause();

    LifeCyleStatus getStatus();
}
