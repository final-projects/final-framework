package org.finalframework.mybatis.coding.mapper.builder;

import org.finalframework.data.coding.entity.Entity;
import org.finalframework.data.coding.entity.Property;
import org.finalframework.mybatis.coding.mapper.Utils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-11 15:21:50
 * @since 1.0
 */
public abstract class AbsXmlMapperBuilder {
    protected static final String SQL_TABLE = "sql-table";
    protected static final String SQL_ORDER = "sql-order";
    protected static final String SQL_LIMIT = "sql-limit";
    protected static final String SQL_SELECT_COLUMNS = "sql-select-columns";

    /**
     * <pre>
     *     <sql id="id">
     *         <choose>
     *             <when test="tableName != null">
     *                 ${tableName}
     *             </when>
     *             <otherwise>
     *                 tableName
     *             </otherwise>
     *         </choose>
     *     </sql>
     * </pre>
     *
     * @param document
     * @param entity
     * @return
     */
    protected Element table(@NonNull Document document, @NonNull Entity<?> entity) {
        //  <sql id="id">
        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_TABLE);
        //      <choose>
        final Element choose = document.createElement("choose");
        //              <when test="tableName != null">
        final Element whenTableNameNotNull = document.createElement("when");
        whenTableNameNotNull.setAttribute("test", "tableName != null");
        //                  ${tableName}
        whenTableNameNotNull.appendChild(textNode(document, "${tableName}"));
        //              </when>
        choose.appendChild(whenTableNameNotNull);
        //              <otherwise>
        final Element otherwise = document.createElement("otherwise");
        //                  tableName
        otherwise.appendChild(textNode(document, entity.getTable()));
        //              </otherwise>
        choose.appendChild(otherwise);
        //      </choose>
        sql.appendChild(choose);
        return sql;
    }


    protected Element order(Document document) {
        /**
         * <sql id="sql-order">
         *     <if test="query != null and query.sort != null and query.sort.size > 0">
         *         ORDER BY
         *         <foreach collection="query.sort" index="index" item="order" separator=",">
         *              #{order}
         *         </foreach>
         *     </if>
         * </sql>
         */
        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_ORDER);

        final Element ifQueryNotNullAndQuerySortNotNull = document.createElement("if");
        ifQueryNotNullAndQuerySortNotNull.setAttribute("test", "query != null and query.sort != null and query.sort.size > 0");
//        final String sort = String.format(" ORDER BY #{query.sort.sql, javaType=%s,typeHandler=%s}",
//                Sort.class.getCanonicalName(), SortTypeHandler.class.getCanonicalName());

//        ifQueryNotNullAndQuerySortNotNull.appendChild(textNode(document,"ORDER BY"));

//        Element foreach = document.createElement("foreach");
//        foreach.setAttribute("collection", "query.sort");
//        foreach.setAttribute("index", "index");
//        foreach.setAttribute("item", "order");
//        foreach.setAttribute("separator", ",");

//        foreach.appendChild(textNode(document,String.format("#{order,javaType=%s,typeHandler=%s}", Order.class.getCanonicalName(), OrderTypeHandler.class.getCanonicalName())));

