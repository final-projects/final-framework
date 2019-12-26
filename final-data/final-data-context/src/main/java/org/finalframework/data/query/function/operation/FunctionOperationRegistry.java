package org.finalframework.data.query.function.operation;

import org.finalframework.data.entity.enums.IEnum;
import org.finalframework.data.query.function.annotation.FunctionOperator;
import org.finalframework.data.query.function.expression.*;
import org.finalframework.util.LinkedMultiKeyMap;
import org.finalframework.util.MultiKeyMap;

import java.util.Arrays;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-18 15:05:19
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class FunctionOperationRegistry {

    private static final FunctionOperationRegistry INSTANCE = new FunctionOperationRegistry();

    private final MultiKeyMap<String, Class<?>, FunctionOperationExpression<? extends FunctionOperation>> operations = new LinkedMultiKeyMap<>();

    {
        registerCriterionOperation(new DateFunctionOperationExpression());

        registerCriterionOperation(new NumberAndFunctionOperationExpression<>());
        registerCriterionOperation(new NumberOrFunctionOperationExpression<>());
        registerCriterionOperation(new NumberXorFunctionOperationExpression<>());
        registerCriterionOperation(new NumberNotFunctionOperationExpression<>());

        registerCriterionOperation(new IEnumAndFunctionOperationExpression<>());
        registerCriterionOperation(new IEnumOrFunctionOperationExpression<>());
        registerCriterionOperation(new IEnumXorFunctionOperationExpression<>());
        registerCriterionOperation(new IEnumNotFunctionOperationExpression<>());

        registerCriterionOperation(new MinFunctionOperationExpression());
        registerCriterionOperation(new MaxFunctionOperationExpression());
        registerCriterionOperation(new SumFunctionOperationExpression());
        registerCriterionOperation(new AvgFunctionOperationExpression());

        registerCriterionOperation(new ConcatFunctionOperationExpression<>());

        registerCriterionOperation(new JsonExtractFunctionOperationExpression<>());
        registerCriterionOperation(new JsonUnquoteFunctionOperationExpression<>());
    }

    private FunctionOperationRegistry() {
    }

    public static FunctionOperationRegistry getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        new FunctionOperationRegistry();
    }

    public <T> void registerCriterionOperation(String operator, Class<T> type, FunctionOperationExpression<? extends FunctionOperation> operation) {
        operations.add(operator, type, operation);
    }

    public <T> void registerCriterionOperation(FunctionOperationExpression<? extends FunctionOperation> criterionOperation) {
        FunctionOperator functionOperator = criterionOperation.getClass().getAnnotation(FunctionOperator.class);
        if (functionOperator == null) {
            throw new IllegalArgumentException();
        }

        String operator = functionOperator.value();
        Class<?>[] types = functionOperator.types();

        Arrays.stream(types).forEach(type -> registerCriterionOperation(operator, type, criterionOperation));


    }

    @SuppressWarnings("unchecked")
    public <T> FunctionOperationExpression<? extends FunctionOperation> getCriterionOperation(String operator, Class<T> type) {
        Class<?> key = type;
        if (type.isEnum()) {
            if (IEnum.class.isAssignableFrom(type)) {
                key = IEnum.class;
            }
        }

        FunctionOperationExpression<? extends FunctionOperation> functionOperationExpression = operations.get(operator, key);

        if (functionOperationExpression == null) {
            functionOperationExpression = operations.get(operator, Object.class);
        }


        if (functionOperationExpression == null) {
            throw new IllegalArgumentException(String.format("not found criterion operator of operator = %s and type = %s", operator, type.getCanonicalName()));
        }
        return functionOperationExpression;
    }


}
