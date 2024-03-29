package org.opensourceframework.demo.seata.integration.call.controller;

import org.opensourceframework.demo.seata.integration.call.service.BusinessService;
import org.opensourceframework.demo.seata.integration.common.dto.BusinessDTO;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: heshouyou
 * @Description  Dubbo业务执行入口
 * @Date Created in 2019/1/14 17:15
 */
@RestController
@RequestMapping("/business/dubbo")
@Slf4j
public class BusinessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);


    @Autowired
    private BusinessService businessService;

    /**
     * 模拟用户购买商品下单业务逻辑流程
     * @Param:
     * @Return:
     */
    @GetMapping("/buy")
    ObjectResponse handleBusiness(BusinessDTO businessDTO){
        LOGGER.info("请求参数：{}",businessDTO.toString());
        return businessService.handleBusiness(businessDTO);
    }
}
