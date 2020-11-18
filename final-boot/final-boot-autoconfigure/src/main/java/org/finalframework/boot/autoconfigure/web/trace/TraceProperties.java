package org.finalframework.boot.autoconfigure.web.trace;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/16 18:04:01
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = TraceProperties.PREFIX)
public class TraceProperties {
    static final String PREFIX = "final.trace";

    private static final String TRACE = "trace";

    private String name = TRACE;
    private String param = TRACE;
    private String header = TRACE;

}