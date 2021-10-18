package org.opensourceframework.commons.filter;

import org.opensourceframework.base.microservice.ServiceContext;
import org.opensourceframework.base.microservice.AbstractMicroServiceContext;
import org.opensourceframework.commons.log.LogConstants;
import org.opensourceframework.commons.log.LoggerFactory;
import org.opensourceframework.commons.log.RequestId;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志信息传递
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 */
public class LoggerServletFilter implements Filter, LogConstants {
    private static final Logger logger = LoggerFactory.getLogger(LoggerServletFilter.class);

    public LoggerServletFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.beforeFilter(request);

        try {
            chain.doFilter(request, response);
        } catch (ServletException | IOException e) {
            logger.error("过滤异常！", e);
            throw e;
        } finally {
            this.afterFilter();
        }

    }

    void beforeFilter(ServletRequest request) {
        MDC.put("req.remoteHost", request.getRemoteHost());
        MDC.put("req.remoteAddr", request.getRemoteAddr());
        MDC.put("req.remotePort", String.valueOf(request.getRemotePort()));
        MDC.put("req.localAddr", request.getLocalAddr());
        MDC.put("req.localPort", String.valueOf(request.getLocalPort()));
        String requestId = request.getParameter("reqId");
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            MDC.put("req.requestURI", httpServletRequest.getRequestURI());
            StringBuffer requestURL = httpServletRequest.getRequestURL();
            if (requestURL != null) {
                MDC.put("req.requestURL", requestURL.toString());
            }

            MDC.put("req.method", httpServletRequest.getMethod());
            MDC.put("req.queryString", httpServletRequest.getQueryString());
            MDC.put("req.userAgent", httpServletRequest.getHeader("User-Agent"));
            MDC.put("req.xForwardedFor", httpServletRequest.getHeader("X-Forwarded-For"));
            if (requestId == null) {
                requestId = httpServletRequest.getHeader(AbstractMicroServiceContext.createContextKey("req.requestId"));
            }
        }

        if (requestId == null) {
            requestId = RequestId.createReqId();
        }

        MDC.put("req.requestId", requestId);
        MDC.put("req.userId", "anonymous");
        ServiceContext.getContext().setAttachment("req.requestId", requestId);
    }

    void afterFilter() {
        MDC.remove("req.remoteHost");
        MDC.remove("req.remoteAddr");
        MDC.remove("req.remotePort");
        MDC.remove("req.localAddr");
        MDC.remove("req.localPort");
        MDC.remove("req.requestURI");
        MDC.remove("req.requestURL");
        MDC.remove("req.method");
        MDC.remove("req.queryString");
        MDC.remove("req.userAgent");
        MDC.remove("req.xForwardedFor");
        MDC.remove("req.requestId");
        MDC.remove("req.userId");
        ServiceContext.getContext().removeAttachment("req.requestId");
    }

    @Override
    public void destroy() {

    }
}
