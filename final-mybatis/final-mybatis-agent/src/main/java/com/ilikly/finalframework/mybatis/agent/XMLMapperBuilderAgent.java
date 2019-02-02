package com.ilikly.finalframework.mybatis.agent;

import com.ilikly.finalframework.data.annotation.MultiColumn;
import com.ilikly.finalframework.data.annotation.enums.PrimaryKeyType;
import com.ilikly.finalframework.data.mapping.Dialect;
import com.ilikly.finalframework.data.mapping.DialectFactory;
import com.ilikly.finalframework.data.mapping.Entity;
import com.ilikly.finalframework.data.mapping.Property;
import com.ilikly.finalframework.data.mapping.converter.NameConverterRegistry;
import com.ilikly.finalframework.data.mapping.generator.ColumnGenerator;
import com.ilikly.finalframework.data.mapping.generator.ColumnGeneratorRegistry;
import com.ilikly.finalframework.data.query.enums.UpdateOperation;
import com.ilikly.finalframework.data.repository.Repository;
import com.ilikly.finalframework.mybatis.EntityHolderCache;
import com.ilikly.finalframework.mybatis.Utils;
import com.ilikly.finalframework.mybatis.handler.TypeHandlerRegistry;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0
 * @date 2018-12-21 23:40:31
 * @see org.apache.ibatis.builder.xml.XMLMapperBuilder
 * @since 1.0
 */
@SuppressWarnings("unused")
public class XMLMapperBuilderAgent {
    private static final Logger logger = LoggerFactory.getLogger(XMLMapperBuilderAgent.class);
    private static final Set<Class> numberClasses = new HashSet<>(Arrays.asList(
            byte.class, short.class,
            int.class, long.class,
            float.class, double.class
    ));
    private static final Set<String> SQL_KEYS = new HashSet<>(Arrays.asList(
            "key".toUpperCase()
    ));
    private static final boolean logMapper = true;
    private static final TypeHandlerRegistry typeHandlerRegistry = TypeHandlerRegistry.getInstance();
    private static final ColumnGeneratorRegistry columnGeneratorRegistry = ColumnGeneratorRegistry.getInstance();
    private static final EntityHolderCache cache = new EntityHolderCache();
    private static final String SQL_TABLE = "sql-table";
    private static final String SQL_UPDATE = "sql-update";
    private static final String SQL_INSERT_COLUMNS = "sql-insert-columns";
    private static final String SQL_INSERT_VALUES = "sql-insert-values";
    private static final String SQL_SELECT = "sql-select";

    private final XNode context;
    private final Document document;
    private final Entity<Property> entity;
    private final Class type;
    private final Dialect dialect;

    public XMLMapperBuilderAgent(Dialect dialect, XNode context, Entity<Property> entity) {
        this.dialect = dialect;
        this.context = context;
        this.document = context.getNode().getOwnerDocument();
        this.entity = entity;
        this.type = entity.getType();
        init();
    }

