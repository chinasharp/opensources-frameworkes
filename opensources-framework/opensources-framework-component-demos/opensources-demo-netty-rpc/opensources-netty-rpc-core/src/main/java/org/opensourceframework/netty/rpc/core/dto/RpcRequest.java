package org.opensourceframework.netty.rpc.core.dto;

import java.io.Serializable;

/**
 * Rpc请求通用传值对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 上午9:05
 */
public class RpcRequest implements Serializable {
	private Class<?> rpcClass;
	private String rpcMethod;
	private Class<?>[] paramTypes;
	private Object[] paramValues;
	private String reqId;
	private Class<?> returnType;

	public RpcRequest() {
	}

	public RpcRequest(Class<?> rpcClass, String rpcMethod, Class<?>[] paramTypes, Object[] paramValues) {
		this.rpcClass = rpcClass;
		this.rpcMethod = rpcMethod;
		this.paramTypes = paramTypes;
		this.paramValues = paramValues;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public Class<?> getRpcClass() {
		return rpcClass;
	}

	public void setRpcClass(Class<?> rpcClass) {
		this.rpcClass = rpcClass;
	}

	public String getRpcMethod() {
		return rpcMethod;
	}

	public void setRpcMethod(String rpcMethod) {
		this.rpcMethod = rpcMethod;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public Object[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
}
