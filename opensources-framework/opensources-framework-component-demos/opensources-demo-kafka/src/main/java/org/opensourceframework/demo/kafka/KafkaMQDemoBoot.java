package org.opensourceframework.demo.kafka;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.BaseBootApplication;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/***
 *
 * 明编码方式发送和订阅MQ
 *
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class KafkaMQDemoBoot extends BaseBootApplication {
    public static void main(String[] args) throws Exception {
        new AbstractBoot(KafkaMQDemoBoot.class, Banner.Mode.CONSOLE, args) {
            @Override
            public void execute() {
                OpensourceFrameworkSystem.init();
            }
        }.run();


    }
}



