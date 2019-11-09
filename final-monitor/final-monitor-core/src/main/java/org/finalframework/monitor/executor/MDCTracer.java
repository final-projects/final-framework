package org.finalframework.monitor.executor;

import org.finalframework.core.Assert;
import org.finalframework.core.generator.TraceGenerator;
import org.finalframework.core.generator.UUIDTraceGenerator;
import org.finalframework.monitor.context.TraceContext;
import org.finalframework.spring.annotation.factory.SpringComponent;
import org.finalframework.spring.aop.annotation.OperationExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author likly
 * @version 1.0
 * @date 2019-07-09 16:58
 * @since 1.0
 */
@SpringComponent
@OperationExecutor(Tracer.class)
public class MDCTracer implements Tracer {
    private static final Logger logger = LoggerFactory.getLogger(MDCTracer.class);
    private static final String mdcTracer = "mdcTracer";

    private TraceGenerator traceGenerator = UUIDTraceGenerator.instance;

    @Override
    public void start(TraceContext context) {
        String trace = context.getTrace();
        String value = MDC.get(trace);
        if (Assert.isBlank(value)) {
            value = traceGenerator.generate(null);
            MDC.put(trace, value);
            MDC.put(mdcTracer, Boolean.TRUE.toString());
            logger.info("put trace: trace={},value={}", trace, value);
        }
    }

    @Override
    public void stop(TraceContext context) {
        if (Boolean.TRUE.equals(Boolean.valueOf(MDC.get(mdcTracer)))) {
            String trace = context.getTrace();
            String value = MDC.get(trace);
            logger.info("remove trace: trace={},value={}", trace, value);
            MDC.remove(trace);
        }
    }
}