    @SuppressWarnings("unused")
    public static void configurationDefaultElement(XNode context) {
        String namespace = context.getStringAttribute("namespace");
        if (namespace == null || namespace.equals("")) {
            throw new BuilderException("Mapper's namespace cannot be empty");
        }
        try {
            Class mapperClass = Class.forName(namespace);
            if (Repository.class.isAssignableFrom(mapperClass)) {
                Dialect dialect = DialectFactory.getDialect(mapperClass);
                Entity entity = cache.get(mapperClass);
                final XMLMapperBuilderAgent agent = new XMLMapperBuilderAgent(dialect, context, entity);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Mapper cannot be found:mapper=" + namespace);
        }
    }

    private void init() {
        appendResultMapElement();
        //mapper method
        appendInsertElement();
        appendUpdateElement();
        appendDeleteElement();
        appendSelectElement();
        appendSelectOneElement();
        appendSelectCountElement();
        //sql fragment
        //sql-table
        appendSqlTableFragment();
        appendInsertColumnsSqlFragment();
        appendInsertValuesSqlFragment();
        appendUpdateSqlFragment();
        appendSqlSelectFragment();

        if (logMapper) {
            String result = null;

            if (document != null) {
                StringWriter strWtr = new StringWriter();
                StreamResult strResult = new StreamResult(strWtr);
                TransformerFactory tfac = TransformerFactory.newInstance();
                try {
                    javax.xml.transform.Transformer t = tfac.newTransformer();
                    t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    t.setOutputProperty(OutputKeys.INDENT, "yes");
                    t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
                    // text
                    t.setOutputProperty(
                            "{http://xml.apache.org/xslt}indent-amount", "4");
                    document.setXmlStandalone(true);
                    t.transform(new DOMSource(document.getDocumentElement()),
                            strResult);
                } catch (Exception e) {
                    System.err.println("XML.toString(Document): " + e);
                }
                result = strResult.getWriter().toString();
                try {
                    strWtr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logger.info(result);
        }

    }

    /*
     * <resultMap id="****Map" type="">
     *     <id property="" column="" javaType="" jdbcType="" typeHandler=""/>
     *     <result property="" column="" javaType="" jdbcType="" typeHandler=""/>
     *     <result property="" column="" javaType="" jdbcType="" typeHandler=""/>
     *     <association property="" javaType="">
     *          <id property="" column="" javaType="" jdbcType="" typeHandler=""/>
     *          <result property="" column="" javaType="" jdbcType="" typeHandler=""/>
     *     </association>
     * </resultMap>
     */
    private void appendResultMapElement() {
        final Element resultMap = document.createElement("resultMap");
        resultMap.setAttribute("id", type.getSimpleName() + "Map");
        resultMap.setAttribute("type", type.getCanonicalName());

        entity.stream().filter(it -> !it.isTransient())
                .forEach(property -> {
                    if (property.hasAnnotation(MultiColumn.class)) {
                        resultMap.appendChild(buildAssociationElement(property));
                    } else {
                        resultMap.appendChild(buildResultElement(property, null));
                    }
                });
        context.getNode().appendChild(resultMap);
    }

    //    ******************************************************************************************************************

    @SuppressWarnings("all")
    private Element buildAssociationElement(Property property) {
        final Class javaType = Utils.getPropertyJavaType(property);
        final Element association = document.createElement("association");
        association.setAttribute("property", property.getName());
        association.setAttribute("javaType", javaType.getCanonicalName());
        MultiColumn multiColumn = property.findAnnotation(MultiColumn.class);
        Entity<Property> multiEntity = Entity.from(javaType);
//        Arrays.stream(multiColumn.properties())
//                .map(multiEntity::getRequiredPersistentProperty)
        multiEntity.stream().filter(it -> !it.isTransient() && !it.hasAnnotation(MultiColumn.class))
                .map(it -> buildResultElement(it, property.getColumn()))
                .forEach(association::appendChild);
        return association;
    }

    @SuppressWarnings("all")
    private Element buildResultElement(Property property, String prefix) {
        final Class javaType = Utils.getPropertyJavaType(property);
        final Class collectionType = Utils.getPropertyCollectionType(property);
        TypeHandler typeHandler = Utils.getPropertyTypeHandler(dialect, property);
        final String name = property.isIdProperty() ? "id" : "result";
        final Element result = document.createElement(name);
        final String column = prefix == null ? property.getColumn()
                : property.isIdProperty() ? prefix : prefix + property.getColumn().substring(0, 1).toUpperCase() + property.getColumn().substring(1);
        result.setAttribute("property", property.getName());
        result.setAttribute("column", NameConverterRegistry.getInstance().getColumnNameConverter().map(column));
        if (javaType != null) {
            result.setAttribute("javaType", javaType.getCanonicalName());
        }
        if (typeHandler != null) {
            result.setAttribute("typeHandler", typeHandler.getClass().getCanonicalName());
        }
        return result;
    }

    private void appendSqlTableFragment() {
        /*
         *     <sql id="sql-table">
         *         <choose>
         *             <when test="tableName != null">
         *                 ${tableName}
         *             </when>
         *             <otherwise>
         *                 tableName
         *             </otherwise>
         *         </choose>
         *     </sql>
         */
        //<sql id="sql-table">
        final Element sqlTableFragment = document.createElement("sql");
        sqlTableFragment.setAttribute("id", SQL_TABLE);
        //      <choose>
        final Element choose = document.createElement("choose");
        //              <when test="tableName != null">
        final Element whenTableNameNotNull = document.createElement("when");
        whenTableNameNotNull.setAttribute("test", "tableName != null");
        //                  ${tableName}
        whenTableNameNotNull.appendChild(textNode("${tableName}"));
        //              </when>
        choose.appendChild(whenTableNameNotNull);
        //              <otherwise>
        final Element otherwise = document.createElement("otherwise");
        //                  tableName
        otherwise.appendChild(textNode(entity.getTable()));
        //              </otherwise>
        choose.appendChild(otherwise);
        //      </choose>
        sqlTableFragment.appendChild(choose);
        //</sql>
        context.getNode().appendChild(sqlTableFragment);
    }

    @SuppressWarnings("all")
    private void appendInsertColumnsSqlFragment() {
        /*
         * <sql id="insert-columns">
         *      (column1,column2,multiColumn1,multiColumn2)
         * </sql>
         */
        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_INSERT_COLUMNS);
        // (column1,column2,multiColumn1,multiColumn2)
        final List<String> columns = new ArrayList<>();
        entity.stream().filter(it -> !it.isTransient() && it.insertable())
                .forEach(property -> {
                    if (property.hasAnnotation(MultiColumn.class)) {
                        final Class multiType = Utils.getPropertyJavaType(property);
                        final Entity<Property> multiEntity = Entity.from(multiType);
                        Arrays.stream(property.findAnnotation(MultiColumn.class).properties())
                                .map(multiEntity::getRequiredPersistentProperty)
                                .map(multiProperty -> {
                                    final String table = property.getTable();
//                                    final String multiColumn = multiProperty.isIdProperty() ? property.getColumn() :
//                                            property.getColumn() + multiProperty.getColumn().substring(0, 1).toUpperCase() + multiProperty.getColumn().substring(1);
//                                    final String column = NameConverterRegistry.getInstance().getColumnNameConverter().map(multiColumn);
                                    final ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, multiProperty);
                                    return columnGenerator.generateWriteColumn(table, property.getColumn(), multiProperty);
                                })
                                .forEach(columns::add);
                    } else {
                        ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, property);
                        columns.add(columnGenerator.generateWriteColumn(property.getTable(), null, property));
                    }
                });
        sql.appendChild(textNode(columns.stream().collect(Collectors.joining(",", "(", ")"))));
        context.getNode().appendChild(sql);
    }

