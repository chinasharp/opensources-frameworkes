package org.opensourceframework.base.threads.pattens;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface ProductStore<T> {
    void push(T t) throws InterruptedException;

    T pop() throws InterruptedException;

    List<T> pop(int n) throws InterruptedException;

    boolean isFull();
}
