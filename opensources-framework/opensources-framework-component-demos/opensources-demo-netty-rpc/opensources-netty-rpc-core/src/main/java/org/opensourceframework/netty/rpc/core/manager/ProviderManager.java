package org.opensourceframework.netty.rpc.core.manager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务生产者管理
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 下午3:27
 */
public class ProviderManager {
	private ConcurrentHashMap<Class , Object> providerMap = new ConcurrentHashMap<>(1024);
	private static ProviderManager providerManager;
	private ProviderManager(){

	}
	public static ProviderManager instance(){
		if(providerManager == null){
			providerManager = new ProviderManager();
		}
		return providerManager;
	}

	public void putProviderBean(Object bean){
		Class<?> beanClass = bean.getClass();
		providerMap.put(beanClass , bean);

		Class<?>[] interfaceClassArray = beanClass.getInterfaces();
		for(Class interfaceClass : interfaceClassArray){
			providerMap.put(interfaceClass , bean);
		}


	}

	public Object getProviderBean(Class<?> clazz){
		return providerMap.get(clazz);
	}

}
