package org.finalframework.cache;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author likly
 * @version 1.0
 * @date 2018-11-22 21:53:13
 * @since 1.0
 */
public interface CacheInvocation<O extends CacheOperation, CTX, BEFORE, AFTER> {

    BEFORE beforeInvocation(@NonNull Cache cache, @NonNull CacheOperationContext<O, CTX> context, @Nullable Object result);

    AFTER afterInvocation(@NonNull Cache cache, @NonNull CacheOperationContext<O, CTX> context, @Nullable Object result, @Nullable Throwable throwable);

}
