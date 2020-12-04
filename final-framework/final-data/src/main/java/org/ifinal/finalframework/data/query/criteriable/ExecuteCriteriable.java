package org.ifinal.finalframework.data.query.criteriable;

import org.ifinal.finalframework.data.query.criterion.CriterionTarget;
import org.ifinal.finalframework.data.query.criterion.function.*;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ExecuteCriteriable<V> extends
        DateFunction<CriterionTarget<CriterionFunction>>,
        MathFunction<CriterionTarget<CriterionFunction>>,
        LogicFunction<V, CriterionTarget<CriterionFunction>>,
        JsonFunction<CriterionTarget<CriterionFunction>> {

}
