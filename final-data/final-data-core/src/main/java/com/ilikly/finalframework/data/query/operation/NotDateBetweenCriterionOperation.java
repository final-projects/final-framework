package com.ilikly.finalframework.data.query.operation;

import com.ilikly.finalframework.data.query.BetweenCriterionOperation;
import com.ilikly.finalframework.data.query.CriterionOperations;
import com.ilikly.finalframework.data.query.QProperty;

import java.util.Date;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-18 14:38:23
 * @since 1.0
 */
public class NotDateBetweenCriterionOperation extends AbsCriterionOperation<Date> implements BetweenCriterionOperation<Date> {
    public static final NotDateBetweenCriterionOperation INSTANCE = new NotDateBetweenCriterionOperation();

    @Override
    public String name() {
        return CriterionOperations.NOT_DATE_BETWEEN.name();
    }

    @Override
    public String format(QProperty property, String operation, Date min, Date max) {
        final String column = getPropertyColumn(property);
        return String.format("DATE(%s) BETWEEN '%s' AND '%s'", column, format(min), format(max));
    }
}
