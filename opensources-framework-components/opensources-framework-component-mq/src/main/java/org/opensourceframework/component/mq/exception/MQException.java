package org.opensourceframework.component.mq.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ 异常定义类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class MQException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    protected static Map<Integer, String> mapMessage = new HashMap();
    public static final int SYSTEM_INTERNAL_ERROR = 10001;
    public static final int PARAMETER_ERROR = 10002;
    public static final int RECORD_DUPLICATED = 11001;
    public static final int RECORD_NOTEXIST = 11002;
    public static final int APIGATEWAY_ERROR = 99999;
    protected String code;
    protected String rawMessage;

    public MQException(String message) {
        this("10001", message);
    }

    public MQException(String message, Throwable e) {
        this("10001", message, e);
    }

    public MQException(String code, String message) {
        super("{\"code\":\"" + code + "\", \"message\":\"" + message + "\"}");
        this.code = null;
        this.rawMessage = null;
        this.code = code;
        this.rawMessage = message;
    }

    public MQException(String code, String message, Throwable e) {
        super("{\"code\":\"" + code + "\", \"message\":\"" + message + "\"}", e);
        this.code = null;
        this.rawMessage = null;
        this.code = code;
        this.rawMessage = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getRawMessage() {
        return this.rawMessage;
    }

    public String MQException() {
        //return JSON.toJSONString(new RestResponse(this.code, this.rawMessage));
        return null;
    }

    public static String getMessage(int code) {
        return mapMessage.get(code);
    }

    static {
        mapMessage.put(10001, "系统内部错误");
        mapMessage.put(10002, "业务参数异常");
        mapMessage.put(11001, "记录重复");
        mapMessage.put(11002, "记录不存在");
    }
}
