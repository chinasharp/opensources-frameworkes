package org.opensourceframework.test.qlexpress;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.opensourceframework.demo.qlexpress.QLExpressDemoBoot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QLExpressDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"server.localHost=127.0.0.1",
                      "spring.application.name=opensources-user-qlexpress"})
public class BaseQlExpressTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseQlExpressTest.class);

    @Test
    public void add() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a", 1);
        context.put("b", 2);
        String express = "a+b";
        Object r = runner.execute(express, context, null, true, false);
        logger.info("res data:{}" , r);
    }



}
