package org.opensourceframework.demo.lock.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * user service
 */
@Component
public class DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    public void cutPayment(String account){
        logger.info("Exec Method DemoService.cutPayment.account:{}" , account);
    }
}
