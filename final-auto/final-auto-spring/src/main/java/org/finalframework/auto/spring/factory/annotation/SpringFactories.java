package org.finalframework.auto.spring.factory.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author likly
 * @version 1.0
 * @date 2020/10/26 15:15:53
 * @see SpringFactory
 * @since 1.0
 */
@Target({PACKAGE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface SpringFactories {
    SpringFactory[] value();
}