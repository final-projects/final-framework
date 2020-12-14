package org.ifinal.finalframework.aop;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.ifinal.finalframework.util.Asserts;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface AnnotationMethodInterceptor<R> extends MethodInterceptor {

    R findAnnotations(Method method, Class<?> clazz);

    @Override
    default Object invoke(MethodInvocation invocation) throws Throwable {

        final R annotations = findAnnotations(invocation.getMethod(), invocation.getThis().getClass());

        if (Asserts.isEmpty(annotations)) {
            return invocation.proceed();
        }

        return invoke(invocation, annotations);
    }

    Object invoke(MethodInvocation invocation, R annotations) throws Throwable;

}
