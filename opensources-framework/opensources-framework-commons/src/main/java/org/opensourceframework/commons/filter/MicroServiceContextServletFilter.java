package org.opensourceframework.commons.filter;

import org.opensourceframework.base.microservice.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 */
public class MicroServiceContextServletFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(MicroServiceContextServletFilter.class);

    public MicroServiceContextServletFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.beforeFilter(request);

        try {
            chain.doFilter(request, response);
        } catch (ServletException e) {
            logger.error(e.getMessage(), e);
            ((HttpServletResponse)response).setStatus(500);
            throw e;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            ((HttpServletResponse)response).setStatus(500);
            throw e;
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            ((HttpServletResponse)response).setStatus(500);
            throw new ServletException(throwable.getMessage(), throwable);
        }
    }

    void beforeFilter(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            setAttachmentFromHeader((HttpServletRequest)request);
        }

        setAttachmentFromParameter(request);
    }

    public static void setAttachmentFromHeader(HttpServletRequest httpServletRequest) {
        Enumeration enumHeaders = httpServletRequest.getHeaderNames();

        while(enumHeaders.hasMoreElements()) {
            String header = (String)enumHeaders.nextElement();
            if (header.startsWith("x-opensources-context-")) {
                String key = header.substring("x-opensources-context-".length());
                String attachement = httpServletRequest.getHeader(header);
                ServiceContext.getContext().setAttachment(key, attachement);
            }
        }

    }

    public static void setAttachmentFromParameter(ServletRequest request) {
        String updateParam;
        if (ServiceContext.getContext().getAttachment("req.requestId") == null) {
            updateParam = request.getParameter("reqId");
            if (updateParam != null) {
                ServiceContext.getContext().setAttachment("req.requestId", updateParam);
            }
        }

        if (ServiceContext.getContext().getAttachment("req.appId") == null) {
            updateParam = request.getParameter("appId");
            if (updateParam != null) {
                ServiceContext.getContext().setAttachment("req.appId", updateParam);
            }
        }

        if (ServiceContext.getContext().getAttachment("req.userCode") == null) {
            updateParam = request.getParameter("userCode");
            if (updateParam != null) {
                ServiceContext.getContext().setAttachment("req.userCode", updateParam);
            }
        }

        if (ServiceContext.getContext().getAttachment("req.userId") == null) {
            updateParam = request.getParameter("userId");
            if (updateParam != null) {
                ServiceContext.getContext().setAttachment("req.userId", updateParam);
            }
        }

    }

    @Override
    public void destroy() {
    }
}
