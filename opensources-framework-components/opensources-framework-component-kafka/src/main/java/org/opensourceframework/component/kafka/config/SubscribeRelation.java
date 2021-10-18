package org.opensourceframework.component.kafka.config;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 消息订阅关系vo
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class SubscribeRelation implements Serializable {
	/**
	 * 消费者id(consumerGroup)
	 */
	private String consumerId;

	/**
	 * 消费的top列表
	 */
	private List<String> topicList;


	private Pattern pattern;

	/**
	 * 消费消息bean id
	 */
	private String processorBean;

	public SubscribeRelation() {
		this.topicList = Lists.newArrayList();
	}

	public SubscribeRelation(String consumerId, String processorBean , List<String> topicList) {
		this.consumerId = consumerId;
		this.topicList = topicList;
		this.processorBean = processorBean;
	}

	public SubscribeRelation(String consumerId, Pattern pattern , String processorBean) {
		this.consumerId = consumerId;
		this.pattern = pattern;
		this.processorBean = processorBean;
	}

	public SubscribeRelation(String consumerId, List<String> topicList, Pattern pattern , String processorBean) {
		this.consumerId = consumerId;
		this.pattern = pattern;
		this.topicList = topicList;
		this.processorBean = processorBean;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getProcessorBean() {
		return processorBean;
	}

	public void setProcessorBean(String processorBean) {
		this.processorBean = processorBean;
	}

	public List<String> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<String> topicList) {
		this.topicList = topicList;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}

