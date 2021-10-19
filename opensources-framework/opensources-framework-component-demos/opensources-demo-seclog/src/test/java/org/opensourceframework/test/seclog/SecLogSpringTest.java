package org.opensourceframework.test.seclog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.opensourceframework.demo.seclog.SecLogSpringDemoBoot;

/**
 * 审计日志Spring配置的测试用例
 * pom.xml只引入opensources-framework-component-seclog，不能引入opensources-framework-starter-seclog
 * 
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecLogSpringDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.application.name=opensources-user-seclog",
        "opensourceframework.mq.config.registryvo.nameSrvAddr=10.53.156.243:9876"})
public class SecLogSpringTest {

    private static final Logger logger = LoggerFactory.getLogger(SecLogSpringDemoBoot.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSecLogSpring() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/usersList")
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .param("name", "Tom"))
            .andExpect(MockMvcResultMatchers.status()
                .isOk());
        //睡眠用于异步线程测试，实际不用
        Thread.sleep(3000L);
    }
}
