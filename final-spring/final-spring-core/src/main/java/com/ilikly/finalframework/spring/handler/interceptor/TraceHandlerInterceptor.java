package com.ilikly.finalframework.spring.handler.interceptor;

import com.ilikly.finalframework.spring.handler.interceptor.annotation.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author likly
 * @version 1.0
 * @date 2018-09-28 15:18
 * @since 1.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@HandlerInterceptor
public class TraceHandlerInterceptor implements AsyncHandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TraceHandlerInterceptor.class);
    private static final String TRACE = "trace";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String trace = request.getHeader(TRACE);
        if (trace == null) {
            trace = UUID.randomUUID().toString();
        }

        MDC.put(TRACE, trace);
        logger.info("put trace to MDC context: {}", trace);
        return true;
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.remove(TRACE);
        logger.info("remove trace from MDC context");
    }
}
