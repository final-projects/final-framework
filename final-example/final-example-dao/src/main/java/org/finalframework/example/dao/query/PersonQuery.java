package org.finalframework.example.dao.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.finalframework.annotation.query.AndOr;
import org.finalframework.annotation.query.Criteria;
import org.finalframework.annotation.query.Equal;
import org.finalframework.annotation.query.PageQuery;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/18 10:12:12
 * @since 1.0
 */
@Data
@Criteria(AndOr.OR)
@EqualsAndHashCode(callSuper = true)
public class PersonQuery extends PageQuery {
    @Equal
    private String name;

    @Criteria
    private OrQuery orQuery;

    @Data
    static class OrQuery {
        @Equal
        private String password;
        @Equal
        private Boolean admin;
    }

}