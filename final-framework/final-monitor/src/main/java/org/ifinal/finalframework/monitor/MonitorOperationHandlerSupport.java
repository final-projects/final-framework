package org.ifinal.finalframework.monitor;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.ifinal.finalframework.aop.OperationHandlerSupport;
import org.ifinal.finalframework.context.expression.MethodMetadata;
import org.ifinal.finalframework.monitor.annotation.MonitorLevel;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface MonitorOperationHandlerSupport extends OperationHandlerSupport {

    @Nullable
    String generateName(@NonNull String[] name, @NonNull String delimiter, @NonNull MethodMetadata metadata,
        @NonNull EvaluationContext evaluationContext);

    @Nullable
    Object generateOperator(@NonNull String operator, @NonNull MethodMetadata metadata,
        @NonNull EvaluationContext evaluationContext);

    Object generateTarget(@NonNull String target, @NonNull MethodMetadata metadata,
        @NonNull EvaluationContext evaluationContext);

    Object generateAttribute(@NonNull String attribute, @NonNull MethodMetadata metadata,
        @NonNull EvaluationContext evaluationContext);

    MonitorLevel level(AnnotationAttributes annotation);

}
