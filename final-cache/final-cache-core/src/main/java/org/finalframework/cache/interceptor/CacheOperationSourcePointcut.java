package org.finalframework.cache.interceptor;


import org.finalframework.cache.CacheOperationPointCut;
import org.finalframework.cache.CacheOperationSource;
import org.finalframework.core.Assert;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-05 13:19:55
 * @since 1.0
 */
public abstract class CacheOperationSourcePointcut extends StaticMethodMatcherPointcut implements CacheOperationPointCut, Serializable {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        final CacheOperationSource source = getCacheOperationSource();
        return source != null && Assert.nonEmpty(source.getCacheOperations(method, targetClass));
    }

    protected abstract CacheOperationSource getCacheOperationSource();
}