package org.ifinal.finalframework.monitor.handler;


import org.ifinal.finalframework.aop.Operation;
import org.ifinal.finalframework.aop.OperationMetadata;
import org.ifinal.finalframework.aop.interceptor.AbsOperationHandlerSupport;
import org.ifinal.finalframework.context.user.UserContextHolder;
import org.ifinal.finalframework.monitor.MonitorOperationExpressionEvaluator;
import org.ifinal.finalframework.monitor.MonitorOperationHandlerSupport;
import org.ifinal.finalframework.monitor.interceptor.DefaultMonitorOperationExpressionEvaluator;
import org.ifinal.finalframework.util.Asserts;
import org.springframework.expression.EvaluationContext;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class AbsMonitorOperationHandlerSupport extends AbsOperationHandlerSupport implements MonitorOperationHandlerSupport {
    private final MonitorOperationExpressionEvaluator evaluator;

    public AbsMonitorOperationHandlerSupport() {
        this(new DefaultMonitorOperationExpressionEvaluator());
    }

    public AbsMonitorOperationHandlerSupport(MonitorOperationExpressionEvaluator evaluator) {
        super(evaluator);
        this.evaluator = evaluator;
    }

    @Override
    public String generateName(String name, OperationMetadata<? extends Operation> metadata, EvaluationContext evaluationContext) {
        if (Asserts.isBlank(name)) return null;


        if (isExpression(name)) {
            return evaluator.name(generateExpression(name), metadata.getMethodKey(), evaluationContext);
        } else {
            for (String expression : findExpressions(name)) {
                final String value = evaluator.name(generateExpression(expression), metadata.getMethodKey(), evaluationContext);
                name = name.replace(expression, value);
            }
        }
        return name;
    }

    @Override
    public Object generateOperator(String operator, OperationMetadata<? extends Operation> metadata, EvaluationContext evaluationContext) {
        if (Asserts.nonBlank(operator)) {
            if (isExpression(operator)) {
                return evaluator.operator(generateExpression(operator), metadata.getMethodKey(), evaluationContext);
            }
            return operator;
        }

        return UserContextHolder.getUser();
    }

    @Override
    public Object generateTarget(String target, OperationMetadata<? extends Operation> metadata, EvaluationContext evaluationContext) {
        if (Asserts.isBlank(target)) return null;
        if (isExpression(target)) {
            return evaluator.target(generateExpression(target), metadata.getMethodKey(), evaluationContext);
        }
        return target;
    }

    @Override
    public Object generateAttribute(String attribute, OperationMetadata<? extends Operation> metadata, EvaluationContext evaluationContext) {
        if (Asserts.nonBlank(attribute) && isExpression(attribute)) {
            return evaluator.attribute(generateExpression(attribute), metadata.getMethodKey(), evaluationContext);
        }
        return attribute;
    }
}