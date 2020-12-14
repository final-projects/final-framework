package org.ifinal.finalframework.aop;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInvocation;
import org.ifinal.finalframework.context.expression.MethodMetadata;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.expression.AnnotatedElementKey;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class DefaultAnnotationMethodInterceptor<A> implements AnnotationMethodInterceptor<A> {

    private final Map<AnnotatedElementKey, MethodMetadata> metadataCache = new ConcurrentHashMap<>(1024);

    private final AnnotationSource<A> source;

    private final MethodInvocationDispatcher<A> dispatcher;

    public DefaultAnnotationMethodInterceptor(final AnnotationSource<A> source,
        final MethodInvocationDispatcher<A> dispatcher) {

        this.source = source;
        this.dispatcher = dispatcher;
    }

    @Override
    public A findAnnotations(final Method method, final Class<?> clazz) {

        return source.getAnnotations(method, clazz);
    }

    @Override
    public Object invoke(final MethodInvocation invocation, final A annotations) throws Throwable {

        final Class<?> targetClass = getTargetClass(invocation.getThis());
        final MethodMetadata metadata = getOperationMetadata(invocation.getMethod(), targetClass);

        final InvocationContext context = new DefaultInvocationContext(metadata, invocation.getThis(),
            invocation.getArguments());

        final Object operationValue = dispatcher.before(context, annotations);
        if (Objects.nonNull(operationValue)) {
            return operationValue;
        }

        Object returnValue = null;
        Throwable throwable = null;

        try {
            returnValue = invocation.proceed();
        } catch (Throwable e) {
            throwable = e;
        }

        if (throwable == null) {
            dispatcher.afterReturning(context, annotations, returnValue);
        } else {
            dispatcher.afterThrowing(context, annotations, throwable);
        }

        dispatcher.after(context, annotations, returnValue, throwable);

        if (throwable != null) {
            throw throwable;
        }

        return returnValue;
    }

    private MethodMetadata getOperationMetadata(final Method method, final Class<?> targetClass) {

        final AnnotatedElementKey cacheKey = new AnnotatedElementKey(method, targetClass);
        return this.metadataCache.computeIfAbsent(cacheKey, key -> new MethodMetadata(method, targetClass));
    }

    private Class<?> getTargetClass(final Object target) {

        return AopProxyUtils.ultimateTargetClass(target);
    }

}
