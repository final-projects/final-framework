package com.ilikly.finalframework.cache;

import java.lang.annotation.Annotation;

/**
 * @author likly
 * @version 1.0
 * @date 2018-11-22 17:14:24
 * @since 1.0
 */
public interface CacheAnnotationParser<A extends Annotation, O extends CacheOperation<A>> {
    O parseCacheAnnotation(A ann);
}
