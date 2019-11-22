package org.finalframework.coding.mapper.builder;

import org.finalframework.coding.entity.Entity;
import org.finalframework.coding.entity.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.lang.model.element.ExecutableElement;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-12 11:15:36
 * @since 1.0
 */
public class SelectIdsMethodXmlMapperBuilder extends AbsMethodXmlMapperBuilder {
    @Override
    public boolean supports(ExecutableElement method) {
        return !method.isDefault() && "selectIds".equals(method.getSimpleName().toString());
    }

    @Override
    public Element buildMethodElement(ExecutableElement method, Document document, Entity<Property> entity) {
        final Element select = document.createElement("select");
        select.setAttribute("id", method.getSimpleName().toString());
        final Property idProperty = entity.getRequiredIdProperty();
        select.setAttribute("resultType", idProperty.getMetaTypeElement().getQualifiedName().toString());

        Element trim = document.createElement("trim");
        trim.setAttribute("prefix", String.format("SELECT %s FROM", idProperty.getColumn()));

        trim.appendChild(include(document, SQL_TABLE));
        trim.appendChild(include(document, SQL_QUERY));
        select.appendChild(trim);
        return select;
    }
}
