package org.finalframework.monitor.action;

import java.util.HashMap;
import java.util.Map;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-27 23:56:52
 * @since 1.0
 */
public class ActionContext<T> {
    private final String name;
    private final int type;
    private final int action;
    private final ActionLevel level;
    private final T operator;
    private final Object target;
    private final Map<String, Object> attributes;

    private ActionContext(Builder<T> builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.action = builder.action;
        this.level = builder.level;
        this.operator = builder.operator;
        this.target = builder.target;
        this.attributes = builder.attributes;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getAction() {
        return action;
    }

    public ActionLevel getLevel() {
        return level;
    }

    public T getOperator() {
        return operator;
    }

    public Object getTarget() {
        return target;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public static class Builder<T> implements org.finalframework.core.Builder<ActionContext<T>> {
        private String name;
        private int type;
        private int action;
        private ActionLevel level;
        private T operator;
        private Object target;
        private Map<String, Object> attributes = new HashMap<>();

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder action(int action) {
            this.action = action;
            return this;
        }

        public Builder level(ActionLevel level) {
            this.level = level;
            return this;
        }

        public Builder operator(T operator) {
            this.operator = operator;
            return this;
        }

        public Builder target(Object target) {
            this.target = target;
            return this;
        }

        public Builder addAttribute(String name, Object value) {
            this.attributes.put(name, value);
            return this;
        }

        @Override
        public ActionContext<T> build() {
            return new ActionContext<>(this);
        }
    }
}
