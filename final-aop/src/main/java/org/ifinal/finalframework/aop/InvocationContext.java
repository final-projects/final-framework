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
 *
 */

package org.ifinal.finalframework.aop;

import java.util.Map;

import org.ifinal.finalframework.context.expression.MethodMetadata;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface InvocationContext {

    MethodMetadata metadata();

    Object target();

    Object[] args();

    Class<?> view();

    Map<String, Object> attributes();

    void addAttribute(String name, Object value);

    <T> T getAttribute(String name);

}
