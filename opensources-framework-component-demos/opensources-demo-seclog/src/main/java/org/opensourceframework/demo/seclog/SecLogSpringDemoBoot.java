package org.opensourceframework.demo.seclog;

import org.opensourceframework.commons.boot.AbstractBoot;
import org.opensourceframework.commons.boot.BaseBootApplication;
import org.opensourceframework.commons.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 审计日志组件Spring配置的demo入口
 * pom.xml只引入opensources-framework-component-seclog，不能引入opensources-framework-starter-seclog
 * 
 * @since 1.0.0
 */
@SpringBootApplication
@ImportResource(locations = {"classpath:seclog-spring.xml"})
public class SecLogSpringDemoBoot extends BaseBootApplication {
    public static void main(String[] args) throws Exception {
        new AbstractBoot(SecLogSpringDemoBoot.class) {
            @Override
            public void execute() {
                OpensourceFrameworkSystem.init();
            }
        }.run(args);
    }
}
