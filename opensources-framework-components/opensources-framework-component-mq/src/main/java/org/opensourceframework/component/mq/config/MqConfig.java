package org.opensourceframework.component.mq.config;

import org.opensourceframework.component.mq.constant.AckTypeEnum;
import org.opensourceframework.component.mq.constant.MQConstant;
import org.opensourceframework.component.mq.constant.MQTypeEnum;

/**
 * MQ配置类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MqConfig {
    protected String namespace;

    /**
     * MQ类型  默认 MQTypeEnum.ROCKET
     */
    protected String mqType = MQTypeEnum.ROCKET.getType();

    /**
     * 服务器地址 例如:192.168.100.1:9876;192.168.100.2:9876
     */
    protected String nameSrvAddr;

    protected String accessKey;

    protected String secretKey;

    protected String producerId;

    protected String consumerId;

    /**
     * 发送超时时间 毫秒
     */
    protected Long sendTimeOut;

    /**
     * 消费者消费MQ的线程数
     */
    protected Integer consumeThreadNums = 20;
    protected Integer produceThreadNums = 10;

    /**
     * 消费失败后，最多重新消费多少次才投递到死信队列
     */
    protected Integer maxReconsumeTimes;

    /**
     * 启用消息跟踪
     */
    protected Boolean enableMsgTrace;

    protected Integer syncFlag;

    protected AckTypeEnum ack;

    public MqConfig() {
    }

    public MqConfig(String nameSrvAddr, String consumerId) {
       this(nameSrvAddr , MQConstant.DEF_PRODUCER_ID , consumerId);
    }

    public MqConfig(String nameSrvAddr, String producerId, String consumerId) {
        this(nameSrvAddr , producerId , consumerId ,20 , 10);
    }

    public MqConfig(String nameSrvAddr, String producerId, String consumerId , Integer consumeThreadNums) {
        this(nameSrvAddr , producerId , consumerId , consumeThreadNums , 10);
    }

    public MqConfig(String nameSrvAddr, String producerId, String consumerId , Integer consumeThreadNums, Integer produceThreadNums) {
       this(nameSrvAddr , producerId ,consumerId ,null ,null, false ,consumeThreadNums ,produceThreadNums );
    }

    public MqConfig(String nameSrvAddr, String consumerId , String accessKey, String secretKey, Integer consumeThreadNums){
       this(nameSrvAddr , MQConstant.DEF_PRODUCER_ID , consumerId , accessKey ,secretKey ,false , consumeThreadNums , 10 );
    }


    public MqConfig(String nameSrvAddr, String producerId , String consumerId  , String accessKey, String secretKey , Boolean enableMsgTrace ,
                    Integer consumeThreadNums , Integer produceThreadNums ) {
        this.mqType = MQTypeEnum.ROCKET.getType();
        this.ack = AckTypeEnum.ALL_ACK;
        this.nameSrvAddr = nameSrvAddr;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.consumerId = consumerId;
        this.producerId = producerId;
        this.consumeThreadNums = consumeThreadNums;
        this.produceThreadNums = produceThreadNums;
        this.enableMsgTrace = enableMsgTrace;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getMqType() {
        return mqType;
    }

    public void setMqType(String mqType) {
        this.mqType = mqType;
    }

    public void setConsumeThreadNums(Integer consumeThreadNums) {
        this.consumeThreadNums = consumeThreadNums;
    }

    public void setProduceThreadNums(Integer produceThreadNums) {
        this.produceThreadNums = produceThreadNums;
    }

    public void setSyncFlag(Integer syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getNameSrvAddr() {
        return nameSrvAddr;
    }

    public void setNameSrvAddr(String nameSrvAddr) {
        this.nameSrvAddr = nameSrvAddr;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public int getConsumeThreadNums() {
        return consumeThreadNums;
    }

    public void setConsumeThreadNums(int consumeThreadNums) {
        this.consumeThreadNums = consumeThreadNums;
    }

    public int getProduceThreadNums() {
        return produceThreadNums;
    }

    public void setProduceThreadNums(int produceThreadNums) {
        this.produceThreadNums = produceThreadNums;
    }

    public AckTypeEnum getAck() {
        return ack;
    }

    public void setAck(AckTypeEnum ack) {
        this.ack = ack;
    }

    public Boolean getEnableMsgTrace() {
        return enableMsgTrace;
    }

    public void setEnableMsgTrace(Boolean enableMsgTrace) {
        this.enableMsgTrace = enableMsgTrace;
    }

    public Integer getSyncFlag() {
        return syncFlag;
    }

    public Long getSendTimeOut() {
        return sendTimeOut;
    }

    public void setSendTimeOut(Long sendTimeOut) {
        this.sendTimeOut = sendTimeOut;
    }

    public Integer getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    public void setMaxReconsumeTimes(Integer maxReconsumeTimes) {
        this.maxReconsumeTimes = maxReconsumeTimes;
    }
}
