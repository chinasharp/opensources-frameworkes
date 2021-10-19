package org.opensourceframework.component.seclog.config;

import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.constant.AckTypeEnum;
import org.springframework.beans.factory.annotation.Value;

/**
 * 审计日志组件消息配置
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
public class SecLogMqConfig extends MqConfig {
    @Override
    @Value("${opensourceframework.mq.config.registryvo.mqType:1}")
    public void setMqType(String mqType) {
        super.setMqType(mqType);
    }

    @Override
    @Value("${opensourceframework.mq.config.registryvo.consumeThreadNums:20}")
    public void setConsumeThreadNums(Integer consumeThreadNums) {
        super.setConsumeThreadNums(consumeThreadNums);
    }

    @Override
    @Value("${opensourceframework.mq.config.registryvo.produceThreadNums:10}")
    public void setProduceThreadNums(Integer produceThreadNums) {
        super.setProduceThreadNums(produceThreadNums);
    }

    @Override
    public void setSyncFlag(Integer syncFlag) {
        super.setSyncFlag(syncFlag);
    }

    @Override
    @Value("${opensourceframework.mq.config.registryvo.nameSrvAddr}")
    public void setNameSrvAddr(String nameSrvAddr) {
        super.setNameSrvAddr(nameSrvAddr);
    }

    @Override
    @Value("${opensourceframework.mq.config.registryvo.accessKey:}")
    public void setAccessKey(String accessKey) {
        super.setAccessKey(accessKey);
    }

    @Override
    @Value("${opensourceframework.mq.config.registryvo.secretKey:}")
    public void setSecretKey(String secretKey) {
        super.setSecretKey(secretKey);
    }

    @Override
    @Value("${opensourceframework.mq.config.registryvo.producerId:PID_SEC_LOG_APPLICATION}")
    public void setProducerId(String producerId) {
        super.setProducerId(producerId);
    }

    @Value("${spring.profiles.active:}")
    public String profileActive;

    @Override
    public void setConsumerId(String consumerId) {
        super.setConsumerId(consumerId);
    }

    @Override
    public void setConsumeThreadNums(int consumeThreadNums) {
        super.setConsumeThreadNums(consumeThreadNums);
    }

    @Override
    public void setProduceThreadNums(int produceThreadNums) {
        super.setProduceThreadNums(produceThreadNums);
    }

    @Override
    public void setAck(AckTypeEnum ack) {
        super.setAck(ack);
    }

    @Override
    public void setEnableMsgTrace(Boolean enableMsgTrace) {
        super.setEnableMsgTrace(enableMsgTrace);
    }

    @Override
    public void setSendTimeOut(Long sendTimeOut) {
        super.setSendTimeOut(sendTimeOut);
    }
}
