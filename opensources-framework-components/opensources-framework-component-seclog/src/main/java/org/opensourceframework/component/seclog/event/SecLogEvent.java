package org.opensourceframework.component.seclog.event;

import org.springframework.context.ApplicationEvent;

/**
 * 审计日志事件
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
public class SecLogEvent extends ApplicationEvent {

    public SecLogEvent(Object source) {
        super(source);
    }
}
