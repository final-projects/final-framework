package org.ifinal.finalframework.mybatis.sql.provider;


import org.apache.ibatis.builder.annotation.ProviderContext;
import org.ifinal.finalframework.annotation.IEntity;
import org.ifinal.finalframework.annotation.IQuery;
import org.ifinal.finalframework.data.query.QEntity;
import org.ifinal.finalframework.data.query.Query;
import org.ifinal.finalframework.data.query.sql.AnnotationQueryProvider;
import org.ifinal.finalframework.mybatis.sql.AbsMapperSqlProvider;

import java.util.Collection;
import java.util.Map;

/**
 * @author likly
 * @version 1.0.0
 * @see org.ifinal.finalframework.mybatis.mapper.AbsMapper#delete(String, Collection, IQuery)
 * @since 1.0.0
 */
public class DeleteSqlProvider implements AbsMapperSqlProvider, ScriptSqlProvider {

    @SuppressWarnings("unused")
    public String delete(ProviderContext context, Map<String, Object> parameters) {
        return provide(context, parameters);
    }

    @Override
    public void doProvide(StringBuilder sql, ProviderContext context, Map<String, Object> parameters) {

        Object ids = parameters.get("ids");
        Object query = parameters.get("query");

        final Class<?> entity = getEntityClass(context.getMapperType());
        final QEntity<?, ?> properties = QEntity.from(entity);

        sql.append("<trim prefix=\"DELETE FROM\">").append("${table}").append("</trim>");

        if (ids != null) {
            sql.append(whereIdsNotNull());
        } else if (query instanceof Query) {
            ((Query) query).apply(sql, "query");
        } else if (query != null) {
            sql.append(AnnotationQueryProvider.INSTANCE.provide("query", (Class<? extends IEntity<?>>) entity, query.getClass()));
        }


    }
}
