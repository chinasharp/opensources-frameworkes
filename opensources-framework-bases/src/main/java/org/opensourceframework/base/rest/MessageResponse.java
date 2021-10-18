package org.opensourceframework.base.rest;

import org.opensourceframework.base.constants.CommonCanstant;
import io.swagger.annotations.ApiModel;

/**
 * 发送响应对象
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@ApiModel(value = "MessageResponse",description = "消息响应对象")
public class MessageResponse<T> extends RestResponse<T> {
	public static final MessageResponse SUCCESS = new MessageResponse(CommonCanstant.SUCCESS_CODE, CommonCanstant.SUCESS_MSG);
	public static final MessageResponse FAIL = new MessageResponse(CommonCanstant.FAILURE_CODE, CommonCanstant.FAILURE_MSG);
	public static final MessageResponse ERROR = new MessageResponse(CommonCanstant.ERROR_CODE, CommonCanstant.ERROR_MSG);

	public MessageResponse() {
	}

	public MessageResponse(T data) {
		super(data);
	}

	public MessageResponse(int errCode, String errMsg) {
		super(errCode, errMsg);
	}
}
