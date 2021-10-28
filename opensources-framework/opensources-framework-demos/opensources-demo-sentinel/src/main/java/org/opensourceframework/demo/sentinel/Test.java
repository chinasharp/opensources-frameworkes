package org.opensourceframework.demo.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import org.opensourceframework.demo.sentinel.vo.UserVo;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.helper.ProducerHelper;
import org.opensourceframework.component.mq.vo.MqMessageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/3/2 上午8:54
 */
public class Test {
	private static final MqConfig mqConfig;

	static {
		mqConfig = new MqConfig();
		mqConfig.setNameSrvAddr("10.53.156.243:9876");
	}
	public static void main(String[] args) {
		initFlowRules();

		// 1. 定义资源
		while (true){
			Entry entry = null;
			try {
				// 2.1 定义资源名称
				entry = SphU.entry("sendMessage");

				// 2.2 业务逻辑
				// eg: 访问数据库 调用接口等等
				sendMessage();

				Thread.sleep(20);
			}catch (Exception e){
				System.out.println("被流控限制");
			}finally {
				if(entry != null) {
					entry.exit();
				}
			}
		}
	}

	private static void sendMessage(){
		UserVo vo = new UserVo();
		vo.setId(1L);
		vo.setName("test");
		vo.setAge(18);
		vo.setAddress("广州市天河区");
		MqMessageVo messageVo = new MqMessageVo();
		messageVo.setMsgContent(JSON.toJSONString(vo));
		ProducerHelper.sendConcurrentlyMsg(mqConfig , "DEMO_TOPIC" , "DEMO_TAG", messageVo);
	}

	private static void initFlowRules(){
		List<FlowRule> flowRules = new ArrayList<>();

		FlowRule flowRule = new FlowRule();
		// 与Entry中定义的资源名称对应
		flowRule.setResource("sendMessage");
		flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		flowRule.setCount(10);

		flowRules.add(flowRule);

		FlowRuleManager.loadRules(flowRules);
	}
}
