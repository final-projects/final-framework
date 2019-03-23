package org.finalframework.data.query.criterion.operation;

import org.finalframework.data.query.CriterionOperator;
import org.finalframework.data.query.DefaultCriterionOperator;
import org.finalframework.data.query.QProperty;
import org.finalframework.data.query.criterion.SingleCriterionOperation;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-18 13:38:04
 * @since 1.0
 */
public class NotNullCriterionOperation<T> extends AbsCriterionOperation<T> implements SingleCriterionOperation<T> {

    @Override
    public CriterionOperator operator() {
        return DefaultCriterionOperator.NOT_NULL;
    }

    @Override
    public String format(QProperty property, CriterionOperator operator, T value) {
        final String column = getPropertyColumn(property);
        return String.format("%s IS NOT NULL", column);
    }

}