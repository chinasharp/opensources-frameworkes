package org.opensourceframework.netty.rpc.core.dto;

import java.io.Serializable;

/**
 * Rpc响应通用传值对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 下午5:05
 */
public class RpcResponse implements Serializable {
	private String reqId;
	protected Object res;

	public RpcResponse() {
	}

	public RpcResponse(String reqId, Object res) {
		this.reqId = reqId;
		this.res = res;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public Object getRes() {
		return res;
	}

	public void setRes(Object res) {
		this.res = res;
	}
}
