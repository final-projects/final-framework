package org.ifinal.finalframework.data.query.criterion;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.ifinal.finalframework.annotation.query.CriterionSqlProvider;
import org.ifinal.finalframework.annotation.query.Metadata;
import org.ifinal.finalframework.data.util.Velocities;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.NonNull;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class VelocityCriterionSqlProvider implements CriterionSqlProvider {

    @Override
    public String provide(final @NonNull AnnotationAttributes annotationAttributes, final @NonNull Metadata metadata) {

        final String value = Arrays.stream(annotationAttributes.getStringArray("value")).map(String::trim)
            .collect(Collectors.joining());
        return Velocities.getValue(value, metadata);
    }

}

