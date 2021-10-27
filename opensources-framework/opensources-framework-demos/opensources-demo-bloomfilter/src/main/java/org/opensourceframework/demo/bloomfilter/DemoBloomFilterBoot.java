package org.opensourceframework.demo.bloomfilter;

import org.opensourceframework.common.boot.AbstractBoot;
import org.opensourceframework.common.boot.BaseBootApplication;
import org.opensourceframework.common.boot.OpensourceFrameworkSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Demo 启动类
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
@SpringBootApplication
@ComponentScan({"org.opensourceframework"})
public class DemoBloomFilterBoot extends BaseBootApplication {
    public static void main(String[] args) throws Exception{
        new AbstractBoot(DemoBloomFilterBoot.class , args){
            @Override
            public void execute() throws Exception {
                OpensourceFrameworkSystem.init();
            }
        }.run(args);
    }
}
