package org.opensourceframework.base.threads.pattens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ConsumerThread<T> implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerThread.class);
    private ProductStore<T> prodStore;
    private boolean running;
    private ConsumerWorker worker;

    public ConsumerThread(ProductStore<T> prodStore, ConsumerWorker worker) {
        this.running = true;
        this.prodStore = prodStore;
        this.worker = worker;
    }

    public ConsumerThread() {
        this.running = false;
    }

    @Override
    public void run() {
        LOG.info("ConsumerThread Started....");

        while(this.running && !Thread.currentThread().isInterrupted()) {
            try {
                T prod = this.prodStore.pop();
                this.worker.doWork(prod);
                LOG.debug("polled product:{}", prod.toString());
            } catch (InterruptedException e) {
                LOG.warn("ConsumerThread线程被中断");
                break;
            }
        }
        LOG.info("ConsumerThread Stopped....");
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ProductStore<T> getProdStore() {
        return this.prodStore;
    }

    public void setProdStore(ProductStore<T> prodStore) {
        this.prodStore = prodStore;
    }

    public ConsumerWorker getWorker() {
        return this.worker;
    }

    public void setWorker(ConsumerWorker worker) {
        this.worker = worker;
    }
}

