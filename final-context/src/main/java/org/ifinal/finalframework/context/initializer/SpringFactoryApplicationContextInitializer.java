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

package org.ifinal.finalframework.context.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

import org.ifinal.auto.spring.factory.annotation.SpringFactory;
import org.ifinal.finalframework.context.beans.factory.support.SpringFactoryBeanDefinitionRegistryPostProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author likly
 * @version 1.0.0
 * @see SpringFactory
 * @see ApplicationContextInitializer
 * @see SpringFactoryBeanDefinitionRegistryPostProcessor
 * @since 1.0.0
 */
@Slf4j
@SpringFactory(ApplicationContextInitializer.class)
public final class SpringFactoryApplicationContextInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Class<SpringFactory> springFactory = SpringFactory.class;

    @Override
    public void initialize(final @NonNull ConfigurableApplicationContext context) {
        context.addBeanFactoryPostProcessor(new SpringFactoryBeanDefinitionRegistryPostProcessor(springFactory, true));
    }

}

