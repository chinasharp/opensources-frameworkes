package org.opensourceframework.demo.lock.spinlock;

import org.opensourceframework.demo.lock.service.DemoService;
import org.opensourceframework.demo.lock.ILock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁 user  适用于单节点多线程安全场景下使用
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/22 上午9:21
 */
@Component
public class SpinLock implements ILock {
	@Autowired
	private DemoService demoService;
	private AtomicReference<Thread> locker = new AtomicReference<>();

	private SpinLock(){
	}

	/**
	 * 获取锁 并执行业务逻辑
	 *
	 * @param woker
	 * @return
	 */
	private Object lock(LockWoker woker){
		Object res = null;
		Thread thread = Thread.currentThread();

		Boolean isLock =  locker.compareAndSet(null , thread);
		if(isLock){
			res = woker.run();
		}else {
			long waitStartTime = System.currentTimeMillis();
			while (true) {
				if(locker.compareAndSet(null, thread)) {
					res = woker.run();
					break;
				}
			}
		}
		return res;
	}

	private void unLock(){
		Thread thread = Thread.currentThread();
		locker.compareAndSet(thread , null);
	}

	public interface LockWoker{
		Object run();
	}

	@Override
	public void cutPayment(String account){
		try {
			Object obj = lock(new LockWoker() {
				@Override
				public Object run() {
					demoService.cutPayment(account);
					return true;
				}
			});
		}finally {
			unLock();
		}
	}

}
