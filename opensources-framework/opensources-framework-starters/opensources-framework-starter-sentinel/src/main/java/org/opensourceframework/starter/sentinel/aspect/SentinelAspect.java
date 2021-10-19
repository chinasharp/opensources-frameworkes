package org.opensourceframework.starter.sentinel.aspect;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/7 上午1:55
 */
@Aspect
@Component
@Slf4j
public class SentinelAspect {
	@Pointcut("@annotation(org.opensourceframework.starter.sentinel.annotation.Sentinel)")
	private void sentinelAop(){}

	@Around("sentinelAop()")
	public Object sentinelHandler(ProceedingJoinPoint joinPoint) throws Throwable{
		Signature signature = joinPoint.getSignature();
		if(signature instanceof MethodSignature) {
			MethodSignature methodSignature = (MethodSignature) signature;
			Method targetMethod = methodSignature.getMethod();
			Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes());
			log.info("[SentinelAspect] targetMethod:{} , realMethod:{}" , targetMethod.getName() , realMethod.getName());
			Entry entry = null;
			try {
				entry = SphU.entry(joinPoint.getTarget().getClass().toString().concat(realMethod.getName()));
				joinPoint.proceed();
			}catch (BlockException e){
				throw new Exception(e.getMessage());
			}finally {
				if(entry != null){
					entry.exit();
				}
			}



			return null;

		}else {
			return joinPoint.proceed();
		}
	}




}
