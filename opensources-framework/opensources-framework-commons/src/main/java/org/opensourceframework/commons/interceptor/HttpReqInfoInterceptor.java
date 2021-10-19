package org.opensourceframework.commons.interceptor;

import org.opensourceframework.base.helper.StringHelper;
import org.opensourceframework.commons.http.HttpRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截计算执行时间
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class HttpReqInfoInterceptor extends RestInterceptorWraper {
    private static final Logger logger = LoggerFactory.getLogger(HttpReqInfoInterceptor.class);
    private static final String REQ_START_TIME = "http_req_start_time";
    private static final String INFO_FOOT = "-----------------------------------------------------------------";

    public HttpReqInfoInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        StringBuffer url = request.getRequestURL();
        if(StringUtils.isNotBlank(request.getQueryString())){
            url.append("?").append(request.getQueryString());
        }

        String contentType = request.getContentType();
        if(!(request instanceof StandardMultipartHttpServletRequest)) {
            HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper(request);
            String reqInfo = httpRequestWrapper.getBodyString();

            if (reqInfo == null || StringUtils.isBlank(reqInfo)) {
                reqInfo = StringHelper.map2String(httpRequestWrapper.getParameterMap());
            }

            logger.info("\nrequest contentType:{}\nrequest url:{} \nrequest body:\n{}\n".concat(INFO_FOOT).concat("\n"),
                    contentType, url, reqInfo);

            request.setAttribute(REQ_START_TIME, System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mav) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
        if(!(request instanceof StandardMultipartHttpServletRequest)) {
            Long start = (Long) request.getAttribute(REQ_START_TIME);
            long end = System.currentTimeMillis();
            logger.info("req url:{} cast time:{} millis ", request.getRequestURI(), end - start);
        }
    }
}
