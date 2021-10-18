package org.opensourceframework.starter.mybatis.base.threadquery;

import org.opensourceframework.base.db.Condition;
import org.opensourceframework.component.dao.annotation.ShardingTable;
import org.opensourceframework.component.dao.contant.SqlStatementContant;
import org.opensourceframework.starter.mybatis.base.dao.BaseShardingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程查询分表
 * @author yuce
 */
public class MultiHashTableExecutor<T> {
	private static final Logger logger = LoggerFactory.getLogger(MultiHashTableExecutor.class);

	private final BaseShardingDao baseShardingDao;

	public MultiHashTableExecutor(BaseShardingDao baseShardingDao) {
		this.baseShardingDao = baseShardingDao;
	}

	/**
	 * 执行查询
	 *
	 * @param eoClass          查询数据的EO Class
	 * @param whereMap         方法的参数,此处规定invokeMethod的参数必须是Map类型
	 * @return
	 */
	public List<T> execSelect(Class<?> eoClass , Map<String, Object> whereMap ) {
		return exec(eoClass , baseShardingDao.getClass() , SqlStatementContant.QUERY_SHARDING_DATA_NOT_KEY_METHOD, whereMap);
	}

	/**
	 * 执行查询
	 *
	 * @param eoClass          查询数据的EO Class
	 * @param condition        查询条件对象
	 * @return
	 */
	public List<T> execSelect(Class<?> eoClass , Condition condition , Integer limit){
		Map<String , Object> paramMap = new HashMap<>();
		paramMap.put(SqlStatementContant.WHERE_CONDITION_KET , condition);
		if(limit == null){
			limit = 500;
		}
		paramMap.put(SqlStatementContant.QUERY_DATA_LIMIT_PARAM , limit);
		return exec(eoClass , baseShardingDao.getClass() , SqlStatementContant.QUERY_SHARDING_DATA_NOT_KEY_METHOD, paramMap);
	}

	/**
	 * 执行更新
	 *
	 * @param eoClass
	 * @param whereMap
	 * @param updateMap
	 * @return
	 */
	public List<T> execUpdate(Class<?> eoClass , Map<String, Object> whereMap , Map<String ,Object> updateMap){
		Map<String , Object> paramMap = new HashMap<>();
		paramMap.put(SqlStatementContant.WHERE_MAP_KEY , whereMap);
		paramMap.put(SqlStatementContant.UPDATE_MAP_KET , updateMap);
		return exec(eoClass , baseShardingDao.getClass() , SqlStatementContant.UPDATE_SHARDING_DATA_NOT_KEY_METHOD, paramMap);
	}

	/**
	 * 执行删除
	 *
	 * @param eoClass          查询数据的EO Class
	 * @param whereMap         方法的参数,此处规定invokeMethod的参数必须是Map类型
	 * @return
	 */
	public List<T> execDelate(Class<?> eoClass ,  Map<String, Object> whereMap){
		return exec(eoClass , baseShardingDao.getClass() , SqlStatementContant.DELETE_SHARDING_DATA_NOT_KEY_METHOD, whereMap);
	}

	/**
	 * 执行删除
	 *
	 * @param eoClass          查询数据的EO Class
	 * @param condition        查询条件对象
	 * @return
	 */
	public List<T> execDelate(Class<?> eoClass , Condition condition){
		Map<String , Object> paramMap = new HashMap<>();
		paramMap.put(SqlStatementContant.WHERE_CONDITION_KET , condition);
		return exec(eoClass , baseShardingDao.getClass() , SqlStatementContant.DELETE_SHARDING_DATA_NOT_KEY_METHOD, paramMap);
	}


	/**
	 * @param eoClass          查询数据的EO Class
	 * @param invokeClassBean  执行的Class
	 * @param invokeMethod     执行的方法名
	 * @param paramMap         方法的参数,此处规定invokeMethod的参数必须是Map类型
	 * @return
	 */
	public List<T> exec(Class<?> eoClass, Class<?> invokeClassBean, String invokeMethod, Map<String, Object> paramMap) {
		long start = System.currentTimeMillis();

		// 设置线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(ThreadConstant.THREAD_SIZE, ThreadConstant.THREAD_SIZE,
				0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

		List<FutureTask<Object>> taskList = new ArrayList<FutureTask<Object>>();
		ShardingTable shardingTable = eoClass.getAnnotation(ShardingTable.class);
		int tableNum = shardingTable.tableCount();
		String tableNumParam = shardingTable.tableNumParam();

		for (int i = 0; i < tableNum; i++) {
			Map<String, Object> methodParamMap = new HashMap<String, Object>();
			if (!paramMap.isEmpty()) {
				methodParamMap.putAll(paramMap);
				methodParamMap.put(tableNumParam, i);
				methodParamMap.put(SqlStatementContant.CLASS_PARAM, eoClass);
			}
			Object[] invokeParams = new Object[] {methodParamMap };
			Class<?>[] invokeParamTypes = new Class[] { Map.class };
			SubHashTableCallable collectCall = new SubHashTableCallable(invokeClassBean, invokeMethod, invokeParams, invokeParamTypes);
			// 创建每条指令的采集任务对象
			FutureTask<Object> collectTask = new FutureTask<>(collectCall);
			// 添加到list,方便后面取得结果
			taskList.add(collectTask);
			// 提交给线程池
			threadPool.submit(collectTask);
		}

		List<T> results = new ArrayList<T>();
		try {
			for (FutureTask<Object> taskF : taskList) {
				// 防止某个子线程查询时间过长 超过默认时间没有拿到抛出异常
				Object obj = taskF.get(Long.MAX_VALUE, TimeUnit.DAYS);
				if (obj != null) {
					if (obj instanceof List) {
						results.addAll((List<T>) obj);
					} else {
						results.add((T) obj);
					}
				}else{
					logger.info("null");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 处理完毕,关闭线程池,这个不能在获取子线程结果之前关闭,因为如果线程多,执行中的可能被打断
		threadPool.shutdown();

		if (logger.isDebugEnabled()) {
			logger.debug("{} cast time:{}Millis. ", invokeMethod, System.currentTimeMillis() - start);

		}

		return results;
	}
}
