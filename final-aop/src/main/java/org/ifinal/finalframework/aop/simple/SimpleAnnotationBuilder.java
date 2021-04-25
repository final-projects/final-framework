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

package org.ifinal.finalframework.aop.simple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.ifinal.finalframework.aop.AnnotationBuilder;

import org.springframework.lang.NonNull;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public final class SimpleAnnotationBuilder<A extends Annotation> implements AnnotationBuilder<A, A> {

    @Override
    @NonNull
    public A build(final @NonNull Class<?> type, final @NonNull A annotation) {

        return annotation;
    }

    @Override
    @NonNull
    public A build(final @NonNull Method method, final @NonNull A annotation) {

        return annotation;
    }

    @Override
    @NonNull
    public A build(final @NonNull Parameter parameter, final @NonNull Integer index, final @NonNull A annotation) {

        return annotation;
    }

}
