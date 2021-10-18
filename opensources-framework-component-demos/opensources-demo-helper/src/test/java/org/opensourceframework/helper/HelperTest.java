package org.opensourceframework.helper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.opensourceframework.base.helper.BeanHelper;
import org.opensourceframework.base.vo.MessageVo;
import org.opensourceframework.demo.helper.HelperDemoBoot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelperDemoBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {"server.localHost=127.0.0.1",
		"spring.application.name=opensources-user-helper"})
public class HelperTest {
	private static final Logger logger = LoggerFactory.getLogger(HelperTest.class);

	@Test
	public void copyProperties(){
		MessageVo messageVo = new MessageVo();
		messageVo.setBizName("test");
		HashMap<String, String> extendMap = Maps.newHashMap();
		extendMap.put("id" , "1111");
		extendMap.put("name" , "test");
		messageVo.setExtendMap(extendMap);

		MessageVo copyMessgaeVo = new MessageVo();
		BeanHelper.copyProperties(copyMessgaeVo , messageVo);
		logger.info("copy config:{}" , JSON.toJSONString(copyMessgaeVo));
	}
}
