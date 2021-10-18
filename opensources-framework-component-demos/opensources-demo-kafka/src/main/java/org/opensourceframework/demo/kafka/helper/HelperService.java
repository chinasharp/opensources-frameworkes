package org.opensourceframework.demo.kafka.helper;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.kafka.contant.KafkaContants;
import org.opensourceframework.component.kafka.api.IKafkaMessageProcessor;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.helper.KafkaConsumerHelper;
import org.opensourceframework.component.kafka.helper.KafkaProducerHelper;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * KafkaProducerHelper、KafkaConsumerHelper使用例子
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
@Component
public class HelperService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(HelperService.class);
    private static java.util.concurrent.atomic.AtomicInteger sendCount = new AtomicInteger(0);
    @Autowired
    private KafkaConfig kafkaConfig;
    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while(true) {
                    MessageVo messageVo = new MessageVo();
                    messageVo.setMsgContent("Use KafkaProducerHelper send message. num:" + sendCount.addAndGet(1));
                    KafkaProducerHelper.send(kafkaConfig, KafkaContants.HELPER_TOPIC, null, null, messageVo);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }).start();

        new Thread(new Runnable() {
            /**
             * @see Thread#run()
             */
            @Override
            public void run() {
                subscribe();
            }
        }).start();


    }

    public void subscribe(){
        List<String> topicList = Lists.newArrayList(KafkaContants.HELPER_TOPIC);
        KafkaConsumerHelper.subscribe(kafkaConfig , KafkaContants.HELPER_CONSUMER ,topicList , new IKafkaMessageProcessor(){

            /**
             * 消息业务处理方法
             *
             * @param messageVo
             */
            @Override
            public void process(List<MessageVo> messageVo) {
                logger.info("\n Use  KafkaConsumerHelper Subscribe Msg. Data:{}", JSON.toJSONString(messageVo));
            }

            /**
             * 异常处理方法
             *
             * @param messageVo
             */
            @Override
            public void exceptionhandle(List<MessageVo> messageVo) {

            }
        } );
    }
}
