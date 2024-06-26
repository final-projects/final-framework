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

package org.ifinalframework.aop.simple;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;

import org.ifinalframework.aop.AnnotationFinder;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author iimik
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleAnnotationFinder implements AnnotationFinder<Boolean>, Serializable {

    private static final long serialVersionUID = 4901280051679214659L;

    private final Collection<Class<? extends Annotation>> annotationTypes = new ArrayList<>();

    public SimpleAnnotationFinder(final Collection<Class<? extends Annotation>> annotationTypes) {

        this.annotationTypes.addAll(annotationTypes);
    }

    @Override
    public Boolean findAnnotations(final @NonNull AnnotatedElement ae) {

        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (AnnotatedElementUtils.isAnnotated(ae, annotationType)) {
                return true;
            }
        }

        return false;
    }

}
