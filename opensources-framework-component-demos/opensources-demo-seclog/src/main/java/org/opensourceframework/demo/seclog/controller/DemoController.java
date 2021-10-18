package org.opensourceframework.demo.seclog.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.opensourceframework.component.seclog.annotation.SecLog;
import org.opensourceframework.component.seclog.constant.FunctionNameConstant;
import org.opensourceframework.component.seclog.constant.OperateNameConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author maihaixian
 * 
 * @since 1.0.0
 */
@RestController
@Slf4j
public class DemoController {


    @PostMapping("/usersList")
    @SecLog(appName = "console", functionName= FunctionNameConstant.APPROVED_RESULTS, operateName = OperateNameConstant.SELECT)
    public void usersList(@RequestBody QueryJinJianParam param, ModelMap modelMap) {
        log.info("usersList request");
    }
}
