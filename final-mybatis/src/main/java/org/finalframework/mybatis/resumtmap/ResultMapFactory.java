/*
 * Copyright (c) 2018-2020.  the original author or authors.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.finalframework.mybatis.resumtmap;


import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.finalframework.annotation.IEnum;
import org.finalframework.annotation.data.Json;
import org.finalframework.annotation.data.Reference;
import org.finalframework.annotation.data.ReferenceMode;
import org.finalframework.annotation.data.UpperCase;
import org.finalframework.data.mapping.Entity;
import org.finalframework.data.mapping.Property;
import org.finalframework.data.mapping.converter.NameConverterRegistry;
import org.finalframework.mybatis.handler.EnumTypeHandler;
import org.finalframework.mybatis.handler.JsonTypeReferenceTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0
 * @date 2020-04-12 15:44:14
 * @since 1.0
 */
public class ResultMapFactory {

    private static final Logger logger = LoggerFactory.getLogger(ResultMapFactory.class);

    private static final Map<Class<?>, ResultMap> resultMaps = new ConcurrentHashMap<>();

    public static ResultMap from(@NonNull Configuration configuration, Class<?> entity) {
        return from(configuration, Entity.from(entity));
    }

    public static ResultMap from(@NonNull Configuration configuration, Entity<?> entity) {
        return resultMaps.computeIfAbsent(entity.getType(), key -> createResultMap(configuration, entity));
    }


    private static ResultMap createResultMap(Configuration configuration, Entity<?> entity) {

        final String id = entity.getType().getCanonicalName();

        final List<ResultMapping> resultMappings = entity.stream()
                .filter(it -> !it.isTransient() && !it.isVirtual() && !it.isWriteOnly())
                .map(property -> {
                    final Class<?> type = property.getType();
                    if (property.isAssociation()) {

                        final Reference reference = property.getRequiredAnnotation(Reference.class);
                        final Entity<?> referenceEntity = Entity.from(type);
                        final List<ResultMapping> composites = Arrays.stream(reference.properties())
                                .map(referenceEntity::getPersistentProperty)
                                .map(referenceProperty -> {

                                    final String name = referenceProperty.getName();
                                    String column = formatColumn(entity, property, referenceProperty);
                                    final TypeHandler<?> typeHandler = findTypeHandler(configuration, referenceProperty);

                                    return new ResultMapping.Builder(configuration, name, column, type)
                                            .javaType(type)
                                            .flags(referenceProperty.isIdProperty() ? Collections.singletonList(ResultFlag.ID) : Collections.emptyList())
                                            .typeHandler(typeHandler)
                                            .build();
                                })
                                .collect(Collectors.toList());


                        final String name = property.getName();
                        return new ResultMapping.Builder(configuration, name)
                                .column(formatColumn(entity, null, property))
                                .javaType(type)
                                .flags(property.isIdProperty() ? Collections.singletonList(ResultFlag.ID) : Collections.emptyList())
                                .composites(composites)
//                                .nestedResultMapId(associationId)
                                // a composting result mapping is not need a typehandler, but mybatis have this a validate.
                                .typeHandler(configuration.getTypeHandlerRegistry().getUnknownTypeHandler())
                                .build();


                    } else {
                        final String name = property.getName();
                        final String column = formatColumn(entity, null, property);

                        final TypeHandler<?> typeHandler = findTypeHandler(configuration, property);

                        return new ResultMapping.Builder(configuration, name, column, type)
                                .flags(property.isIdProperty() ? Collections.singletonList(ResultFlag.ID) : Collections.emptyList())
                                .typeHandler(typeHandler)
                                .build();
                    }

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        return new ResultMap.Builder(configuration, id, entity.getType(), resultMappings)
                .build();


    }

    private static String formatColumn(Entity entity, Property property, Property referenceProperty) {
        String column = null;
        if (property == null) {
            column = referenceProperty.getColumn();
//            if (property.isKeyword()) {
//                column = String.format("`%s`", column);
//            }

        } else {
            final String referenceColumn = property.getReferenceColumn(referenceProperty);
            column = referenceProperty.isIdProperty() && property.getReferenceMode() == ReferenceMode.SIMPLE ?
                    property.getColumn() : property.getColumn() + referenceColumn.substring(0, 1).toUpperCase() + referenceColumn.substring(1);
        }
        column = NameConverterRegistry.getInstance().getColumnNameConverter().convert(column);
        if (Optional.ofNullable(property).orElse(referenceProperty).hasAnnotation(UpperCase.class) || entity.isAnnotationPresent(UpperCase.class)) {
            column = column.toUpperCase();
        }
        return column;
    }


    private static TypeHandler<?> findTypeHandler(Configuration configuration, Property property) {

        if (property.hasAnnotation(org.finalframework.annotation.data.TypeHandler.class)) {
            try {
                return property.getRequiredAnnotation(org.finalframework.annotation.data.TypeHandler.class).value().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        if (property.hasAnnotation(Json.class) || property.isCollectionLike() || property.isMap()) {
            return new JsonTypeReferenceTypeHandler<>(property.getField().getGenericType());
        }
        if (property.isEnum()) {
            final Class<? extends IEnum> type = (Class<? extends IEnum>) property.getType();
            return new EnumTypeHandler<>(type);
        }
        return configuration.getTypeHandlerRegistry().getTypeHandler(property.getType());
    }


}
