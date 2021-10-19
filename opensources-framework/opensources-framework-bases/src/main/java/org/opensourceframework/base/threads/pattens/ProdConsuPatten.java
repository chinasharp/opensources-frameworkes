package org.opensourceframework.base.threads.pattens;

import org.opensourceframework.base.threads.CommonThreadPool;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ProdConsuPatten<T> {
    ConsumerThread<T> consumer;
    ProducerThread<T> producer;
    ProductStore<T> store;
    CommonThreadPool pool;
    boolean started = false;

    public ProdConsuPatten(CommonThreadPool pool, ProductStore<T> store, ConsumerWorker<T> consu, ProducerWorker prod) {
        this.producer = new ProducerThread(store, prod);
        this.consumer = new ConsumerThread(store, consu);
        this.store = store;
        this.pool = pool;
    }

    public synchronized void start() {
        if (!this.started) {
            this.pool.execute(this.consumer);
            this.started = true;
        }

    }

    public synchronized void stop() {
        if (this.started) {
            this.producer.setRunning(false);
            this.consumer.setRunning(false);
            this.started = false;
        }

    }

    public void put(T prod) throws InterruptedException {
        this.store.push(prod);
    }
}
