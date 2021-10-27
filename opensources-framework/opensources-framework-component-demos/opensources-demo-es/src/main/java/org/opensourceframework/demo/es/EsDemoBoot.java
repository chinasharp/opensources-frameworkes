package org.opensourceframework.demo.es;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.BaseBootApplication;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * * @author yu.ce@foxmail.com
 * 
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.opensourceframework")
public class EsDemoBoot extends BaseBootApplication {
    public static void main(String[] args) throws Exception {
        new AbstractBoot(EsDemoBoot.class) {
            @Override
            public void execute() {
                OpensourceFrameworkSystem.init();
            }
        }.run(args);
    }
}
