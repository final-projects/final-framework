package org.finalframework.cache.invocation;


import org.finalframework.cache.operation.CacheableOperation;
import org.finalframework.spring.aop.interceptor.BaseInvocation;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-11 10:25:28
 * @since 1.0
 */
@SuppressWarnings("all")
public final class CacheableInvocation extends BaseInvocation<CacheableOperation> {
    public CacheableInvocation() {
        super(CacheableOperation.class);
    }
}
