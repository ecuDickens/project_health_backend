package com.health.servlet;


import com.health.DomainConstants;
import com.google.inject.Singleton;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Adds response headers useful in debugging.
 */
@Singleton
public class DebuggingResponseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            httpResponse.setHeader("X-SN", DomainConstants.SYSTEM_NAME);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
