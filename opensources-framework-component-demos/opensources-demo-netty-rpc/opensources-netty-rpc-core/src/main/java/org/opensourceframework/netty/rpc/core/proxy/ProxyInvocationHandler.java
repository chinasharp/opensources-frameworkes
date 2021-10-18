package org.opensourceframework.netty.rpc.core.proxy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.base.id.SnowFlakeId;
import org.opensourceframework.netty.rpc.core.dto.RpcRequest;
import org.opensourceframework.netty.rpc.core.dto.RpcResponse;
import org.opensourceframework.netty.rpc.core.handler.ConsumerHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理对象反射调用target对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 下午5:46
 */
public class ProxyInvocationHandler implements InvocationHandler{
	private Class<?> interfaceClass;

	public ProxyInvocationHandler(Class<?> interfaceClass){
		this.interfaceClass = interfaceClass;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
		RpcRequest rpcRequest = new RpcRequest();
		rpcRequest.setReqId(SnowFlakeId.nextId(null , 1L).toString());
		rpcRequest.setRpcClass(interfaceClass);
		rpcRequest.setRpcMethod(method.getName());
		rpcRequest.setReturnType(method.getReturnType());

		Class<?>[] paramTypes = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length ; i++){
			paramTypes[i] = parameters[i].getClass();
		}
		rpcRequest.setParamTypes(paramTypes);
		rpcRequest.setParamValues(parameters);

		ConsumerHandler consumerHandler = ConsumerHandler.instance();
		RpcResponse rpcResponse = consumerHandler.invoke(rpcRequest);
		Object obj = rpcResponse.getRes();
		if(obj instanceof JSONObject){
			obj =  ((JSONObject) obj).toJavaObject(method.getReturnType());
		}

		if(obj instanceof JSONArray){
			obj = ((JSONArray)obj).toJavaList(method.getReturnType());
		}

		return obj;
	}
}
