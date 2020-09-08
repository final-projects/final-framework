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

package org.finalframework.data.query;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.finalframework.annotation.query.AndOr;
import org.finalframework.annotation.query.Criteria;
import org.finalframework.annotation.query.Equal;
import org.finalframework.data.mapping.Entity;
import org.finalframework.data.mapping.Property;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * @author likly
 * @version 1.0
 * @date 2020-06-04 16:54:32
 * @since 1.0
 */
class IQueryTest {
    private static final Logger logger = LoggerFactory.getLogger(IQueryTest.class);

    @Test
    void apply() {
        final XPathParser parser = new XPathParser("<script></script>");
        final XNode script = parser.evalNode("//script");
        final Document document = script.getNode().getOwnerDocument();

        final QEntity<?, ?> entity = QEntity.from(QueryEntity.class);
        QProperty<String> name = entity.getProperty("name");
        QProperty<Integer> age = entity.getProperty("age");


        final QueryEntityQuery query = new QueryEntityQuery();

        final Entity<QueryEntityQuery> properties = Entity.from(QueryEntityQuery.class);
        final Element where = document.createElement("where");


        final AndOr andOr = properties.isAnnotationPresent(Criteria.class) ? properties.getRequiredAnnotation(Criteria.class).value() : AndOr.AND;

        for (Property property : properties) {
            if (property.hasAnnotation(Equal.class)) {
                final Element element = document.createElement("if");
                element.setAttribute("test", String.format("%s.%s != null", "query", property.getName()));
                element.appendChild(document.createCDATASection(String.format("%s %s = #{%s.%s}", andOr, entity.getProperty(property.getName()).getColumn(), "query", property.getName())));
                where.appendChild(element);
            }
        }


        script.getNode().appendChild(where);


        final String sql = script.toString();
        if (logger.isDebugEnabled()) {
            final String[] sqls = sql.split("\n");
            for (String item : sqls) {
                logger.debug(item);
            }
        }

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("query", query);

        final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
        final SqlSource sqlSource = languageDriver.createSqlSource(new Configuration(), script, null);
        final BoundSql boundSql = sqlSource.getBoundSql(parameter);
        logger.info("Sql: ==> {}", boundSql.getSql());
        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
            logger.info("Parameter ==> {}={}", parameterMapping.getProperty(), OgnlCache.getValue(parameterMapping.getProperty(), parameter));
        }


    }
}