package org.opensourceframework.component.redis.cache.queue;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface IRedisSubProcessor<T> {
	void process(String paramString, T paramT);
}
