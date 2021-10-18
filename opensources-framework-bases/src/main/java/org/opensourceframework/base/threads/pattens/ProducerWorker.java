package org.opensourceframework.base.threads.pattens;

import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface ProducerWorker<T> {
    List<T> doWork();
}
