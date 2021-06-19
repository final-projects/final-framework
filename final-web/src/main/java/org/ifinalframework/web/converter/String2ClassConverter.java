/*
 * Copyright 2020-2021 the original author or authors.
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

package org.ifinalframework.web.converter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RestController;

import org.ifinalframework.context.exception.NotFoundException;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class String2ClassConverter implements Converter<String, Class<?>> {

    @Getter
    private final Map<String, Class<? extends Annotation>> shortAnnotations
        = Stream.of(
        RestController.class, Controller.class,
        Component.class, Service.class, Configuration.class,
        EnableAutoConfiguration.class, Aspect.class
    ).collect(Collectors.toMap(Class::getSimpleName, Function.identity()));

    @Override
    @NonNull
    public Class<?> convert(final @NonNull String source) {
        try {
            return shortAnnotations.containsKey(source) ? shortAnnotations.get(source)
                : ClassUtils.forName(source, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

}
