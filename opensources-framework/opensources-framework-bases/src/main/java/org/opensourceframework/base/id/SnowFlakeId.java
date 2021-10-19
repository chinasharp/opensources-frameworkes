package org.opensourceframework.base.id;

import org.opensourceframework.base.eo.BaseEoUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * snowflake(雪花)算法Id生成,标准雪花如下<br>
 * 分为四要素生成一共64位<br><br>
 * 第一要素:表示正整数的标识位 固定为0  在二进制码中,为了区分正负数,采用最高位是符号位的方法来区分,正数的符号位为0、负数的符号位为1<br><br>
 * 第二要素:时间戳 41位  为 2的41次方-1,减1表示从0开始<br><br>
 * 第三要素:机器标识 10位  5位数据类型applicationId(datacenterId) 2的5次方-1 0~31  5位为workerId(机器ip) 2的5次方-1<br><br>
 * 第四要素:序列号，12位，用来记录同毫秒内产生的不同id 2的12次方-1 0~4095<br><br>
 *
 * 万达使用稍微变种构造,保持64不变,结构有所调整,结构为:<br>
 * 第一要素:固定为0 <br>
 * 第二要素:时间戳二进制(41位),从2019-01-01开始 <br>
 * 第三要素:applicationId(6位) workId(5位)  <br>
 * 第四要素:序列Id(11位)<br>
 *
 * 特殊改动点:增加环境检查参数,保证测试环境修改系统时间后也能正常运行,无须重启应用重置lastTimestamp<br>
 *
 * @author yu.ce@foxmail.com
 * @version 1.1.0
 * @date  2020-11-19
 */
public class SnowFlakeId {
    private static final Logger logger = LoggerFactory.getLogger(SnowFlakeId.class);

    private static final Long DEFAULT_APPLICATION_ID = 0L;

    private static final String ENV_KEY = "snowflake.env";
    private static final String ENV_DEV_VAL = "dev";
    private static final String ENV_TEST_VAL = "test";
    private static final String ENV_PROD_VAL = "prod";

    /**
     * 第二要素时间截
     * 取2019-01-01开始  41位 范围0 ~ 2的41次方-1
     */
    private static final long epoch = 1546272000000L;

    /**
     * 第三要素 机器Id
     * 机器id所占的位数= workerIdBits + applicationIdBits = 12位
     * 11位的长度最多支持部署4096个节点
     */
    /** workId所占位数5位 0-31 **/
    private static final long workerIdBits = 5L;
    /** 业务实例id所占的位数 范围2的6次方-1  64种不同业务数据 0-63*/
    private static final long applicationIdBits = 6L;
    /** 支持的最大workId  -1L ^ (-1L << workerIdBits) = 31L*/
    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /** 支持的最大applicationId  -1L ^ (-1L << applicationIdBits) = 63L  最大可支持64种不同的产品形态*/
    private static final long maxApplicationId = -1L ^ (-1L << applicationIdBits);

    /**
     * 第四要素 序列id 11位 范围0 ~ 2047
     */
    private static final long sequenceBits = 11L;
    /** 序列最大位数  -1L ^ (-1L << sequenceBits) = 2047L*/
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);
    private static final Random random = new Random();
    /** 毫秒内序列(0~2047) */
    private static long sequence = 0L;

    /** sequenceBits为11位,因此workId向左移12位 workerIdShift = sequenceBits = 11L*/
    private static final long workerIdShift = sequenceBits;

    /** workerIdBits为5位,因此applicationId向左移 workerIdBits + sequenceBits = 5 + 11 = 16L */
    private static final long applicationIdShift = workerIdBits + sequenceBits;

    /** 时间截向左移 sequenceBits + workerIdBits + applicationIdBits = 22L*/
    private static final long timestampShift = applicationIdBits + workerIdBits + sequenceBits;

    /** 上次生成ID的时间截 */
    private static long lastTimestamp = -1L;

    /**
     * 获取id
     *
     * @param workerId  0-31       workerId为空则使用部署该Id生产器的服务器IP计算出workId<BR>
     * @param applicationId 0-63   applicationId为空则使用当前时间计算出applicationId<BR>
     * @return
     */
    public static synchronized Long nextId(Long workerId, Long applicationId) {
        if(workerId == null){
            workerId = getWorkerId();
        }
        workerId = Math.abs(workerId);
        if(workerId > maxWorkerId){
            workerId = workerId % maxWorkerId;
        }


        if(applicationId == null){
            applicationId = DEFAULT_APPLICATION_ID;
        }
        applicationId = Math.abs(applicationId);
        if(applicationId > maxApplicationId){
            applicationId = applicationId % maxApplicationId;
        }

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        // 当前时间如果小于上一次id的生成时间,则表明系统修改过时间,此时需要判断是否为生成环境,如果为测试或开发环境则忽略,如果为生产环境则抛出异常
        if(lastTimestamp > timestamp){
            boolean isProd = checkProdEnv();
            if(isProd){
                logger.error(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
                //throw new BizException(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }
            sequence = 0L;
        }else if (lastTimestamp == timestamp) {
            //如果是同一时间生成的，则进行毫秒内序列 相同毫秒内，序列号自增
            sequence = (sequence + 1L) & sequenceMask;

            //毫秒内序列溢出 ,同一毫秒的序列数已经达到最大 2的N次方 & (2的N次方 - 1） = 0
            if (sequence == 0L) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = nextMillis(lastTimestamp);
            }
        } else {
            //时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        // 记录生成id的时间戳
        lastTimestamp = timestamp;
        return (Math.abs(timestamp - epoch) << timestampShift) | (applicationId << applicationIdShift) | (workerId << workerIdShift) | sequence;

    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private static long nextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    private static Long getWorkerId() {
        return BaseEoUtil.getWorkerId();
    }

    /**
     * 检查是否为生产环境
     * 生产环境:snowflake.env的值为空或者prod 其它值一律认为不是生产环境
     * @return
     */
    private static Boolean checkProdEnv(){
        boolean isProd = true;
        String envValue = System.getProperty(ENV_KEY);
        if(StringUtils.isNotBlank(envValue) && !ENV_PROD_VAL.equalsIgnoreCase(envValue)){
            isProd = false;
        }
        return isProd;
    }

    /**
     * 系统自动applicationId
     *
     * @param data
     * @return
     */
    public static Long getApplicationId(Object data){
        Long applicationId = null;
        if(data != null){
            int hashCode = data.hashCode();
            if(hashCode == Integer.MIN_VALUE){
                applicationId = Long.valueOf(hashCode % maxApplicationId);
            }else {
                applicationId = Long.valueOf(Math.abs(hashCode) % maxApplicationId);
            }
        }else{
            applicationId = DEFAULT_APPLICATION_ID;
        }
        return applicationId;
    }
}
