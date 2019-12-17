package org.finalframework.data.query.criterion.operation;

import org.finalframework.data.query.criterion.operator.CriterionOperator;
import org.finalframework.data.query.criterion.operator.DefaultCriterionOperator;
import org.finalframework.data.query.criterion.FunctionCriterion;
import org.finalframework.data.query.QProperty;
import org.finalframework.data.query.criterion.SingleCriterionOperation;

import java.util.Collection;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-18 13:38:04
 * @since 1.0
 */
public class NullCriterionOperation<T> extends AbsCriterionOperation<T> implements SingleCriterionOperation<T> {

    @Override
    public final CriterionOperator operator() {
        return DefaultCriterionOperator.NULL;
    }

    @Override
    public String format(QProperty property, Collection<FunctionCriterion> functions, CriterionOperator operator, T value) {
        final String column = getPropertyColumn(property, functions);
        return String.format("%s IS NULL", column);
    }

}
