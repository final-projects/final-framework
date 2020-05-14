package org.finalframework.data.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.finalframework.data.annotation.Default;
import org.finalframework.data.annotation.Final;
import org.finalframework.data.annotation.ReadOnly;
import org.finalframework.data.annotation.Sharding;
import org.finalframework.data.annotation.Transient;
import org.finalframework.data.annotation.Virtual;
import org.finalframework.data.annotation.WriteOnly;
import org.finalframework.data.annotation.enums.ReferenceMode;
import org.finalframework.data.serializer.PropertyJsonSerializer;
import org.springframework.data.mapping.PersistentProperty;


/**
 * @author likly
 * @version 1.0
 * @date 2018-10-17 10:52
 * @since 1.0
 */
@JsonSerialize(using = PropertyJsonSerializer.class)
public interface Property extends PersistentProperty<Property> {


    default boolean isEnum() {
        return getType().isEnum();
    }

    String getColumn();

    /**
     * @return true if don't have {@link Transient} annotation and have {@link Default} annotation.
     * @see Default
     */
    boolean isDefault();

    /**
     * @return {@code true} if the {@link Property} annotated by {@link Final} annotation.
     * @see Final
     */
    boolean isFinal();

    boolean isReference();

    /**
     * @return {@code true} if the {@link Property} annotated by {@link Virtual} annotation.
     * @see Virtual
     */
    boolean isVirtual();

    /**
     * @return {@code true} if this {@link Property} is annotated by {@link Sharding} annotation.
     * @see Sharding
     */
    boolean isSharding();

    /**
     * @return {@code true} if this {@link Property} is annotated by {@link ReadOnly} annotation.
     * @see ReadOnly
     */
    boolean isReadOnly();


    /**
     * @return {@code true} if this {@link Property} is annotated by {@link WriteOnly} annotation.
     * @see WriteOnly
     */
    boolean isWriteOnly();

    boolean isKeyword();


    default boolean isWriteable() {
        return !isTransient() && !isVirtual() && !isReadOnly() && !isDefault();
    }

    default boolean isModifiable() {
        return !isTransient() && !isVirtual() && !isReadOnly() && !isFinal();
    }


    ReferenceMode getReferenceMode();

    Set<String> getReferenceProperties();

    String getReferenceColumn(Property property);

    Class<?> getJavaType();

    default Object get(@NotNull Object target) {
        try {
            return getRequiredGetter().invoke(target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    default void set(@NotNull Object target, Object value) {
        try {
            getRequiredSetter().invoke(target, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    default <A extends Annotation> boolean hasAnnotation(Class<A> ann) {
        return findAnnotation(ann) != null;
    }
}
