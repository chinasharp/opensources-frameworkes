package org.opensourceframework.component.redis.common;

import org.opensourceframework.base.exception.BizException;
import org.opensourceframework.component.redis.cache.config.RedisConfig;
import org.opensourceframework.component.redis.cache.contants.WorkModel;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建RedissonClient工厂类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class RedissonService {
    private static final Logger logger = LoggerFactory.getLogger(RedissonService.class);
    protected static final String ADDRESS_PREFIX="redis://";

    public static RedissonClient buildRedissonClient(RedisConfig redisConfig){
        logger.info("WorkModel:{} , address:{}" ,redisConfig.getWorkModel() , redisConfig.getAddresses() );
        Config config = new Config();
        if (WorkModel.SINGLE.getName().equals(redisConfig.getWorkModel())) {
            SingleServerConfig singleConfig = config.useSingleServer();
            String[] addresses = redisConfig.getAddresses();
            if(addresses != null && addresses.length > 0){
                singleConfig.setAddress(buildRealAddress(addresses[0])[0]);
            }else{
                if(StringUtils.isBlank(redisConfig.getHost()) || StringUtils.isBlank(redisConfig.getPort())){
                    throw new BizException("init redis SINGLE registryvo error. not found redis server host:port registryvo.");
                }
                singleConfig.setAddress(ADDRESS_PREFIX.concat(redisConfig.getHost().concat(":").concat(redisConfig.getPort())));
            }

            if (StringUtils.isNotBlank(redisConfig.getAuthPwd())) {
                singleConfig.setPassword(redisConfig.getAuthPwd());
            }
        } else if (WorkModel.SENTINE.getName().equals(redisConfig.getWorkModel())) {
            SentinelServersConfig sentinelConfig = config.useSentinelServers();
            String[] addresses = buildRealAddress(redisConfig.getAddresses());
            if(addresses == null || addresses.length == 0){
                throw new BizException("init redis SENTINE registryvo error. check redis server addresses registryvo.");
            }
            sentinelConfig.addSentinelAddress(addresses);
            if (StringUtils.isNotBlank(redisConfig.getAuthPwd())) {
                sentinelConfig.setPassword(redisConfig.getAuthPwd());
            }
        } else if (WorkModel.CLUSTER.getName().equals(redisConfig.getWorkModel())) {
            ClusterServersConfig clusterConfig = config.useClusterServers();
            // 集群状态扫描间隔时间，单位是毫秒
            clusterConfig.setScanInterval(5000);
            clusterConfig.setPingConnectionInterval(30000);

            String[] redissionAddresses = buildRealAddress(redisConfig.getAddresses());
            if(redissionAddresses == null || redissionAddresses.length == 0){
                throw new BizException("init redis CLUSTER registryvo error. check redis server addresses registryvo.");
            }
            logger.info("redission config address:{}" ,redissionAddresses );
            clusterConfig.addNodeAddress(redissionAddresses);
            if (StringUtils.isNotBlank(redisConfig.getAuthPwd())) {
                clusterConfig.setPassword(redisConfig.getAuthPwd());
            }
        }
        return Redisson.create(config);
    }

    private static String[] buildRealAddress(String... addresses){
        String[] realAddresses = null;
        if(addresses != null && addresses.length > 0){
            realAddresses = new String[addresses.length];
            int index = 0;
            for(String address : addresses){
                realAddresses[index ++] = ADDRESS_PREFIX.concat(address);
            }
        }
        return realAddresses;
    }
}
