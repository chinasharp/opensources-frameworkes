package org.opensourceframework.base.threads.pattens;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ProductQueueStore<T> implements ProductStore<T> {
    private final BlockingQueue<T> msgQueue;

    public ProductQueueStore(int size) {
        this.msgQueue = new ArrayBlockingQueue(size);
    }

    public ProductQueueStore() {
        this(10000);
    }

    @Override
    public void push(T msg) throws InterruptedException {
        if (!this.msgQueue.contains(msg)) {
            this.msgQueue.put(msg);
        }

    }

    @Override
    public T pop() throws InterruptedException {
        return this.msgQueue.take();
    }

    @Override
    public List<T> pop(int n) throws InterruptedException {
        T prod = this.msgQueue.take();
        List<T> msgs = new ArrayList();
        msgs.add(prod);

        for(int i = 1; i < n; ++i) {
            prod = this.msgQueue.poll();
            if (null == prod) {
                break;
            }

            msgs.add(prod);
        }

        return msgs;
    }

    @Override
    public boolean isFull() {
        return this.msgQueue.remainingCapacity() > 0;
    }
}
