

package org.finalframework.data.query;

import org.finalframework.annotation.data.Table;
import org.finalframework.annotation.data.View;
import org.finalframework.data.mapping.Entity;
import org.finalframework.data.mapping.MappingUtils;
import org.finalframework.data.mapping.converter.NameConverterRegistry;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-21 10:34:30
 * @since 1.0
 */
public class AbsQEntity<ID extends Serializable, T> implements QEntity<ID, T> {

    private final List<QProperty<?>> properties = new ArrayList<>();
    private final Map<String, QProperty<?>> pathProperties = new HashMap<>();

    private final Class<T> type;
    private final String table;

    private QProperty<?> idProperty;

    public AbsQEntity(Class<T> type) {
        this(type, NameConverterRegistry.getInstance().getTableNameConverter().convert(
                type.getAnnotation(Table.class) == null || type.getAnnotation(Table.class).value().isEmpty()
                        ? type.getSimpleName()
                        : type.getAnnotation(Table.class).value()
        ));
    }

    public AbsQEntity(Class<T> type, String table) {
        this.type = type;
        this.table = table;
        this.initProperties();
    }

    protected void initProperties() {
        Entity<T> entity = Entity.from(type);

        entity.stream()
                .filter(it -> !it.isTransient())
                .forEach(property -> {

                    final View view = property.findAnnotation(View.class);
                    final List<Class<?>> views = Optional.ofNullable(view).map(value -> Arrays.asList(value.value()))
                            .orElse(null);
                    final int order = property.getOrder();
                    if (property.isReference()) {

                        final Entity<?> referenceEntity = Entity.from(property.getType());

                        AtomicInteger index = new AtomicInteger();

                        property.getReferenceProperties()
                                .stream()
                                .map(referenceEntity::getRequiredPersistentProperty)
                                .forEach(referenceProperty -> {

                                    addProperty(
                                            QProperty.builder(this, referenceProperty)
                                                    .order(order + index.getAndIncrement())
                                                    .path(property.getName() + "." + referenceProperty.getName())
                                                    .name(MappingUtils.formatPropertyName(property, referenceProperty))
                                                    .column(MappingUtils.formatColumn(entity, property, referenceProperty))
                                                    .views(views)
                                                    .readable(true)
                                                    .writeable(property.isWriteable())
                                                    .modifiable(property.isModifiable())
                                                    .typeHandler(referenceProperty.getTypeHandler())
                                                    .build()
                                    );
                                });


                    } else {

                        addProperty(
                                QProperty.builder(this, property)
                                        .order(order)
                                        .path(property.getName())
                                        .name(property.getName())
                                        .column(MappingUtils.formatColumn(entity, property, null))
                                        .idProperty(property.isIdProperty())
                                        .readable(!property.isTransient() && !property.isVirtual() && !property.isWriteOnly())
                                        .writeable(property.isWriteable())
                                        .modifiable(property.isModifiable())
                                        .typeHandler(property.getTypeHandler())
                                        .views(views)
                                        .build()
                        );
                    }
                });
        this.properties.sort(Comparator.comparing(QProperty::getOrder));
    }

    private void addProperty(QProperty<?> property) {
        this.properties.add(property);
        this.pathProperties.put(property.getPath(), property);
        if (property.isIdProperty()) {
            this.idProperty = property;
        }
    }

    @Override
    public String getTable() {
        return this.table;
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    @Override
    public QProperty<ID> getIdProperty() {
        return (QProperty<ID>) this.idProperty;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> QProperty<E> getProperty(String path) {
        return (QProperty<E>) pathProperties.get(path);
    }

    @Override
    public Iterator<QProperty<?>> iterator() {
        return properties.iterator();
    }

    @Override
    public Stream<QProperty<?>> stream() {
        return properties.stream();
    }
}
