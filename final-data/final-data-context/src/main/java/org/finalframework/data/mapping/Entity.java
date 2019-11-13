package org.finalframework.data.mapping;

import org.finalframework.core.Assert;
import org.finalframework.core.Streamable;
import org.finalframework.data.annotation.NonCompare;
import org.springframework.data.mapping.PersistentEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0PP
 * @date 2018-10-17 10:52
 * @since 1.0
 */
public interface Entity<T> extends PersistentEntity<T, Property>, Streamable<Property>, Iterable<Property> {

    static <T> Entity<T> from(Class<T> entityClass) {
        Assert.isNull(entityClass, "entityClass must not be null!");
        return EntityCache.getInstance().get(entityClass);
    }

    @SuppressWarnings("unchecked")
    static <T> List<CompareProperty> compare(T before, T after) {
        Entity<T> entity = (Entity<T>) from(before.getClass());
        return entity.stream()
                .filter(it -> !it.isTransient() && !it.hasAnnotation(NonCompare.class))
                .map(property -> CompareProperty.builder()
                        .property(property)
                        .value(property.get(before), property.get(after))
                        .build())
                .collect(Collectors.toList());
    }

    default T getInstance() {
        try {
            return getType().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(String.format("the entity of %s must have no args constructor!", getType().getName()));
        }
    }

}