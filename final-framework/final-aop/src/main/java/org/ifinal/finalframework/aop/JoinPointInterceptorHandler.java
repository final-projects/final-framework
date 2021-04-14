package org.ifinal.finalframework.aop;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.ifinal.finalframework.core.annotation.aop.JoinPoint;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface JoinPointInterceptorHandler<E, A> extends InterceptorHandler<E, A> {

    @Nullable
    JoinPoint point(A annotation);

    @Override
    default Object before(@NonNull E executor, @NonNull InvocationContext context, @NonNull A annotation) {

        if (JoinPoint.BEFORE == point(annotation)) {
            handle(executor, context, annotation, null, null);
        }
        return null;
    }

    @Override
    default void afterReturning(@NonNull E executor, @NonNull InvocationContext context, @NonNull A annotation,
        Object result) {

        if (JoinPoint.AFTER_RETURNING == point(annotation)) {
            handle(executor, context, annotation, result, null);
        }
    }

    @Override
    default void afterThrowing(@NonNull E executor, @NonNull InvocationContext context, @NonNull A annotation,
        @NonNull Throwable throwable) {

        if (JoinPoint.AFTER_THROWING == point(annotation)) {
            handle(executor, context, annotation, null, throwable);
        }
    }

    @Override
    default void after(@NonNull E executor, @NonNull InvocationContext context, @NonNull A annotation, Object result,
        Throwable throwable) {

        if (JoinPoint.AFTER == point(annotation)) {
            handle(executor, context, annotation, result, throwable);
        }
    }

}
