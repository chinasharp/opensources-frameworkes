package org.opensourceframework.component.seclog.vo;

import org.aspectj.lang.ProceedingJoinPoint;

import org.opensourceframework.component.seclog.annotation.SecLog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecLogConvertVo {

    private ProceedingJoinPoint joinPoint;

    private SecLog secLog;

    private String ip;

    private String path;

    private String operator;
}
