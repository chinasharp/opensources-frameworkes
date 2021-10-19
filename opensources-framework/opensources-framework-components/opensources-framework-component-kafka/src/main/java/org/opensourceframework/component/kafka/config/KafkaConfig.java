package org.opensourceframework.component.kafka.config;

import java.io.Serializable;

/**
 * Kafka 配置类
 *
 * @author rewerma 2020-01-27
 * @since 1.0.0
 */
public class KafkaConfig implements Serializable {
    private boolean kerberosEnabled = false;
    private String krb5File;
    private String jaasFile;

    /**
     * 指定broker的地址清单，地址的格式为host1:port1,host2:port2
     */
    private String bootstrapServers;

    /**
     * Key 序列化器
     */
    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * Value 序列化器
     */
    private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * 不同生产者唯一标识
     */
    private String producerId;

    /**
     * 指定了必须要有多少个分区副本收到消息，生产者才会认为写入消息是成功的，这个参数对消息丢失的可能性有重大影响
     *
     * acks=0:生产者在写入消息之前不会等待任何来自服务器的响应，容易丢消息，但是吞吐量高。
     * acks=1:只要集群的首领节点收到消息，生产者会收到来自服务器的成功响应。如果消息无法到达首领节点(比如首领节点崩溃，新首领没有选举出来)
     *        生产者会收到一个错误响应，为了避免数据丢失，生产者会重发消息。不过，如果一个没有收到消息的节点成为新首领，消息还是会丢失。
     *        默认使用这个配置。
     * acks=all：只有当所有参与复制的节点都收到消息，生产者才会收到一个来自服务器的成功响应。延迟高。
     */
    private Integer acks = 1;

    /**
     * 设置生产者内存缓冲区的大小，生产者用它缓冲要发送到服务器的消息。如果数据产生速度大于向broker发送的速度，导致生产者空间不足，
     * producer会阻塞或者抛出异常。源码缺省33554432 (32M),这里设置默认值为100M
     */
    private Long bufferMemory = 104857600L;


    /**
     * 指定了在调用send()方法或者使用partitionsFor()方法获取元数据时生产者的阻塞时间。当生产者的发送缓冲区已满，或者没有可用的元数据时，
     * 这些方法就会阻塞。在阻塞时间达到max.block.ms时，生产者会抛出超时异常。缺省60000ms
     */
    private Long maxBlockMs = 60000L;

    /**
     * 发送失败时，指定生产者可以重发消息的次数。默认情况下，生产者在每次重试之间等待100ms，
     * 可以通过参数retry.backoff.ms参数来改变这个时间间隔。缺省0
     */
    private Integer retries = 0;

    /**
     * 指定TCP socket接受和发送数据包的缓存区大小。如果它们被设置为-1，则使用操作系统的默认值。如果生产者或消费者处在不同的数据中心，
     * 那么可以适当增大这些值，因为跨数据中心的网络一般都有比较高的延迟和比较低的带宽。缺省102400
     */
    private Long receiveBufferBytes = 102400L;
    private Long sendBufferBytes = 102400L;

    /**
     * 当多个消息被发送同一个分区时，生产者会把它们放在同一个批次里。该参数指定了一个批次可以使用的内存大小，按照字节数计算。
     * 当批次内存被填满后，批次里的所有消息会被发送出去。但是生产者不一定都会等到批次被填满才发送，半满甚至只包含一个消息的
     * 批次也有可能被发送。缺省16384(16k)
     */
    private Integer batchSize = 16384;

    /**
     * 指定了生产者在发送批次前等待更多消息加入批次的时间。它和batch.size以先到者为先。也就是说，一旦我们获得消息的数量够batch.size的数量了，
     * 他将会立即发送而不顾这项设置，然而如果我们获得消息字节数比batch.size设置要小的多，我们需要“linger”特定的时间以获取更多的消息。
     * 这个设置默认为0，即没有延迟。设定linger.ms=5，例如，将会减少请求数目，但是同时会增加5ms的延迟，但也会提升消息的吞吐量。
     */
    private Long lingerMs = 0L;

    /**
     * producer用于压缩数据的压缩类型。默认是无压缩。正确的选项值是none、gzip、snappy。压缩最好用于批量处理，批量处理消息越多，压缩性能越好。
     * snappy占用cpu少，提供较好的性能和可观的压缩比，如果比较关注性能和网络带宽，用这个。如果带宽紧张，用gzip，会占用较多的cpu，但提供更高的压缩比。
     */
    private String compressionType = "snappy";


    /**
     * 指定了生产者在接收到服务器响应之前可以发送多个消息，值越高，占用的内存越大，当然也可以提升吞吐量。发生错误时，可能会造成数据的发送顺序改变,
     * 默认是5。
     * 如果需要保证消息在一个分区上的严格顺序，这个值应该设为1。但会严重影响生产者的吞吐量。
     */
    private Integer maxInFlightRequestsPerConnection = 5;

    /**
     * 客户端将等待请求的响应的最大时间,如果在这个时间内没有收到响应，客户端将重发请求;超过重试次数将抛异常 默认值30000ms
     */
    private Long  requestTimeoutMs = 30000L;


    /**
     * 是指我们所获取的一些元数据的第一个时间数据。元数据包含：topic，host，partitions。此项配置是指当等待元数据fetch成功完成所需要的时间，
     * 否则会跑出异常给客户端
     */
    //private Long metadataFetchTimeoutMs;


