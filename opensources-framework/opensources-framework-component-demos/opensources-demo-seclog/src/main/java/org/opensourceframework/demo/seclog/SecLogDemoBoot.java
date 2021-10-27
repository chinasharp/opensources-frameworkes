package org.opensourceframework.demo.seclog;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.BaseBootApplication;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 审计日志组件SpringBoot配置的demo入口
 * pom.xml引入opensources-framework-starter-seclog
 *
 * 
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class SecLogDemoBoot extends BaseBootApplication {
    public static void main(String[] args) throws Exception {
        new AbstractBoot(SecLogSpringDemoBoot.class) {
            @Override
            public void execute() {
                OpensourceFrameworkSystem.init();
            }
        }.run(args);
    }
}
