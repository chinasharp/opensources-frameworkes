package org.opensourceframework.common.filter;


import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.common.http.HttpRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SkyWalking 集成
 *
 * @since 1.0.0
 */
@Slf4j
public class SkyWalkingRequestFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SkyWalkingRequestFilter.class);

    private static final String SKYWALKING_HEADER_ID = "tid";
    private static final String SKYWALKING_TAG_REQ = "request";
    private static final String SKYWALKING_TAG_ERROR = "error";

    @Override
    @Trace(operationName = "skyWalkingRequestFilter")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        String uri = httpServletRequest.getRequestURI();
        String remoteAddr = httpServletRequest.getRemoteAddr();
        String method = httpServletRequest.getMethod();
        String contentType = httpServletRequest.getContentType();
        String requestStr = "";
        if(StringUtils.isBlank(contentType)){
            contentType = "";
        }
        try{
            httpServletResponse.setHeader(SKYWALKING_HEADER_ID, TraceContext.traceId());
            if(contentType.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    ||contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)
                    ||StringUtils.equals(method,HttpMethod.GET.name())){
                requestStr = JSONObject.toJSONString(httpServletRequest.getParameterMap());
                filterChain.doFilter(httpServletRequest, servletResponse);
            }else if(contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)){
                HttpRequestWrapper requestWrapper = new HttpRequestWrapper(
                        (HttpServletRequest) servletRequest);
                requestStr = requestWrapper.getBodyString();
                filterChain.doFilter(requestWrapper, httpServletResponse);
            }else{
                filterChain.doFilter(servletRequest, httpServletResponse);
            }
        }catch (Exception e){
            ActiveSpan.tag(SKYWALKING_TAG_ERROR ,e.getMessage());
            filterChain.doFilter(servletRequest, httpServletResponse);
        }
        ActiveSpan.tag(SKYWALKING_TAG_REQ, requestStr);
        logger.info("url:{} , method:{}, clientIp:{}, params:{},contentType:{}", uri, method, remoteAddr, requestStr,contentType);
    }
}

