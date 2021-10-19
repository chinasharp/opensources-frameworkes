package org.opensourceframework.base.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class CommonThreadPool implements LifeCyle, ThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonThreadPool.class);
    private FixedThreadPoolExecutor threadPool;
    private AtomicInteger count = new AtomicInteger(0);
    /**
     * 线程池中所保存的线程数，包括空闲线程。
     */
    private int poolCoreSize = 10;

    /**
     * 任务队列最大容量
     */
    private int poolQueueSize = 500;

    /**
     * 线程池名称
     */
    private String threadPoolName;

    private LifeCyleStatus status;

    public CommonThreadPool() {
        this.status = LifeCyleStatus.NEW;
    }

    public CommonThreadPool(Integer poolCoreSize) {
        this.poolCoreSize = poolCoreSize;
        this.threadPoolName = "CommonThreadPool_".concat(String.valueOf(count.getAndIncrement()));
        this.status = LifeCyleStatus.NEW;
    }

    public CommonThreadPool(Integer poolCoreSize , Integer poolQueueSize) {
        this.poolCoreSize = poolCoreSize;
        this.poolQueueSize = poolQueueSize;
        this.threadPoolName = "CommonThreadPool_".concat(String.valueOf(count.getAndIncrement()));
        this.status = LifeCyleStatus.NEW;
    }


    public CommonThreadPool(Integer poolCoreSize , Integer poolQueueSize , String threadPoolName) {
        this.poolCoreSize = poolCoreSize;
        this.poolQueueSize = poolQueueSize;
        this.threadPoolName = threadPoolName;
        this.status = LifeCyleStatus.NEW;
    }

    @Override
    public synchronized void execute(Runnable worker) {
        if (this.status != LifeCyleStatus.RUNNING) {
            this.start();
        }
        this.threadPool.execute(worker);
    }

    @Override
    public synchronized void start() {
        if (this.status != LifeCyleStatus.RUNNING) {
            LOGGER.info("Start CommonThreadPool......");
            this.status = LifeCyleStatus.RUNNING;
            LOGGER.info("Create CommonThreadPool......");
            this.threadPool = new FixedThreadPoolExecutor(this.poolCoreSize, this.poolQueueSize,this.threadPoolName);
        }
    }

    @Override
    public synchronized void stop() {
        if (this.status == LifeCyleStatus.RUNNING) {
            LOGGER.info("Stopping CommonThreadPool......");
            shutdownAndAwaitTermination(this.threadPool);
            this.threadPool = null;
            this.status = LifeCyleStatus.STOPPED;
            LOGGER.info("Stop CommonThreadPool end! ");
        }
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LifeCyleStatus getStatus() {
        return this.status;
    }

    public void setPoolCoreSize(int poolCoreSize) {
        this.poolCoreSize = poolCoreSize;
    }

    public static void shutdownAndAwaitTermination(FixedThreadPoolExecutor threadPool) {
        threadPool.shutdown();

        try {
            if (!threadPool.awaitTermination(4L, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                threadPool.awaitTermination(4L, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }
}