    /**
     * 此配置选项控制broker等待副本确认的最大时间。如果确认的请求数目在此时间内没有实现，则会返回一个错误。这个超时限制是以server端度量的，
     * 没有包含请求的网络延迟。这个参数和acks的配置相匹配。
     */
    //private Long timeoutMs;


    /**
     * 控制生产者发送请求最大大小。假设这个值为1M，如果一个请求里只有一个消息，那这个消息不能大于1M，如果一次请求是一个批次，该批次包含了1000条消息，那么每个消息不能大于1KB。
     * 注意：broker具有自己对消息记录尺寸的覆盖，如果这个尺寸小于生产者的这个设置，会导致消息被拒绝。
     * 默认为1M,此处设置为5M
     *
     * 注:此处设置为5M,相应的服务端、消费端需要同步修改相关参数
     * server端:
     * message.max.bytes=5242880
     * # 每个分区试图获取的消息字节数。要大于等于message.max.bytes
     * replica.fetch.max.bytes=6291456
     *
     * consumer:
     * # 每个提取请求中为每个主题分区提取的消息字节数。要大于等于message.max.bytes
     * fetch.message.max.bytes=6291456
     *
     */
    private Long maxRequestSize = 5242880L;


    /** Consumer Config Start **/
    private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
    private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
    /**
     * 是否自动提交偏移量
     */
    private Boolean enableAutoCommit = false;
    /**
     * 设置多久一次更新被消费消息的偏移量
     */
    private Integer autoCommitIntervalMs = 1000;
    /**
     * auto.offset.reset
     * earliest:当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
     * latest:当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
     * none:topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
     */
    private String autoOffsetReset = "earliest";
    /**
     * 设置会话响应的时间，超过这个时间kafka就可以选择放弃消费或者消费下一条消息
     */
    private Integer sessionTimeoutMs = 30000;

    /**
     * 设置broker返回数据的最小字节数，默认是1字节
     */
    private Integer fetchMinBytes = 1;

    /**
     * 设置broker最长的等待时间，默认是500ms。如果一个broker接收到的新消息大小小于fetch.min.bytes的值，那么这个broker会
     * 一直等待直到时间超过配置的500ms。如果想减少延时，可以把该配置的值设小一点。此配置和上面的fetch.min.bytes是一起使用的，
     * 例如fetch.max.wait.ms=100，fetch.min.bytes=1048576(1MB)，当broker数据达到1MB或者等待了100ms时，broker都会
     * 把数据发送给消费者
     *
     */
    private Integer fetchMaxWaitMs = 500;

    /**
     * 每个提取请求中为每个主题分区提取的消息字节数
     */
    private Long fetchMessageMaxBytes = 6291456L;

    /**
     * Consumer每次调用poll()时取到的records的最大数。
     */
    private Integer maxPollRecords = 1000;
    /** Consumer Config End **/

    public KafkaConfig() {
    }

    public KafkaConfig(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public Integer getAcks() {
        return acks;
    }

    public void setAcks(Integer acks) {
        this.acks = acks;
    }

    public Long getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(Long bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public Long getMaxBlockMs() {
        return maxBlockMs;
    }

    public void setMaxBlockMs(Long maxBlockMs) {
        this.maxBlockMs = maxBlockMs;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Long getReceiveBufferBytes() {
        return receiveBufferBytes;
    }

    public void setReceiveBufferBytes(Long receiveBufferBytes) {
        this.receiveBufferBytes = receiveBufferBytes;
    }

    public Long getSendBufferBytes() {
        return sendBufferBytes;
    }

    public void setSendBufferBytes(Long sendBufferBytes) {
        this.sendBufferBytes = sendBufferBytes;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Long getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(Long lingerMs) {
        this.lingerMs = lingerMs;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public Integer getMaxInFlightRequestsPerConnection() {
        return maxInFlightRequestsPerConnection;
    }

    public void setMaxInFlightRequestsPerConnection(Integer maxInFlightRequestsPerConnection) {
        this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
    }

    public Long getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public void setRequestTimeoutMs(Long requestTimeoutMs) {
        this.requestTimeoutMs = requestTimeoutMs;
    }

    public Long getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(Long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public boolean isKerberosEnabled() {
        return kerberosEnabled;
    }

    public void setKerberosEnabled(boolean kerberosEnabled) {
        this.kerberosEnabled = kerberosEnabled;
    }

    public String getKrb5File() {
        return krb5File;
    }

    public void setKrb5File(String krb5File) {
        this.krb5File = krb5File;
    }

    public String getJaasFile() {
        return jaasFile;
    }

    public void setJaasFile(String jaasFile) {
        this.jaasFile = jaasFile;
    }

    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public Integer getAutoCommitIntervalMs() {
        return autoCommitIntervalMs;
    }

    public void setAutoCommitIntervalMs(Integer autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public Integer getFetchMaxWaitMs() {
        return fetchMaxWaitMs;
    }

    public void setFetchMaxWaitMs(Integer fetchMaxWaitMs) {
        this.fetchMaxWaitMs = fetchMaxWaitMs;
    }

    public Long getFetchMessageMaxBytes() {
        return fetchMessageMaxBytes;
    }

    public void setFetchMessageMaxBytes(Long fetchMessageMaxBytes) {
        this.fetchMessageMaxBytes = fetchMessageMaxBytes;
    }

    public Integer getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(Integer maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public Integer getFetchMinBytes() {
        return fetchMinBytes;
    }

    public void setFetchMinBytes(Integer fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }
}
