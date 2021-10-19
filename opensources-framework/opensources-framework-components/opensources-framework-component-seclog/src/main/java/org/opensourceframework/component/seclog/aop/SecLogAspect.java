package org.opensourceframework.component.seclog.aop;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.opensourceframework.component.seclog.annotation.SecLog;
import org.opensourceframework.component.seclog.event.SecLogEvent;
import org.opensourceframework.component.seclog.helper.UserHelper;
import org.opensourceframework.component.seclog.vo.SecLogConvertVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.opensourceframework.component.httpclient.helper.WebHelper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计日志aop异步传输
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Aspect
@Slf4j
public class SecLogAspect {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Around("@annotation(secLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint joinPoint, SecLog secLog) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取ip
        String ip = WebHelper.getIpAdrress(request);
        // 获取路径
        String path = request.getRequestURI();
        // 获取userName
        String userName = UserHelper.getCurrentUserName();
        // 异步转换
        SecLogConvertVo secLogConvertVo = new SecLogConvertVo(joinPoint, secLog, ip, path, userName);
        publisher.publishEvent(new SecLogEvent(secLogConvertVo));
        // 返回
        Object obj = joinPoint.proceed();
        return obj;
    }
}
