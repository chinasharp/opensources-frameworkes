package org.opensourceframework.starter.mybatis.base.threadquery;

/**
 * 多线程执行数据库常量
 *
 * @author 空见
 * @since 1.0.0
 * @date  2019/2/22 10:19 AM
 */
public class ThreadConstant {
	/**
	 * 每核Cpu负载的最大线程队列数
	 */
	public static final float POOL_SIZE = 1.5f;

	public static final int THREAD_SIZE;

	static {
		int cpuNums = Runtime.getRuntime().availableProcessors();
		float MathNum = cpuNums * ThreadConstant.POOL_SIZE;
		THREAD_SIZE = (int) MathNum;
	}
}
