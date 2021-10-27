package org.opensourceframework.common.aop;

import org.opensourceframework.base.eo.BaseEo;
import org.opensourceframework.base.microservice.ServiceContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 异步调用方法切面
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@Aspect
@Component
@Order(100)
public class AsyncMethodAspect {
	private static final Logger logger = LoggerFactory.getLogger(AsyncMethodAspect.class);
	public AsyncMethodAspect() {
	}

	@Pointcut("@annotation(org.springframework.scheduling.annotation.Async)")
	private void annotationAop() {
	}

	@Around("annotationAop()")
	public Object controllerAround(ProceedingJoinPoint point) throws Throwable {
		Object[] args = point.getArgs();
		String asyncRequestId = null;
		if(args != null && args.length > 0){
			for(int index = 0 ; index < args.length ; index ++){
				if(args[index] instanceof BaseEo){
					asyncRequestId = ((BaseEo)args[index]).getAsyncRequestId();
					if(StringUtils.isNotBlank(asyncRequestId)){
						ServiceContext.getContext().setRequestId(asyncRequestId);
						break;
					}
				}
			}
		}
		return point.proceed();
	}
}
