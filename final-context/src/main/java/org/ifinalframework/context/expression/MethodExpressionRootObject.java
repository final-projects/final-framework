/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ifinalframework.context.expression;

import java.lang.reflect.Method;

import lombok.Getter;

/**
 * @author iimik
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class MethodExpressionRootObject {

    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;

    public MethodExpressionRootObject(final Method method, final Object[] args, final Object target,
                                      final Class<?> targetClass) {

        this.method = method;
        this.args = args;
        this.target = target;
        this.targetClass = targetClass;
    }

}
