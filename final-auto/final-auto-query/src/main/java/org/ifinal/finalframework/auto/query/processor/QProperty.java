package org.ifinal.finalframework.auto.query.processor;


import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class QProperty implements Serializable {

    private static final long serialVersionUID = 737464778206273859L;

    private static final Set<String> dateTypes = new HashSet<>();

    static {
        dateTypes.add(Date.class.getCanonicalName());
        dateTypes.add(LocalDateTime.class.getCanonicalName());
    }

    private final String path;
    private final String name;
    private final String column;
    private final Element element;
    private final TypeElement type;
    private final TypeElement typeHandler;
    private final boolean idProperty;
    private final boolean insertable;
    private final boolean updatable;
    private final boolean selectable;

    private QProperty(Builder builder) {
        this.path = builder.path;
        this.name = builder.name;
        this.column = builder.column;
        this.element = builder.element;
        this.type = builder.type;
        this.typeHandler = builder.typeHandler;
        this.idProperty = builder.idProperty;
        this.insertable = builder.insertable;
        this.updatable = builder.updatable;
        this.selectable = builder.selectable;
    }

    public static Builder builder(String path, String name) {
        return new Builder(path, name);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getColumn() {
        return column;
    }

    public boolean isIdProperty() {
        return idProperty;
    }


    public Element getElement() {
        return element;
    }

    public TypeElement getType() {
        return type;
    }

    public TypeElement getTypeHandler() {
        return typeHandler;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public boolean isDate() {
        return dateTypes.contains(type.getQualifiedName().toString());
    }

    public static class Builder implements org.ifinal.finalframework.util.Builder<QProperty> {

        private final String path;
        private final String name;
        private String column;
        private Element element;
        private TypeElement type;
        private TypeElement typeHandler;
        private boolean idProperty;

        private boolean insertable;
        private boolean updatable;
        private boolean selectable;

        private Builder(String path, String name) {
            this.path = path;
            this.name = name;
        }

        public Builder column(String column) {
            this.column = column;
            return this;
        }

        public Builder element(Element element) {
            this.element = element;
            return this;
        }


        public Builder type(TypeElement type) {
            this.type = type;
            return this;
        }

        public Builder typeHandler(TypeElement typeHandler) {
            this.typeHandler = typeHandler;
            return this;
        }


        public Builder idProperty(boolean idProperty) {
            this.idProperty = idProperty;
            return this;
        }

        public Builder updatable(boolean updatable) {
            this.updatable = updatable;
            return this;
        }

        public Builder insertable(boolean insertable) {
            this.insertable = insertable;
            return this;
        }

        public Builder selectable(boolean selectable) {
            this.selectable = selectable;
            return this;
        }


        @Override
        public QProperty build() {
            return new QProperty(this);
        }
    }

}