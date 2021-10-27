package org.opensourceframework.common.log;

import org.apache.commons.collections.CollectionUtils;
import org.opensourceframework.base.helper.IpAddressHelper;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.List;

/**
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class LogConfigSetting {
    private static final Logger logger = LoggerFactory.getLogger(LogConfigSetting.class);
    public LogConfigSetting() {
    }

    private static void setUserId(Long userId) {
        MDC.put(LogConstants.REQ_USERID, String.valueOf(userId));
        String[] requestKeys = new String[]{LogConstants.REQ_USERID};

        for(int i = 0; i < requestKeys.length; ++i) {
            logger.info("LoggerFilters " + requestKeys[i] + ": " + MDC.get(requestKeys[i]));
        }

    }

    public static void initLogger() {

    }

    private static String getIp(){
        List<String> ips = IpAddressHelper.resolveLocalIps();
        String ip = null;
        if (CollectionUtils.isEmpty(ips)) {
            ip = LogConstants.LOCAL_HOST_IP;
        }else{
            ip = ips.get(ips.size() - 1);
        }
        return ip;
    }
}
