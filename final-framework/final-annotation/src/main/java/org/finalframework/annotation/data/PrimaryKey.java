package org.finalframework.annotation.data;

import org.finalframework.annotation.IEntity;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Marked the element is a primary key.
 *
 * @author likly
 * @version 1.0
 * @date 2018-10-15 15:14
 * @see org.springframework.data.annotation.Id
 * @see IEntity
 * @see AutoInc
 * @since 1.0
 */
@Final
@Column
@Default
@Documented
@Order(Integer.MIN_VALUE)
@org.springframework.data.annotation.Id
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {

    @AliasFor(annotation = Column.class, value = "value")
    String value() default "";

    @AliasFor(annotation = Column.class, value = "name")
    String name() default "";
}