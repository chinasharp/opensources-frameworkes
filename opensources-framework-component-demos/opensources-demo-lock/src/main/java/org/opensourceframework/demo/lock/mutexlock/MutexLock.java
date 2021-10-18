package org.opensourceframework.demo.lock.mutexlock;

import org.opensourceframework.demo.lock.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 互斥锁demo synchronized对象锁 适用于单节点多线程安全场景下使用
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/22 上午9:48
 */
@Component
public class MutexLock {
    @Autowired
    private DemoService demoService;

    public void cutPayment(String account){
        // 使用账号对应的字符量(常量池) 保持对象的唯一性
        synchronized (account.intern()){
            demoService.cutPayment(account);
        }
    }
}
