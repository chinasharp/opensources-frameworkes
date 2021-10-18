package org.opensourceframework.component.redis.cache.queue;

import redis.clients.jedis.JedisPubSub;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class ListenerConver {
	public static JedisPubSub conver(IRedisSubProcessor<String> processor){
		JedisPubSub pubSub = new JedisPubSub(){
			@Override
			public void onMessage(String channel, String message) {
				processor.process(channel, message);
			}
		};
		return pubSub;
	}
}
