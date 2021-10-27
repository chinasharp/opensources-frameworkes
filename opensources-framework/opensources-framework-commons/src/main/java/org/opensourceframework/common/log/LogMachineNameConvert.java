package org.opensourceframework.common.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.opensourceframework.base.helper.IpAddressHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 记录日志输出机器名 
 *
 * @Snice: 1.0.0     
 */
public class LogMachineNameConvert extends ClassicConverter {
    private static final Logger logger = LoggerFactory.getLogger(LogMachineNameConvert.class);
    /**
     * 获取主机名称
     */
    public static String machineName = "";

    static {
        try {
            machineName = IpAddressHelper.getLocalHost();
        } catch (Exception e) {
            logger.error("日志记录获取主机机器名出现异常:{}", e);
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return machineName;
    }

}