package org.opensourceframework.component.seclog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审计日志注解
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SecLog {

    /**
     * 系统模块
     */
    String appName() default "";

    /**
     * 业务模块
     */
    String functionName() default "";

    /**
     * 业务属性
     */
    String operateName() default "";

    /**
     * 自定义日志文本
     */
    String context() default "";
}
