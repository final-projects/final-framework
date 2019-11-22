package org.finalframework.coding.mapper.builder;

import org.finalframework.coding.entity.Entity;
import org.finalframework.coding.entity.Property;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.lang.model.element.ExecutableElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-12 11:01:11
 * @since 1.0
 */
public class SelectMethodXmlMapperBuilder extends AbsMethodXmlMapperBuilder {

    private static final String METHOD_SELECT = "select";
    private static final String METHOD_SELECT_ONE = "selectOne";
    private static final Set<String> methods = new HashSet<>(Arrays.asList(METHOD_SELECT, METHOD_SELECT_ONE));

    @Override
    public boolean supports(ExecutableElement method) {
        return !method.isDefault() && methods.contains(method.getSimpleName().toString());
    }

    @Override
    public Element buildMethodElement(ExecutableElement method, Document document, Entity<Property> entity) {
        final Element sql = document.createElement("select");
        String methodName = method.getSimpleName().toString();
        sql.setAttribute("resultMap", entity.getSimpleName() + "Map");
        sql.setAttribute("id", methodName);
        Element select = document.createElement("trim");
        select.setAttribute("prefix", "SELECT");
        select.appendChild(include(document, SQL_SELECT_COLUMNS));
        sql.appendChild(select);

        Element from = document.createElement("trim");
        from.setAttribute("prefix", "FROM");
        from.appendChild(include(document, SQL_TABLE));
        sql.appendChild(from);
        sql.appendChild(where(document,
                METHOD_SELECT_ONE.equals(methodName) ? whenIdNotNull(document, entity) : whenIdsNotNull(document, entity),
                whenQueryNotNull(document)
        ));
        return sql;
    }

}
