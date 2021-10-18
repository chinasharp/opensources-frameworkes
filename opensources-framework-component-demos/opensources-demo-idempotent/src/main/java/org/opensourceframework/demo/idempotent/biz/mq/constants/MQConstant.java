package org.opensourceframework.demo.idempotent.biz.mq.constants;

/**
 * MQ常量 可使用配置文件或者nacos配置替换
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MQConstant {
	/**
	 * MQ DemoUser消息主题
	 */
	public static final String DEMO_USER_TOPIC = "DEMO_USER_TOPIC";

	/**
	 * MQ DemoUser消息TAG
	 */
	public static final String DEMO_USER_TAG = "DEMO_USER_TAG";

	/**
	 * MQ DemoUser消息消费者
	 */
	public static final String DEMO_USER_CONSUMER = "GID_DEMO_USER_CONSUMER";
}
