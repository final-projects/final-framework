package org.finalframework.coding.query;


import org.finalframework.coding.entity.Entity;
import org.finalframework.coding.entity.EntityFactory;
import org.finalframework.coding.entity.Property;
import org.finalframework.coding.mapper.TypeHandlers;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-26 14:07:01
 * @since 1.0
 */
public class QEntityFactory {

    public static QEntity create(ProcessingEnvironment processingEnv, String packageName, Entity entity, TypeHandlers typeHandlers) {
        final String entityName = entity.getSimpleName();
        final QEntity.Builder builder = new QEntity.Builder(entity);
        builder.packageName(packageName)
                .name("Q" + entityName);
        entity.stream()
                .filter(property -> !property.isTransient())
                .forEach(property -> {
                    if (property.isReference()) {
                        TypeElement multiElement = processingEnv.getElementUtils().getTypeElement(property.getType());
                        Entity multiEntity = EntityFactory.create(processingEnv, multiElement);
                        @SuppressWarnings("unchecked") final List<String> properties = property.referenceProperties();
                        properties.stream()
                                .map(multiEntity::getRequiredProperty)
                                .map(multiProperty -> buildProperty(property, multiProperty, typeHandlers))
                                .forEach(builder::addProperty);
                    } else {
                        builder.addProperty(buildProperty(null, property, typeHandlers));
                    }

                });

        return builder.build();
    }


    private static QProperty buildProperty(@Nullable Property referenceProperty, @NonNull Property property, TypeHandlers typeHandlers) {
        if (referenceProperty == null) {
            return QProperty.builder(property.getName(), Utils.formatPropertyName(null, property))
                    .type(property.getMetaTypeElement())
                    .typeHandler(typeHandlers.getTypeHandler(property))
                    .idProperty(property.isIdProperty())
                    .column(Utils.formatPropertyColumn(null, property))
                    .idProperty(property.isIdProperty())
                    .persistentType(property.getPersistentType())
                    .insertable(property.insertable())
                    .updatable(property.updatable())
                    .selectable(property.selectable())
                    .build();
        } else {
            final String path = referenceProperty.getName() + "." + property.getName();
            final String name = Utils.formatPropertyName(referenceProperty, property);
            return QProperty.builder(path, name)
                    .column(Utils.formatPropertyColumn(referenceProperty, property))
                    .type(property.getMetaTypeElement())
                    .typeHandler(typeHandlers.getTypeHandler(property))
                    .idProperty(false)
                    .persistentType(property.getPersistentType())
                    .insertable(referenceProperty.insertable())
                    .updatable(referenceProperty.updatable())
                    .selectable(referenceProperty.selectable())
                    .build();
        }

    }
}

