package org.opensourceframework.component.mq.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 消息订阅关系vo
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SubscribeRelation implements Serializable {
	/**
	 * 消息类型  一般消息/有序消息/事务消息
	 * 参见MessageEnum
	 */
	private Integer messageType;

	/**
	 * 消费者id(consumerGroup)
	 */
	private String consumerId;

	/**
	 * 消费的top+tags列表
	 */
	private List<TopicTag> topicTags;

	/**
	 * 消费消息bean id
	 */
	private String processorBean;

	public SubscribeRelation() {
		topicTags = Lists.newArrayList();
	}

	public SubscribeRelation(String consumerId, List<TopicTag> topicTags) {
		this.messageType = MsgTypeConstant.CONCURRENTLY;
		this.consumerId = consumerId;
		this.topicTags = topicTags;
	}

	public SubscribeRelation(Integer messageType, String consumerId, List<TopicTag> topicTags) {
		this.messageType = messageType;
		this.consumerId = consumerId;
		this.topicTags = topicTags;
	}

	public SubscribeRelation(String consumerId, List<TopicTag> topicTags, String processorBean) {
		this.consumerId = consumerId;
		this.topicTags = topicTags;
		this.processorBean = processorBean;
	}

	public SubscribeRelation.TopicTag createTopicTag(String topic){
		SubscribeRelation.TopicTag topicTag = new TopicTag(topic);
		topicTag.setTagSet(Sets.newHashSet());
		return topicTag;
	}



	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public List<TopicTag> getTopicTags() {
		return topicTags;
	}

	public void setTopicTags(List<TopicTag> topicTags) {
		this.topicTags = topicTags;
	}

	public String getProcessorBean() {
		return processorBean;
	}

	public void setProcessorBean(String processorBean) {
		this.processorBean = processorBean;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	/**
	 * topic 中被consumer订阅的tag列表
	 *
	 */
	public class TopicTag implements Serializable {
		private String topic;
		private Set<String> tagSet;

		public TopicTag() {
		}

		public TopicTag(String topic) {
			this.topic = topic;
		}

		public TopicTag(String topic, Set<String> tagSet) {
			this.topic = topic;
			this.tagSet = tagSet;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

		public Set<String> getTagSet() {
			return tagSet;
		}

		public void setTagSet(Set<String> tagSet) {
			this.tagSet = tagSet;
		}
	}
}

