package org.opensourceframework.starter.ratelimiter.aspect;

import org.opensourceframework.component.redis.cache.service.IRedisCacheService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 限流aop拦截类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/2 上午9:31
 */
@Aspect
@Component
public class AccessLimitAspect {
	private static final Logger logger = LoggerFactory.getLogger("AccessLimiter");

	@Autowired
	private IRedisCacheService redisCacheService;

	@Pointcut("@annotation(org.opensourceframework.starter.ratelimiter.annotation.AccessLimit)")
	public void AccessLimitAop(){

	}

	@Around("AccessLimitAop()")
	public Object accessLimitHandler(JoinPoint joinPoint)throws Throwable{
		return null;
	}
}
