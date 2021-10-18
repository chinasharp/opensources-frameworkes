package org.opensourceframework.component.httpclient.helper;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Web工具类
 *
 * @author maihaixian
 * 
 * @since 1.0.0
 */
public class WebHelper {

    /**
     * 获取用户Ip地址
     * @param request
     * @return
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if(index != -1){
                return xFor.substring(0,index);
            }else{
                return xFor;
            }
        }
        xFor = xip;
        if(StringUtils.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)){
            return xFor;
        }
        if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }

}
