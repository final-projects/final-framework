package org.finalframework.data.query.function.expression;


import org.finalframework.data.entity.enums.IEnum;
import org.finalframework.data.query.function.operation.FunctionOperationExpression;
import org.finalframework.data.query.function.operation.SingleFunctionOperation;

/**
 * @author likly
 * @version 1.0
 * @date 2019-12-17 16:06:50
 * @since 1.0
 */
@FunctionExpression(
        value = FunctionExpression.OR,
        types = {IEnum.class}
)
public class IEnumOrFunctionOperationExpression<T extends IEnum> implements FunctionOperationExpression<SingleFunctionOperation<T>> {
    @Override
    public String expression(String target, SingleFunctionOperation<T> criterion) {
        return String.format("%s | %s", target, String.valueOf(criterion.value().getCode()));
    }
}

