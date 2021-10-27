package org.opensourceframework.common.context;

import org.opensourceframework.base.microservice.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户相关信息获取
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class UserInfoContext {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoContext.class);

	public static Long getUserInfo() {
		try {
			Long userId = ServiceContext.getContext().getRequestUserId();
			logger.info(">>>>>>>>>>>>>>>>>>>获取当前用户登录Id:{}", userId);
			return userId;
		} catch (Exception e) {
			logger.error(">>>>>>>>>>>>>>>>>>>获取当前登录用户信息异常" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static Long getApplicationId() {
		try {
			Long applicationId = ServiceContext.getContext().getRequestApplicationId();
			logger.info(">>>>>>>>>>>>>>>>>>>获取当前用户应用ID:{}", applicationId);
			return applicationId;
		} catch (Exception e) {
			logger.error(">>>>>>>>>>>>>>>>>>>获取当前用户应用ID异常" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static String getRequestTerminalTypeString() {
		try {
			String terminalCode = ServiceContext.getContext().getRequestTerminalTypeString();
			logger.info(">>>>>>>>>>>>>>>>>>>获取当前终端Code:{}", terminalCode);
			return terminalCode;
		} catch (Exception e) {
			logger.error(">>>>>>>>>>>>>>>>>>>获取当前用户应用ID异常" + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