//        ifQueryNotNullAndQuerySortNotNull.appendChild(foreach);


        ifQueryNotNullAndQuerySortNotNull.appendChild(textNode(document, "ORDER BY ${query.sort.sql}"));
        sql.appendChild(ifQueryNotNullAndQuerySortNotNull);

        return sql;
    }

    protected Element limit(@NonNull Document document) {
        /**
         * <sql id="sql-limit">
         *     <choose>
         *         <when test="query != null and query.offset != null and  query.limit != null">
         *             LIMIT #{query.offset},#{query.limit}
         *         </when>
         *         <when test="query != null and query.limit != null">
         *             LIMIT #{query.limit}
         *         </when>
         *     </choose>
         * </sql>
         */

        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_LIMIT);

        final Element whenOffsetLimitNotNull = whenOrOtherwise(document, "query != null and query.offset != null and query.limit != null", textNode(document, " LIMIT #{query.offset},#{query.limit}"));
        final Element whenLimitNotNull = whenOrOtherwise(document, "query != null and query.limit != null", textNode(document, " LIMIT #{query.limit}"));
        sql.appendChild(choose(document, Arrays.asList(whenOffsetLimitNotNull, whenLimitNotNull)));
        return sql;
    }

    protected Element include(@NonNull Document document, @NonNull String refid) {
        final Element include = document.createElement("include");
        include.setAttribute("refid", refid);
        return include;
    }

    /**
     * <where>
     * <choose>
     * <when test=""/>
     * <when test=""/>
     * <when test=""/>
     * </choose>
     * </where>
     *
     * @param document
     * @param when
     * @return
     */
    protected Element where(@NonNull Document document, @NonNull Element... when) {
        final Element where = document.createElement("where");
        final Element choose = document.createElement("choose");
        Arrays.stream(when).forEach(choose::appendChild);
        where.appendChild(choose);
        return where;
    }

    protected Node textNode(@NonNull Document document, @NonNull String text) {
        return document.createTextNode(text);
    }

    protected Node cdata(@NonNull Document document, @NonNull String text) {
        return document.createCDATASection(text);
    }


    protected Element choose(@NonNull Document document, @NonNull Collection<Element> whenAndOtherWises) {
        final Element choose = document.createElement("choose");
        whenAndOtherWises.forEach(choose::appendChild);
        return choose;
    }

    protected Element whenOrOtherwise(@NonNull Document document, @Nullable String test, @NonNull Node child) {
        final Element when = document.createElement(test != null ? "when" : "otherwise");
        if (test != null) {
            when.setAttribute("test", test);
        }
        when.appendChild(child);
        return when;
    }


    protected Element whenIdNotNull(@NonNull Document document, @NonNull Entity<?> entity) {
        /*
         * <when test="id != null">
         *     id = #{id}
         * </when>
         */
        final Element whenIdNotNull = document.createElement("when");
        whenIdNotNull.setAttribute("test", "id != null");
        whenIdNotNull.appendChild(textNode(document, entity.getRequiredIdProperty().getColumn() + " = #{id}"));
        return whenIdNotNull;
    }

    protected Element whenIdsNotNull(@NonNull Document document, @NonNull Entity<?> entity) {
        /*
         * <when test="ids != null">
         *      id IN
         *     <foreach collection="ids" item="id" open="(" separator="," close=")">
         *         #{id}
         *     </foreach>
         * </when>
         */

        final Element whenIdsNotNull = document.createElement("when");
        whenIdsNotNull.setAttribute("test", "ids != null");
        whenIdsNotNull.appendChild(textNode(document, entity.getRequiredIdProperty().getColumn() + " IN"));
        final Element foreach = document.createElement("foreach");
        foreach.setAttribute("collection", "ids");
        foreach.setAttribute("item", "id");
        foreach.setAttribute("open", "(");
        foreach.setAttribute("separator", ",");
        foreach.setAttribute("close", ")");
        foreach.appendChild(textNode(document, "#{id}"));
        whenIdsNotNull.appendChild(foreach);
        return whenIdsNotNull;
    }

    /**
     * <when test="query != null">
     * #{query.sql}
     * </when>
     */
    protected Element whenQueryNotNull(@NonNull Document document) {
        return whenOrOtherwise(document, "query != null and query.criteria != null", textNode(document, "${query.criteria.sql}"));
    }

    /**
     * <when test="entity != null">
     * <if test="entity.property != null">
     * AND column = #{entity.property}
     * </if>
     * </when>
     *
     * @param document
     * @param entity
     * @return
     */
    @Deprecated
    protected Element whenEntityNotNull(@NonNull Document document, Entity<Property> entity) {
        final Element whenEntityNotNull = document.createElement("when");
        whenEntityNotNull.setAttribute("test", "entity != null");
        entity.stream()
                .filter(it -> !it.isTransient())
                .forEach(property -> {
                    if (property.isReference()) {

                    } else {
                        Element ifPropertyNotNull = document.createElement("if");
                        ifPropertyNotNull.setAttribute("test", String.format("entity.%s != null", property.getName()));

                        final String column = Utils.getInstance().formatPropertyColumn(null, property);
                        final String value = Utils.getInstance().formatPropertyValues(null, property, "entity");

                        ifPropertyNotNull.appendChild(textNode(document, String.format("AND %s = %s", column, value)));
                        whenEntityNotNull.appendChild(ifPropertyNotNull);
                    }
                });
        return whenEntityNotNull;
    }
}
