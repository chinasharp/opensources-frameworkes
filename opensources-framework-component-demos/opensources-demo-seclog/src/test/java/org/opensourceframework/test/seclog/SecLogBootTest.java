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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.demo.seclog.SecLogDemoBoot;
import org.opensourceframework.demo.seclog.controller.QueryJinJianParam;
import org.opensourceframework.demo.seclog.controller.UserInfo;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 审计日志SpringBoot配置的测试用例
 *
 * 
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecLogDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.application.name=opensources-user-seclog",
        "opensourceframework.mq.config.registryvo.nameSrvAddr=10.53.156.243:9876"})
public class SecLogBootTest {

    private static final Logger logger = LoggerFactory.getLogger(SecLogBootTest.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSecLogBoot() throws Exception {
        String uri = "/usersList";
        QueryJinJianParam param = new QueryJinJianParam();
        param.setApplyId("123123");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("zhangsan");
        Object[] args = new Object[]{param, userInfo};
        String paraJson = JSONObject.toJSONString(param);
        mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(paraJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        //睡眠用于异步线程测试，实际不用
        System.in.read();
    }
}
