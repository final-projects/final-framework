package org.ifinal.finalframework.annotation.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ResourceValue.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceValue {

    String value();

    boolean required() default false;

}
