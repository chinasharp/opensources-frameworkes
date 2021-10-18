package org.opensourceframework.component.kafka;

import org.opensourceframework.base.helper.IpAddressHelper;
import org.opensourceframework.component.kafka.config.KafkaConfig;
import org.opensourceframework.component.kafka.constant.KafkaConstants;
import kafka.common.KafkaException;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MQ工厂
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 *
 */
public class KafkaFactory {
    private static final Logger logger = LoggerFactory.getLogger(KafkaFactory.class);
    private static final ConcurrentHashMap<String , KafkaProducer> producerMap = new ConcurrentHashMap<>(100);

    public static KafkaProducer createProducer(KafkaConfig kafkaConfig) throws KafkaException {
        return createOrGet(kafkaConfig);
    }

    private static KafkaProducer createOrGet(KafkaConfig kafkaConfig) throws KafkaException{
        KafkaProducer producer = null;
        String producerId = kafkaConfig.getProducerId();
        if(StringUtils.isBlank(producerId)){
            producerId = KafkaConstants.DEF_PRODUCER_ID;
        }
        if(StringUtils.isBlank(kafkaConfig.getBootstrapServers())){
            throw new org.apache.kafka.common.KafkaException("init kafka error.not found 'bootstrap.servers' config");
        }else{
            try {
                producer = producerMap.get(producerId);
                if (producer == null) {
                    Properties kafkaProperties = buildKafkaProperties(kafkaConfig);
                    producer = new org.apache.kafka.clients.producer.KafkaProducer<>(kafkaProperties);
                    producerMap.put(producerId, producer);
                }
            }catch (Exception e){
                throw new KafkaException("KafkaProducer create error!" , e);
            }
        }
        return producer;
    }

    public static KafkaConsumer createConsumer(KafkaConfig kafkaConfig , String consumerId) {
        KafkaConsumer<String , String> consumer = null;
        try {
            if(StringUtils.isBlank(kafkaConfig.getBootstrapServers())){
                throw new org.apache.kafka.common.KafkaException("init kafka error.not found 'bootstrap.servers' config");
            }
            if(StringUtils.isBlank(consumerId)){
                throw new org.apache.kafka.common.KafkaException("init kafka error.not found 'consumerId' config");
            }
            Properties properties = buildKafkaProperties(kafkaConfig);
            properties.put(ConsumerConfig.GROUP_ID_CONFIG , consumerId);
            properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, IpAddressHelper.resolveLocalIp().concat(":").concat(consumerId));
            consumer = new KafkaConsumer<String, String>(properties);
            logger.info("connect to {} success.", kafkaConfig.getBootstrapServers());
        } catch (org.apache.kafka.common.KafkaException e) {
            logger.info("Failed to connect to {}", kafkaConfig.getBootstrapServers(), e);
        }
        return consumer;
    }


    /**
     * 根据KafkaConfig 构建Kafka 需要的properties配置
     *
     * @param kafkaConfig
     * @return
     */
    private static Properties buildKafkaProperties(KafkaConfig kafkaConfig){
        Properties kafkaProperties = new Properties();
        /** Common Config **/
        kafkaProperties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG , kafkaConfig.getBootstrapServers());
        kafkaProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , kafkaConfig.getKeySerializer());
        kafkaProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG , kafkaConfig.getValueSerializer());

        /** Producer Config **/
        kafkaProperties.setProperty(ProducerConfig.ACKS_CONFIG , kafkaConfig.getAcks().toString());
        kafkaProperties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG , kafkaConfig.getBufferMemory().toString());
        kafkaProperties.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG , kafkaConfig.getMaxBlockMs().toString());
        kafkaProperties.setProperty(ProducerConfig.RETRIES_CONFIG , kafkaConfig.getRetries().toString());
        kafkaProperties.setProperty(ProducerConfig.RECEIVE_BUFFER_CONFIG , kafkaConfig.getReceiveBufferBytes().toString());
        kafkaProperties.setProperty(ProducerConfig.SEND_BUFFER_CONFIG , kafkaConfig.getSendBufferBytes().toString());
        kafkaProperties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG , kafkaConfig.getBatchSize().toString());
        kafkaProperties.setProperty(ProducerConfig.LINGER_MS_CONFIG , kafkaConfig.getLingerMs().toString());
        kafkaProperties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG , kafkaConfig.getCompressionType());
        kafkaProperties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION , kafkaConfig.getMaxInFlightRequestsPerConnection().toString());

        /** Consumer Config **/
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG , kafkaConfig.getKeyDeserializer());
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG , kafkaConfig.getValueDeserializer());
        // 如果value合法，则自动提交偏移量 enable.auto.commit
        kafkaProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConfig.getEnableAutoCommit());
        // 设置多久一次更新被消费消息的偏移量 auto.commit.interval.ms
        kafkaProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConfig.getAutoCommitIntervalMs());
        // 自动重置offset至上次提交的最新位置 auto.offset.reset
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfig.getAutoOffsetReset());
        // 设置会话响应的时间，超过这个时间kafka就可以选择放弃消费或者消费下一条消息 session.timeout.ms
        kafkaProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConfig.getSessionTimeoutMs());
        kafkaProperties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG , kafkaConfig.getFetchMinBytes());
        // Broker 的等待时间 fetch.max.wait.ms 500ms
        kafkaProperties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, kafkaConfig.getFetchMaxWaitMs());
        //kafkaProperties.put("request.timeout.ms", "30000");
        kafkaProperties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaConfig.getRequestTimeoutMs());
        // Consumer每次调用poll()时取到的records的最大数 max.poll.records 原始默认是500 组件设置的默认值为1000
        kafkaProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConfig.getMaxPollRecords());


        if(kafkaConfig.getRequestTimeoutMs() != null){
            kafkaProperties.setProperty(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG , kafkaConfig.getRequestTimeoutMs().toString());
        }
        kafkaProperties.setProperty(ProducerConfig.MAX_REQUEST_SIZE_CONFIG , kafkaConfig.getMaxRequestSize().toString());


        // 配置kerberos认证配置
        if (kafkaConfig.isKerberosEnabled()) {
            File krb5File = new File(kafkaConfig.getKrb5File());
            File jaasFile = new File(kafkaConfig.getJaasFile());
            if (krb5File.exists() && jaasFile.exists()) {
                // 配置kerberos认证，需要使用绝对路径
                System.setProperty("java.security.krb5.conf", krb5File.getAbsolutePath());
                System.setProperty("java.security.auth.login.config", jaasFile.getAbsolutePath());
                System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
                kafkaProperties.put("security.protocol", "SASL_PLAINTEXT");
                kafkaProperties.put("sasl.kerberos.service.name", "kafka");
            } else {
                String errorMsg = "ERROR # The kafka kerberos configuration file does not exist! please check it";
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
        }

        return kafkaProperties;
    }
}
