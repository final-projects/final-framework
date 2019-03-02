package com.ilikly.finalframework.data.query.criterion.operation;

import com.ilikly.finalframework.data.query.QProperty;
import com.ilikly.finalframework.data.query.criterion.CriterionOperator;
import com.ilikly.finalframework.data.query.criterion.CriterionOperators;
import com.ilikly.finalframework.data.query.criterion.SingleCriterionOperation;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-18 13:38:04
 * @since 1.0
 */
public class NotStartWithCriterionOperation<T> extends AbsCriterionOperation<T> implements SingleCriterionOperation<T> {
    public static final NotStartWithCriterionOperation INSTANCE = new NotStartWithCriterionOperation();

    @Override
    public CriterionOperator operator() {
        return CriterionOperators.NOT_START_WITH;
    }

    @Override
    public String format(QProperty property, CriterionOperator operator, T value) {
        final String column = getPropertyColumn(property);
        return String.format("%s NOT LIKE '%s%%'", column, value.toString());
    }

}