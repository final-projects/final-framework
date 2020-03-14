package org.finalframework.spring.web.interceptor.trace;


import lombok.Getter;
import lombok.Setter;
import org.finalframework.core.generator.TraceGenerator;
import org.finalframework.core.generator.UUIDTraceGenerator;
import org.finalframework.spring.web.interceptor.HandlerInterceptorProperties;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.Collections;
import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2020-03-14 10:13:51
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "final.spring.handler-interceptors.trace")
public class TraceHandlerInterceptorProperties implements HandlerInterceptorProperties {
    private static final String TRACE = "trace";
    /**
     * TRACE 名称，放置到{@link MDC}中的KEY的名称
     */
    private String traceName = TRACE;
    /**
     * TRACE 参数名称
     */
    private String paramName = TRACE;
    /**
     * TRACE 请求头名称
     */
    private String headerName = TRACE;

    private Class<? extends TraceGenerator> generator = UUIDTraceGenerator.class;

    /**
     * 排序
     */
    private Integer order = Ordered.LOWEST_PRECEDENCE;

    /**
     * 包含的路径规则
     */
    private List<String> pathPatterns = Collections.singletonList("/**");
    /**
     * 排除的路径规则
     */
    private List<String> excludePathPatterns;


}

