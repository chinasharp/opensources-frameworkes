package org.opensourceframework.demo.bloomfilter.init;

import org.opensourceframework.demo.bloomfilter.cache.DemoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化数据
 *
 * @author yu.ce@foxmail.com
 * 
 * @since  1.0.0
 */
@Component
public class InitApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(InitApplication.class);
    @Autowired
    private DemoCache demoCache;
    @Override
    public void run(String... args) throws Exception {
        logger.info("\n ------------Init System Data Start --------------");
        demoCache.init();
        logger.info("\n ------------Init System Data End --------------");
    }
}