    private void appendInsertValuesSqlFragment() {
        /**
         * <sql id="sql-insert-values">
         *      <foreach collection="list" index="index" item="entity" separator=",">
         *          (
         *              #{list[${index}].property,javaType=%s,typeHandler=%s},
         *              ...
         *              <choose>
         *                  <when test="list[index].multi != null">
         *                      #{list[${index}].multi.property,javaType=%s,typeHandler=%s},
         *                      #{list[${index}].multi.property,javaType=%s,typeHandler=%s},
         *                  </when>
         *                  <otherwise>
         *                      null,null
         *                  </otherwise>
         *              </choose>
         *          )
         *          </foreach>
         *      </foreach>
         * </sql>
         */
        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_INSERT_VALUES);
        Element foreach = document.createElement("foreach");
        foreach.setAttribute("collection", "list");
        foreach.setAttribute("index", "index");
        foreach.setAttribute("item", "item");
        foreach.setAttribute("separator", ",");
        foreach.appendChild(textNode("("));
        AtomicBoolean first = new AtomicBoolean(true);
        entity.stream().filter(it -> !it.isTransient() && it.insertable())
                .forEach(property -> {
                    if (property.hasAnnotation(MultiColumn.class)) {

                        MultiColumn multiColumn = property.findAnnotation(MultiColumn.class);
                        final Class multiType = Utils.getPropertyJavaType(property);
                        final Entity<Property> multiEntity = Entity.from(multiType);

                        final Element choose = document.createElement("choose");
                        final Element when = document.createElement("when");
                        final String whenTest = String.format("list[index].%s != null", property.getName());
                        when.setAttribute("test", whenTest);
                        final String insertMultiValues = Arrays.stream(multiColumn.properties())
                                .map(multiEntity::getRequiredPersistentProperty)
                                .map(multiProperty -> {
                                    final Class javaType = Utils.getPropertyJavaType(multiProperty);
                                    final TypeHandler typeHandler = Utils.getPropertyTypeHandler(dialect, multiProperty);
                                    //#{list[${index}].multi.property,javaType=%s,typeHandler=%s}
                                    final String value = property.placeholder() ? "list[${index}]" : "item";
                                    ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, multiProperty);
                                    return columnGenerator.generateWriteValue(property.getName(), multiProperty, value);
                                })
                                .collect(Collectors.joining(",\n"));
                        when.appendChild(textNode(first.get() ? insertMultiValues : "," + insertMultiValues));
                        choose.appendChild(when);
                        final Element otherwise = document.createElement("otherwise");
                        final List<String> nullValues = new ArrayList<>();
                        for (int i = 0; i < multiColumn.properties().length; i++) {
                            nullValues.add("null");
                        }
                        final String otherWiseText = first.get() ? String.join(",", nullValues) : "," + String.join(",", nullValues);
                        otherwise.appendChild(textNode(otherWiseText));
                        choose.appendChild(otherwise);
                        first.set(false);
                        foreach.appendChild(choose);
                    } else {
                        //#{list[${index}].property,javaType=%s,typeHandler=%s}
                        final Class javaType = Utils.getPropertyJavaType(property);
                        final TypeHandler typeHandler = Utils.getPropertyTypeHandler(dialect, property);
                        StringBuilder builder = new StringBuilder();

                        if (!first.get()) {
                            builder.append(",");
                        }
                        final ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, property);
                        final String item = property.placeholder() ? "list[${index}]" : "item";
                        final String value = columnGenerator.generateWriteValue(null, property, item);
                        builder.append(value);
                        first.set(false);
                        foreach.appendChild(textNode(builder.toString()));
                    }
                });
        foreach.appendChild(textNode(")"));
        sql.appendChild(foreach);
        context.getNode().appendChild(sql);
    }

    @SuppressWarnings("all")
    private void appendUpdateSqlFragment() {
        /*
         *      <sql id="sql-update">
         *         <set>
         *             <choose>
         *                 <when test="entity != null">
         *                     <if test="entity.property != null">
         *                         column = #{entity.property,javaType=,typeHandler}
         *                     </if>
         *                     <if test="entity.property != null and entity.property.property != null">
         *                         column = #{entity.property.property,javaType=,typeHandler}
         *                     </if>
         *                 </when>
         *                 <when test="update != null">
         *                     <if test="update.contains('property')">
         *                         <choose>
         *                             <when test="update.getUpdateSet('property').operation.name() == 'EQUAL'">
         *                                 column = #{update.getUpdateSet('property').value,javaType=,typeHandler=},
         *                             </when>
         *                             <when test="update.getUpdateSet('property').operation.name() == 'INC'">
         *                                 column = column + 1,
         *                             </when>
         *                             <when test="update.getUpdateSet('property').operation.name() == 'INCR'">
         *                                 column = column + #{update.getUpdateSet('property').value},
         *                             </when>
         *                             <when test="update.getUpdateSet('property').operation.name() == 'DEC'">
         *                                 column = column - 1,
         *                             </when>
         *                             <when test="update.getUpdateSet('property').operation.name() == 'DECR'">
         *                                 column = column - #{update.getUpdateSet('property').value},
         *                             </when>
         *                         </choose>
         *                     </if>
         *                     <if test="update.contains('property') and update.getUpdateSet('property').value.property != null">
         *                          column = #{update.getUpdateSet('property').value.property,javaType=,typeHandler=},
         *                     </if>
         *                 </when>
         *             </choose>
         *         </set>
         *     </sql>
         */
        //<sql id="update">
        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_UPDATE);
        //<set>
        final Element set = document.createElement("set");
        //    <choose>
        final Element choose = document.createElement("choose");
        //        <when test="entity != null">
        final Element whenEntityNotNull = document.createElement("when");
        whenEntityNotNull.setAttribute("test", "entity != null");
        entity.stream().filter(it -> !it.isTransient() && it.updatable())
                .forEach(property -> {
                    if (property.hasAnnotation(MultiColumn.class)) {
                        /**
                         * <if test="entity.property != null and entity.property.property != null">
                         *     column = #{entity.property.property,javaType=,typeHandler},
                         * </if>
                         */
                        final Class multiType = Utils.getPropertyJavaType(property);
                        final Entity<Property> multiEntity = Entity.from(multiType);
                        Arrays.stream(property.findAnnotation(MultiColumn.class).properties())
                                .map(multiEntity::getRequiredPersistentProperty)
                                .map(multiProperty -> {

                                    ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, multiProperty);
                                    final String column = columnGenerator.generateWriteColumn(property.getTable(),property.getColumn(),multiProperty);
                                    final String value = columnGenerator.generateWriteValue(property.getName(),multiProperty,"entity");

                                    final Element ifPropertyNotNull = document.createElement("if");
                                    final String ifTest = String.format("entity.%s != null and entity.%s.%s != null", property.getName(), property.getName(), multiProperty.getName());
                                    ifPropertyNotNull.setAttribute("test", ifTest);
                                    ifPropertyNotNull.appendChild(textNode(String.format("%s = %s,", column, value)));
                                    return ifPropertyNotNull;
                                }).forEach(whenEntityNotNull::appendChild);
                    } else {
                        /**
                         * <if test="entity.property != null">
                         *     column = #{entity.property,javaType=,typeHandler=}
                         * </if>
                         */

                        ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, property);
                        final String column = columnGenerator.generateWriteColumn(property.getTable(),null,property);
                        final String value = columnGenerator.generateWriteValue(null,property,"entity");

                        final Element ifPropertyNotNull = document.createElement("if");
                        final String ifTest = String.format("entity.%s != null", property.getName());
                        ifPropertyNotNull.setAttribute("test", ifTest);
                        ifPropertyNotNull.appendChild(textNode(String.format("%s = %s,", column, value)));
                        whenEntityNotNull.appendChild(ifPropertyNotNull);
                    }

                });
        choose.appendChild(whenEntityNotNull);
        //        <when test="update != null">
        final Element whenUpdateNotNull = document.createElement("when");
        whenUpdateNotNull.setAttribute("test", "update != null");

        entity.stream().filter(Property::updatable)
                .forEach(property -> {

                    /*
                     * <if test="update.contains('property') and update['property'].value.property != null">
                     *      column = #{update[property].value.property,javaType=,typeHandler=}
                     * </if>
                     */
                    if (property.hasAnnotation(MultiColumn.class)) {
                        final Class multiType = Utils.getPropertyJavaType(property);
                        final Entity<Property> multiEntity = Entity.from(multiType);
                        Arrays.stream(property.findAnnotation(MultiColumn.class)
                                .properties())
                                .map(multiEntity::getRequiredPersistentProperty)
                                .map(multiProperty -> {
                                    final Class javaType = Utils.getPropertyJavaType(multiProperty);
                                    final TypeHandler typeHandler = Utils.getPropertyTypeHandler(dialect, multiProperty);

                                    final Element ifUpdateContains = document.createElement("if");
                                    final String multiColumn = multiProperty.isIdProperty() ? property.getColumn()
                                            : property.getColumn() + multiProperty.getColumn().substring(0, 1).toUpperCase() + multiProperty.getColumn().substring(1);
                                    final String updatePath = multiColumn;
                                    final String updateColumn = NameConverterRegistry.getInstance().getColumnNameConverter().map(multiColumn);
                                    final String ifTest = String.format("update['%s'] != null", updatePath);
                                    ifUpdateContains.setAttribute("test", ifTest);

                                    List<Element> whenElements = Arrays.stream(UpdateOperation.values())
                                            .map(operation -> {
                                                final String whenTest = String.format("update['%s'].operation.name() == '%s'", updatePath, operation.name());
                                                String updateSql = null;
                                                switch (operation) {
                                                    case EQUAL:
                                                        updateSql = typeHandler == null ?
                                                                String.format("%s = #{update[%s].value},", updateColumn, updatePath)
                                                                : String.format("%s = #{update[%s].value,javaType=%s,typeHandler=%s},",
                                                                updateColumn, updatePath, javaType.getCanonicalName(), typeHandler.getClass().getCanonicalName());
                                                        break;
                                                    case INC:
                                                        updateSql = String.format("%s = %s + 1,", updateColumn, updateColumn);
                                                        break;
                                                    case INCR:
                                                        updateSql = String.format("%s = %s + #{update[%s].value},", updateColumn, updateColumn, updatePath);
                                                        break;
                                                    case DEC:
                                                        updateSql = String.format("%s = %s - 1,", updateColumn, updateColumn);
                                                        break;
                                                    case DECR:
                                                        updateSql = String.format("%s = %s - #{update[%s].value},", updateColumn, updateColumn, updatePath);
                                                        break;
                                                }
                                                return whenElement(whenTest, textNode(updateSql));
                                            }).collect(Collectors.toList());
                                    ifUpdateContains.appendChild(chooseElement(whenElements));
                                    return ifUpdateContains;
                                }).forEach(whenUpdateNotNull::appendChild);

                    } else {
                        final Class javaType = Utils.getPropertyJavaType(property);
                        final TypeHandler typeHandler = Utils.getPropertyTypeHandler(dialect, property);
                        final Element ifUpdateContains = document.createElement("if");
                        final String updatePath = property.getName();
                        final String ifTest = String.format("update['%s'] != null", updatePath);
                        ifUpdateContains.setAttribute("test", ifTest);

                        ifUpdateContains.setAttribute("test", ifTest);
                        final String multiColumn = property.getColumn();

                        List<Element> whenElements = Arrays.stream(UpdateOperation.values())
                                .map(operation -> {
                                    final String whenTest = String.format("update['%s'].operation.name() == '%s'", updatePath, operation.name());
                                    String updateSql = null;
                                    switch (operation) {
                                        case EQUAL:
                                            updateSql = typeHandler == null ?
                                                    String.format("%s = #{update[%s].value},", multiColumn, updatePath)
                                                    : String.format("%s = #{update[%s].value,javaType=%s,typeHandler=%s},",
                                                    multiColumn, updatePath, javaType.getCanonicalName(), typeHandler.getClass().getCanonicalName());
                                            break;
                                        case INC:
                                            updateSql = String.format("%s = %s + 1,", multiColumn, multiColumn);
                                            break;
                                        case INCR:
                                            updateSql = String.format("%s = %s + #{update[%s].value},", multiColumn, multiColumn, updatePath);
                                            break;
                                        case DEC:
                                            updateSql = String.format("%s = %s - 1,", multiColumn, multiColumn);
                                            break;
                                        case DECR:
                                            updateSql = String.format("%s = %s - #{update[%s].value},", multiColumn, multiColumn, updatePath);
                                            break;
                                    }
                                    return whenElement(whenTest, textNode(updateSql));
                                }).collect(Collectors.toList());
                        ifUpdateContains.appendChild(chooseElement(whenElements));

                        whenUpdateNotNull.appendChild(ifUpdateContains);
                    }
                });
        //        </when>
        choose.appendChild(whenUpdateNotNull);
        //    </choose>
        set.appendChild(choose);
        //</set>
        sql.appendChild(set);
        //</sql>
        context.getNode().appendChild(sql);
    }

    private void appendSqlSelectFragment() {
        /**
         * <sql id="sql-select">
         *     SELECT columns FROM
         * </sql>
         */
        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_SELECT);
        sql.appendChild(textNode("SELECT " + buildSelectColumns() + " FROM"));
        context.getNode().appendChild(sql);
    }

    private Element whenIdNotNull() {
        /*
         * <when test="id != null">
         *     id = #{id}
         * </when>
         */
        final Element whenIdNotNull = document.createElement("when");
        whenIdNotNull.setAttribute("test", "id != null");
        whenIdNotNull.appendChild(textNode(entity.getRequiredIdProperty().getColumn() + " = #{id}"));
        return whenIdNotNull;
    }

    private Element whenIdsNotNull() {
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
        whenIdsNotNull.appendChild(textNode(entity.getRequiredIdProperty().getColumn() + " IN"));
        final Element foreach = document.createElement("foreach");
        foreach.setAttribute("collection", "ids");
        foreach.setAttribute("item", "id");
        foreach.setAttribute("open", "(");
        foreach.setAttribute("separator", ",");
        foreach.setAttribute("close", ")");
        foreach.appendChild(textNode("#{id}"));
        whenIdsNotNull.appendChild(foreach);
        return whenIdsNotNull;
    }
