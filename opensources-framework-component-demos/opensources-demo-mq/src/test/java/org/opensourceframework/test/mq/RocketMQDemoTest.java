package org.opensourceframework.test.mq;

import com.google.common.collect.Lists;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.mq.RocketMQDemoBoot;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.constant.MsgTypeConstant;
import org.opensourceframework.component.mq.helper.ConsumerHelper;
import org.opensourceframework.component.mq.helper.ProducerHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 使用明编码方式进行RocketMQ的发送和订阅
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RocketMQDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class RocketMQDemoTest {
    private static String CONSUMER_ID = "GID_TEST_DEMO";
    private static String TOPIC = "MQ_TEST_TOPIC";
    private static String PRODUCER_ID = "PID_MQ_TEST";
    private static String TAG = "MQ_TEST_TAG";
    private static String MESSAGE_PROCESSOR_BEAN = "demoMessageProcessor";

    private static MqConfig mqConfig;
    static {
        mqConfig = new MqConfig();
        mqConfig.setNameSrvAddr("10.53.156.243:9876");
        mqConfig.setConsumerId(CONSUMER_ID);
        mqConfig.setProducerId(PRODUCER_ID);
    }

    @Test
    public void sendMessage() throws Exception{
        // 配置RocketMQ支持logback
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        System.setProperty("rocketmq.client.logLevel" , "INFO");
        for(int i = 0 ; i < 1000 ; i++) {
            MessageVo messageVo = new MessageVo();
            messageVo.setBizId(SnowFlakeId.nextId(null, null).toString());
            messageVo.setMsgContent(i + "");
            messageVo.setBizCode((i % 16) + "");

            try {
                //发送无序消息
                //ProducerHelper.sendMessage(mqConfig , TOPIC ,TAG , messageVo ,0 , 30000L, MsgTypeConstant.CONCURRENTLY);

                //发送有序消息
                ProducerHelper.sendMessage(mqConfig, TOPIC ,TAG , messageVo ,0 , 30000L, MsgTypeConstant.ORDERLY , null , null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while(true){
            Thread.sleep(1000000L);
        }
    }

    @Test
    public void subscribeMessage() throws InterruptedException {
        //ConsumerHelper.subscribe(mqConfig , CONSUMER_ID , TOPIC , Lists.newArrayList(TAG) , MESSAGE_PROCESSOR_BEAN , MsgTypeConstant.ORDERLY);
        ConsumerHelper.subscribe(mqConfig, CONSUMER_ID , TOPIC , Lists.newArrayList(TAG) , MESSAGE_PROCESSOR_BEAN , MsgTypeConstant.ORDERLY);
        while(true){
            Thread.sleep(10000L);
        }
    }
}
