package org.ifinal.finalframework.context.expression;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class MethodEvaluationContext extends MethodBasedEvaluationContext {

    private final Set<String> unavailableVariables = new HashSet<>(1);

    public MethodEvaluationContext(final MethodExpressionRootObject rootObject, final Method method,
        final Object[] arguments, final ParameterNameDiscoverer parameterNameDiscoverer) {

        super(rootObject, method, arguments, parameterNameDiscoverer);
    }

    public void addUnavailableVariable(final String name) {

        this.unavailableVariables.add(name);
    }

}