//    ******************************************************************************************************************
//    ******************************************************************************************************************
//    ******************************************************************************************************************

    private Element whenQueryNotNull() {
        /*
         * <when test="query != null">
         *     #{query.sql}
         * </when>
         */
        final Element whenQueryNotNull = document.createElement("when");
        whenQueryNotNull.setAttribute("test", "query != null");
        whenQueryNotNull.appendChild(textNode("${query.sql}"));
        return whenQueryNotNull;
    }

    /*
     *
     */
    private Element whereElement(Element... when) {
        /**
         *  <where>
         *      <choose>
         *          <when test=""/>
         *          <when test=""/>
         *          <when test=""/>
         *      </choose>
         *  </where>
         */
        final Element where = document.createElement("where");
        final Element choose = document.createElement("choose");
        Arrays.stream(when).forEach(choose::appendChild);
        where.appendChild(choose);
        return where;
    }

    @SuppressWarnings("all")
    private void appendInsertElement() {
        final Element insert = document.createElement("insert");
        insert.setAttribute("id", "insert");
        switch (entity.getPrimaryKeyType()) {
            case AUTO_INC:
                /*
                 * <insert id="insert" useGeneratedKeys="true" keyProperty="id" keyColumn="id">
                 * </insert>
                 */
                insert.setAttribute("useGeneratedKeys", "true");
                insert.setAttribute("keyProperty", entity.getRequiredIdProperty().getName());
                insert.setAttribute("keyColumn", entity.getRequiredIdProperty().getColumn());
                break;
            case UUID:
            case UUID_MD5:
                /*
                 * <selectKey keyProperty="id" resultType="java.lang.String" order="BEFORE">
                 *     SELECT REPLACE(UUID(), '-', '') FROM dual
                 * </selectKey>
                 */
                final Element selectKey = document.createElement("selectKey");
                selectKey.setAttribute("keyProperty", entity.getRequiredIdProperty().getName());
                selectKey.setAttribute("resultType", String.class.getCanonicalName());
                selectKey.setAttribute("order", "BEFORE");
                final Text selectKeyText = document.createTextNode(PrimaryKeyType.UUID == entity.getPrimaryKeyType() ?
                        "SELECT REPLACE(UUID(), '-', '') FROM dual" : "SELECT MD5(REPLACE(UUID(), '-', '')) FROM dual");
                selectKey.appendChild(selectKeyText);
                break;
        }

        /*
         *  INSERT INTO
         *  <include refid="sql-insert-columns"/>
         *  <include refid="sql-table"/>
         *  <include refid="sql-insert-values"/>
         *  (columns)
         *  VALUES
         *  (),()
         *  <include refid="where"/>
         */

        insert.appendChild(textNode("INSERT INTO"));
        insert.appendChild(includeElement(SQL_TABLE));
        insert.appendChild(includeElement(SQL_INSERT_COLUMNS));
        insert.appendChild(textNode("VALUES"));
        insert.appendChild(includeElement(SQL_INSERT_VALUES));

        insert.appendChild(whereElement(whenQueryNotNull()));
        context.getNode().appendChild(insert);
    }

    private void appendUpdateElement() {
        /*
         *      <update id="update">
         *         UPDATE
         *         <include refid="sql-table"/>
         *         <include refid="sql-update"/>
         *         <where>
         *             <choose>
         *                 <when test="ids != null">
         *                     id IN
         *                     <foreach collection="ids" item="id" open="(" separator="," close=")">
         *                         #{id}
         *                     </foreach>
         *                 </when>
         *                 <when test="query != null">
         *                     #{query.sql}
         *                 </when>
         *             </choose>
         *         </where>
         *     </update>
         */
        final Element update = document.createElement("update");
        update.setAttribute("id", "update");
        update.appendChild(textNode("UPDATE"));
        update.appendChild(includeElement(SQL_TABLE));
        update.appendChild(includeElement(SQL_UPDATE));
        update.appendChild(whereElement(whenIdsNotNull(), whenQueryNotNull()));
        context.getNode().appendChild(update);
    }

    private void appendDeleteElement() {
        /**
         *     <delete id="delete">
         *         DELETE FROM
         *         <include refid="sql-table"/>
         *         <where>
         *             <choose>
         *                 <when test="ids != null">
         *                     id IN
         *                     <foreach collection="ids" item="id" open="(" separator="," close=")">
         *                         #{id}
         *                     </foreach>
         *                 </when>
         *                 <when test="query != null">
         *                     #{query.sql}
         *                 </when>
         *             </choose>
         *         </where>
         *     </delete>
         */
        final Element delete = document.createElement("delete");
        delete.setAttribute("id", "delete");
        delete.appendChild(textNode("DELETE FROM"));
        delete.appendChild(includeElement(SQL_TABLE));
        delete.appendChild(whereElement(whenIdsNotNull(), whenQueryNotNull()));
        context.getNode().appendChild(delete);
    }

    private void appendSelectElement() {
        /**
         *
         */
        final Element select = document.createElement("select");
        select.setAttribute("id", "select");
        select.setAttribute("resultMap", type.getSimpleName() + "Map");
        select.appendChild(includeElement(SQL_SELECT));
        select.appendChild(includeElement(SQL_TABLE));
        select.appendChild(whereElement(whenIdsNotNull(), whenQueryNotNull()));
        context.getNode().appendChild(select);
    }

    private void appendSelectOneElement() {
        /**
         *
         */
        final Element select = document.createElement("select");
        select.setAttribute("id", "selectOne");
        select.setAttribute("resultMap", type.getSimpleName() + "Map");
        select.appendChild(includeElement(SQL_SELECT));
        select.appendChild(includeElement(SQL_TABLE));
        select.appendChild(whereElement(whenIdNotNull(), whenQueryNotNull()));
        context.getNode().appendChild(select);
    }

    private String buildSelectColumns() {
        final List<String> columns = new ArrayList<>();
        entity.stream().filter(it -> !it.isTransient() && it.selectable())
                .forEach(property -> {
                    if (property.hasAnnotation(MultiColumn.class)) {
                        final Class multiType = Utils.getPropertyJavaType(property);
                        final Entity<Property> multiEntity = Entity.from(multiType);
                        Arrays.stream(property.findAnnotation(MultiColumn.class)
                                .properties())
                                .map(multiEntity::getRequiredPersistentProperty)
                                .filter(Property::selectable)
                                .forEach(multiProperty -> {
                                    ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, property);
                                    columns.add(columnGenerator.generateReadColumn(property.getTable(),property.getColumn(),multiProperty));
                                });
                    } else {

                        ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(dialect, property);
                        columns.add(columnGenerator.generateReadColumn(property.getTable(),null,property));
                    }
                });
        return String.join(",", columns);
    }

    private void appendSelectCountElement() {
        /*
         *     <select id="selectCount" resultType="java.lang.Long">
         *         SELECT COUNT(*) FROM
         *         <include refid="sql-table"/>
         *         <where>
         *
         *     </select>
         */
        final Element selectCount = document.createElement("select");
        selectCount.setAttribute("id", "selectCount");
        selectCount.setAttribute("resultType", Long.class.getCanonicalName());
        Text selectCountText = document.createTextNode("SELECT COUNT(*) FROM");
        selectCount.appendChild(selectCountText);
        // <include refid="sql-table"/>
        selectCount.appendChild(includeElement(SQL_TABLE));
        selectCount.appendChild(whereElement(whenQueryNotNull()));
        context.getNode().appendChild(selectCount);
    }

    @SuppressWarnings("all")
    private Element includeElement(String refid) {
        final Element includeWhere = document.createElement("include");
        includeWhere.setAttribute("refid", refid);
        return includeWhere;
    }

    private Text textNode(String text) {
        return document.createTextNode(text);
    }

    private Element chooseElement(Collection<Element> whenAndOtherWises) {
        final Element choose = document.createElement("choose");
        whenAndOtherWises.forEach(choose::appendChild);
        return choose;
    }

    private Element whenElement(String test, Node child) {
        final Element when = document.createElement("when");
        when.setAttribute("test", test);
        when.appendChild(child);
        return when;
    }

    private String formatColumn(String column) {
        return SQL_KEYS.contains(column.toUpperCase()) ? String.format("`%s`", column) : column;
    }


}
