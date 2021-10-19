package org.opensourceframework.starter.mq.config;

/**
 * 消息订阅配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SubscribeProperties {
	private String topic;
	private String tag;
	private String consumer;

	public SubscribeProperties() {
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}
}
