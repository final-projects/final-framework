package org.finalframework.data.query.criterion.function.expression;


import org.finalframework.data.query.criterion.function.FunctionOperator;
import org.finalframework.data.query.criterion.function.SupportFunctions;
import org.finalframework.data.query.criterion.function.operation.FunctionOperationExpression;
import org.finalframework.data.query.criterion.function.operation.SingleFunctionOperation;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-28 21:11:49
 * @since 1.0
 */
@SupportFunctions(FunctionOperator.JSON_EXTRACT)
public class JsonExtractFunctionOperationExpression<T> implements FunctionOperationExpression<SingleFunctionOperation<T>> {

    @Override
    public String expression(String target, SingleFunctionOperation<T> criterion) {
        return String.format("JSON_EXTRACT(%s,'%s')", target, criterion.value());
    }

}
