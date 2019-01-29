package com.ilikly.finalframework.data.annotation;

import com.ilikly.finalframework.data.annotation.enums.PrimaryKeyType;

import java.lang.annotation.*;

/**
 * mark the entity property is a primary key.
 * @author likly
 * @version 1.0
 * @date 2018-10-15 15:14
 * @since 1.0
 * @see org.springframework.data.annotation.Id
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.data.annotation.Id
public @interface PrimaryKey {
    String name() default "";

    PrimaryKeyType type() default PrimaryKeyType.AUTO_INC;
}
