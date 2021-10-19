package org.opensourceframework.component.mq;

import org.opensourceframework.component.mq.exception.MQException;
import org.opensourceframework.component.mq.config.MqConfig;
import org.opensourceframework.component.mq.constant.MQConstant;
import org.opensourceframework.component.mq.helper.LocalIpAddress;
import org.opensourceframework.component.mq.helper.ProducerHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.ServiceState;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.remoting.RPCHook;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MQ工厂
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 *
 */
public class RocketFactory {
    private static final ConcurrentHashMap<String , DefaultMQProducer> producerMap = new ConcurrentHashMap<>(100);

    //设置为您在阿里云消息队列RocketMQ版控制台上创建的GID,以及替换为您的AccessKey ID和AccessKey Secret。
    private static RPCHook getAclRPCHook(MqConfig configVo) {
        RPCHook rpcHook = null;
        String accessKey = configVo.getAccessKey();
        String secretKey = configVo.getSecretKey();
        if(StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretKey)) {
            rpcHook = new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
        }
        return rpcHook;
    }

    public static DefaultMQProducer buildDefaultProducer(MqConfig configVo) throws MQException {
        RPCHook rpcHook = getAclRPCHook(configVo);
        return createAndStart(configVo , rpcHook, null);
    }

    public static DefaultMQProducer buildProducer(MqConfig configVo , TransactionListener transactionListener) throws MQException{
        return createAndStart(configVo ,getAclRPCHook(configVo) , transactionListener);
    }

    private static DefaultMQProducer createAndStart(MqConfig configVo , RPCHook rpcHook , TransactionListener transactionListener) throws MQException{
        DefaultMQProducer producer = null;
        String producerId = configVo.getProducerId();
        if(StringUtils.isBlank(producerId)){
            if(transactionListener == null) {
                producerId = MQConstant.DEF_PRODUCER_ID;
            }else{
                producerId = ProducerHelper.buildTransactionProducerId(null , transactionListener.getClass().getSimpleName());
            }
        }
        String nameSrvAddr = configVo.getNameSrvAddr();
        Long sendTimeOut = configVo.getSendTimeOut();
        if(sendTimeOut == null){
            sendTimeOut =  MQConstant.DEF_SEND_TIME_OUT;
        }

        if(StringUtils.isBlank(nameSrvAddr)){
            throw new MQException("nameSrvAddr property is null");
        }else{
            try {
                producer = producerMap.get(producerId);
                if (producer == null) {
                    if(transactionListener == null) {
                        producer = new DefaultMQProducer(null, producerId , rpcHook);
                    }else{
                        producer = new TransactionMQProducer(producerId , rpcHook);
                        ((TransactionMQProducer)producer).setTransactionListener(transactionListener);
                    }
                    producerMap.put(producerId, producer);
                }

                if(ServiceState.CREATE_JUST == producer.getDefaultMQProducerImpl().getServiceState()){
                    producer.setSendMsgTimeout(sendTimeOut.intValue());
                    producer.setNamesrvAddr(nameSrvAddr);
                    producer.start();
                }

            }catch (Exception e){
                throw new MQException("MQ create error!" , e);
            }
        }
        return producer;
    }

    public static DefaultMQPushConsumer createConsumer(MqConfig configVo) {
        DefaultMQPushConsumer consumer = null;
        String namespace = getNameSpace(configVo);
        String consumerGroup = configVo.getConsumerId();
        String nameSrvAddr = configVo.getNameSrvAddr();
        if (StringUtils.isBlank(consumerGroup)) {
            throw new MQException("ConsumerId property is null");
        }else if(StringUtils.isBlank(nameSrvAddr)) {
            throw new MQException("nameSrvAddr property is null");
        }else{
            RPCHook rpcHook = buildAclRPCHook(configVo);
            consumer = new DefaultMQPushConsumer(null , consumerGroup, rpcHook , new AllocateMessageQueueAveragely());
            //CONSUME_FROM_LAST_OFFSET 队列尾消费
            //CONSUME_FROM_FIRST_OFFSET 队列头消费
            //CONSUME_FROM_TIMESTAMP 按照日期选择某个位置消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.setInstanceName(consumerGroup.concat("_").concat(getIp()));
            consumer.setNamesrvAddr(nameSrvAddr);
            consumer.setConsumeThreadMin(configVo.getConsumeThreadNums());
            consumer.setConsumeThreadMax(configVo.getConsumeThreadNums());

            Integer maxReconsumerTimes = configVo.getMaxReconsumeTimes();
            if(maxReconsumerTimes == null){
                maxReconsumerTimes = MQConstant.DEF_MAX_RECONSUME_TIMES;
            }
            consumer.setMaxReconsumeTimes(maxReconsumerTimes);
        }
        return consumer;
    }

    public static String getIp(){
        List<String> ips = LocalIpAddress.resolveLocalIps();
        String ip = null;
        if (CollectionUtils.isEmpty(ips)) {
            ip = "127.0.0.1";
        }else{
            ip = ips.get(ips.size() - 1);
        }
        return ip;
    }

    private static String getNameSpace(MqConfig configVo){
        String namespace = configVo.getNamespace();
        if(StringUtils.isBlank(namespace)){
            namespace = MQConstant.DEF_NAME_SPACE;
        }
        return namespace;
    }

    private static RPCHook buildAclRPCHook(MqConfig configVo){
        RPCHook rpcHook = null;

        String accessKey = configVo.getAccessKey();
        String secretKey = configVo.getSecretKey();
        if(StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretKey)){
            rpcHook = new AclClientRPCHook(new SessionCredentials(accessKey,secretKey));
        }

        return rpcHook;
    }
}
