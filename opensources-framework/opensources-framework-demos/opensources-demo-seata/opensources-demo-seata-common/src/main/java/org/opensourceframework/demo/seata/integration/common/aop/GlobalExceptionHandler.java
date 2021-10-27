package org.opensourceframework.demo.seata.integration.common.aop;

import org.opensourceframework.demo.seata.integration.common.exception.DefaultException;
import org.opensourceframework.demo.seata.integration.common.enums.RspStatusEnum;
import org.opensourceframework.demo.seata.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @since 1.0.0
 */
@ControllerAdvice(basePackages = "com.sinochem.finance.hsy")
@Slf4j
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ObjectResponse exceptionHandler(Exception e){
        LOGGER.error("【系统抛出Exception异常】 —— 异常内容如下：{}" , e);
        ObjectResponse objectResponse = new ObjectResponse<>();
        objectResponse.setStatus(RspStatusEnum.FAIL.getCode());
        objectResponse.setMessage(RspStatusEnum.FAIL.getMessage());
        return objectResponse;
    }

    @ExceptionHandler(DefaultException.class)
    @ResponseBody
    public ObjectResponse defaultException(DefaultException e){
        LOGGER.error("【系统抛出SinochemException异常】 —— 异常内容如下：{}" , e);
        ObjectResponse objectResponse = new ObjectResponse<>();
        objectResponse.setStatus(RspStatusEnum.FAIL.getCode());
        objectResponse.setMessage(RspStatusEnum.FAIL.getMessage());
        return objectResponse;
    }
}
