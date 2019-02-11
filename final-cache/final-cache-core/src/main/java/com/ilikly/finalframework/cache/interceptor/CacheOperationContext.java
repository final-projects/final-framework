package com.ilikly.finalframework.cache.interceptor;

import com.ilikly.finalframework.cache.CacheOperation;
import com.ilikly.finalframework.cache.CacheOperationExpressionEvaluator;
import com.ilikly.finalframework.cache.CacheOperationInvocationContext;
import com.ilikly.finalframework.core.Assert;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A {@link CacheOperationInvocationContext} context for a {@link CacheOperation}
 *
 * @author likly
 * @version 1.0
 * @date 2018-11-23 16:08:08
 * @since 1.0
 */
public class CacheOperationContext<O extends CacheOperation> implements CacheOperationInvocationContext {

    private final CacheOperationExpressionEvaluator evaluator;
    private final CacheOperationMetadata<O> metadata;
    private final Object target;
    private final Object[] args;
    private Boolean conditionPassing;

    public CacheOperationContext(CacheOperationExpressionEvaluator evaluator, CacheOperationMetadata<O> metadata, Object target, Object[] args) {
        this.evaluator = evaluator;
        this.metadata = metadata;
        this.target = target;
        this.args = extractArgs(metadata.getMethod(), args);
    }

    @Override
    public O operation() {
        return this.metadata.getOperation();
    }

    @Override
    public Object target() {
        return this.target;
    }

    @Override
    public Method method() {
        return this.metadata.getMethod();
    }

    @Override
    public Object[] args() {
        return this.args;
    }

    @Override
    public Class<?> returnType() {
        return this.metadata.getReturnType();
    }

    @Override
    public Type genericReturnType() {
        return this.metadata.getGenericReturnType();
    }


    private Object[] extractArgs(Method method, Object[] args) {
        if (!method.isVarArgs()) {
            return args;
        }
        Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
        Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
        System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
        System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
        return combinedArgs;
    }


    @Override
    public Object generateKey(Object result) {

        String[] keys = this.metadata.getOperation().keys();
        EvaluationContext evaluationContext = createEvaluationContext(result);
        Object[] keyValues = Arrays.stream(keys).map(key -> evaluator.key(key, this.metadata.getMethodKey(), evaluationContext)).toArray();

        final String key = Assert.isEmpty(this.metadata.getOperation().keyPattern())
                ? Arrays.stream(keyValues).map(Object::toString).collect(Collectors.joining(":"))
                : String.format(this.metadata.getOperation().keyPattern(), keyValues);

//        if (StringUtils.hasText(this.metadata.getOperation().key())) {
//            return evaluator.key(this.metadata.getOperation().key(), this.metadata.getMethodKey(), evaluationContext);
//        }
        return key;
    }

    @Override
    public Object generateField(Object result) {

        final String[] fields = this.metadata.getOperation().fields();
        if (Assert.isEmpty(fields)) return null;
        EvaluationContext evaluationContext = createEvaluationContext(result);
        Object[] fieldValues = Arrays.stream(fields).map(field -> evaluator.field(field, this.metadata.getMethodKey(), evaluationContext)).toArray();

        final String field = Assert.isEmpty(this.metadata.getOperation().fieldPattern())
                ? Arrays.stream(fieldValues).map(Object::toString).collect(Collectors.joining(":"))
                : String.format(this.metadata.getOperation().fieldPattern(), fieldValues);

//        if (StringUtils.hasText(this.metadata.getOperation().field())) {
//            EvaluationContext evaluationContext = createEvaluationContext(result);
//            return evaluator.key(this.metadata.getOperation().field(), this.metadata.getMethodKey(), evaluationContext);
//        }
        return field;
    }

    @Override
    public boolean isConditionPassing(Object result) {
        if (this.conditionPassing == null) {
            if (StringUtils.hasText(this.metadata.getOperation().condition())) {
                EvaluationContext evaluationContext = createEvaluationContext(result);
                this.conditionPassing = evaluator.condition(this.metadata.getOperation().condition(),
                        this.metadata.getMethodKey(), evaluationContext);
            } else {
                this.conditionPassing = true;
            }
        }
        return this.conditionPassing;
    }

    @Override
    public Object generateExpire(Object result) {
        if (StringUtils.hasText(this.metadata.getOperation().expire())) {
            EvaluationContext evaluationContext = createEvaluationContext(result);
            return evaluator.key(this.metadata.getOperation().expire(), this.metadata.getMethodKey(), evaluationContext);
        }
        return null;
    }

    private EvaluationContext createEvaluationContext(@Nullable Object result) {
        return evaluator.createEvaluationContext(this.metadata.getMethod(), this.args,
                this.target, this.metadata.getTargetClass(), this.metadata.getTargetMethod(), result);
    }

}
