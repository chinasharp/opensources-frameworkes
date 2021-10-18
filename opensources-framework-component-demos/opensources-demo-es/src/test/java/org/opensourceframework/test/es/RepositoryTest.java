package org.opensourceframework.test.es;

import org.opensourceframework.demo.es.EsDemoBoot;
import org.opensourceframework.demo.es.dao.OrderRecordToTelemarketDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * * @author yu.ce@foxmail.com
 * 
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class RepositoryTest {

    @Resource
    private OrderRecordToTelemarketDao orderRecordToTelemarketDao;

    @Test
    public void testFindById() {

    }

    @Test
    public void testFindByUserId() {

    }

}
