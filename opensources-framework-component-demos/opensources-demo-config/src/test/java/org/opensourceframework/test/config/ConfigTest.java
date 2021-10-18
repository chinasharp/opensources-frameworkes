package org.opensourceframework.test.config;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.opensourceframework.demo.config.ConfigDemoBoot;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * SURK配置测试类
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {})
@Data
@Slf4j
public class ConfigTest {

    @Value("${user.key}")
    private String demoKey;

    @Value("${opensourceframework.mq.config.registryvo.nameSrvAddr}")
    private String commonNameSrvAddr;

    @Test
    public void testApplicationNamespaceConfig() {
        log.info(demoKey);
        Assertions.assertThat(demoKey)
            .isNotEmpty();
    }

    @Test
    public void testCommonNamespaceConfig() {
        log.info(commonNameSrvAddr);
        Assertions.assertThat(commonNameSrvAddr)
            .isNotEmpty();
    }

}
