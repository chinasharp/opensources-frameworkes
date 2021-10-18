package org.opensourceframework.base.threads.pattens;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface ConsumerWorker<T> {
    void doWork(T doWork);
}
