package org.opensourceframework.common.filter;

import org.opensourceframework.common.http.HttpRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 包装Resquest fileter
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * 
 */
public class RequestWrapperFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			String contentType = servletRequest.getContentType();
			boolean isDoFilter = true;
			if(StringUtils.isNotBlank(contentType)){
				if(contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)){
					isDoFilter = false;
				}
			}

			if(isDoFilter){
				HttpRequestWrapper requestWrapper = new HttpRequestWrapper((HttpServletRequest) servletRequest);
				filterChain.doFilter(requestWrapper, servletResponse);
			}else {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		} catch (IOException e) {
			e.printStackTrace();
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
}
