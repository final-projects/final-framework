package org.finalframework.data.query.criterion.operation;

import org.finalframework.data.query.criterion.operator.CriterionOperator;
import org.finalframework.data.query.criterion.operator.DefaultCriterionOperator;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-18 13:38:04
 * @since 1.0
 */
public abstract class EqualCriterionOperation<T> extends AbsSingleCriterionOperation<T> {

    @Override
    public final CriterionOperator operator() {
        return DefaultCriterionOperator.EQUAL;
    }

}
