

package org.finalframework.annotation.data;

import org.springframework.lang.NonNull;

/**
 * mark the entity table name if the entity name if not match the database table name.
 *
 * @author likly
 * @version 1.0
 * @date 2018-10-15 15:14
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    @NonNull
    String value();
}
