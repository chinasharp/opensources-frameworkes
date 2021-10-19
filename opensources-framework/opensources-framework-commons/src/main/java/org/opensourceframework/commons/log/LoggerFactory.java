package org.opensourceframework.commons.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * 包装slf4J的日志类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class LoggerFactory {
    public LoggerFactory() {
    }

    public static ILoggerFactory getILoggerFactory() {
        return org.slf4j.LoggerFactory.getILoggerFactory();
    }

    public static Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(String name) {
        return org.slf4j.LoggerFactory.getLogger(name);
    }
}
