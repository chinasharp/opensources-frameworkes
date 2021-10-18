package org.opensourceframework.demo.lock.distributedlock;

import org.opensourceframework.base.helper.DateHelper;
import org.opensourceframework.demo.lock.service.DemoService;
import org.opensourceframework.component.redis.lock.service.ILockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis 分布式锁
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/22 上午9:51
 */
@Component
public class RedisLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);
    @Autowired
    private ILockService lockService;

    @Autowired
    private DemoService demoService;

    public void cutPayment(String account) {
        Boolean isLock = false;
        try {
            // 等待获取锁时间为5秒 如果获取到锁后10后会自动释放
            isLock = lockService.lock("cutPayment", account, 5000, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                logger.info("{} obtain redis lock success." , DateHelper.YYYYMMDDHHMMSS(new Date()));
                demoService.cutPayment(account);
            } else {
                // 没有获取到锁 do something
                logger.info("{} obtain redis lock fail." , DateHelper.YYYYMMDDHHMMSS(new Date()));
            }
        } finally {
            if(isLock) {
                lockService.unlock("cutPayment", account);
                logger.info("release redis lock success.");
            }
        }
    }
}
