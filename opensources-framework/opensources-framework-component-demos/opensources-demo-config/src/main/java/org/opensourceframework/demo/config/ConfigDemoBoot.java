package org.opensourceframework.demo.config;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yu.ce@foxmail.com
 *
 * @since 1.0.0
 */
@SpringBootApplication
public class ConfigDemoBoot {
    public static void main(String[] args) throws Exception {
        new AbstractBoot(ConfigDemoBoot.class) {
            @Override
            public void execute() {
                OpensourceFrameworkSystem.init();
            }
        }.run(args);
    }
}
