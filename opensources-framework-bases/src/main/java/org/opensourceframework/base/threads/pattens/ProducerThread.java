package org.opensourceframework.base.threads.pattens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ProducerThread<T> implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ProducerThread.class);
    private ProductStore<T> prodStore;
    private boolean running;
    private ProducerWorker worker;

    public ProducerThread(ProductStore<T> prodStore, ProducerWorker worker) {
        this.running = true;
        this.prodStore = prodStore;
        this.worker = worker;
    }

    public ProducerThread() {
        this.running = false;
    }

    @Override
    public void run() {
        LOG.info("ProducerThread Started....");

        while(this.running && !Thread.currentThread().isInterrupted()) {
            try {
                List<T> prods = this.worker.doWork();
                Iterator iterator = prods.iterator();

                while(iterator.hasNext()) {
                    T p = (T)iterator.next();
                    this.prodStore.push(p);
                    LOG.debug("Producer-->{}", p);
                }
            } catch (InterruptedException e) {
                LOG.warn("Producer线程被中断");
                break;
            }
        }

        LOG.info("ProducerThread Stopped....");
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
}
