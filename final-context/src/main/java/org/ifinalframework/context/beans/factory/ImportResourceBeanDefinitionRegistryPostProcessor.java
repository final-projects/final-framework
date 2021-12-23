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

package org.ifinalframework.context.beans.factory;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ifinalframework.context.beans.factory.support.BeanDefinitionReaders;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Load {@link org.springframework.beans.factory.config.BeanDefinition}s from resource like
 * {@link org.springframework.context.annotation.ImportResource}.
 *
 * @author likly
 * @version 1.2.4
 * @see org.springframework.context.annotation.ImportResource
 * @since 1.2.4
 */
@Slf4j
@Component
public class ImportResourceBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, EnvironmentAware {


    private static final String[] DEFAULT_RESOURCE_LOCATIONS = new String[]{"classpath:spring-config-*.xml", "classpath*:config/spring-config-*.xml", "classpath*:spring/spring-config-*.xml"};

    @Setter
    private ResourceLoader resourceLoader;

    @Setter
    private Environment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {


        final Set<String> importResources = new LinkedHashSet<>();

        final String[] locations = environment.getProperty("spring.application.import-resource.locations", String[].class);

        if (Objects.nonNull(locations) && locations.length > 0) {
            importResources.addAll(Arrays.asList(locations));
        }


        final Boolean useDefault = environment.getProperty("spring.application.import-resource.use-default", boolean.class, true);
        if (Boolean.TRUE.equals(useDefault)) {
            importResources.addAll(Arrays.asList(DEFAULT_RESOURCE_LOCATIONS));
        }

        logger.info("start load import resources: {}", importResources);

        for (String resource : importResources) {
            final BeanDefinitionReader reader = BeanDefinitionReaders.instance(resource, registry, resourceLoader, environment);
            final int count = reader.loadBeanDefinitions(resource);
            logger.info("{} loaded bean definitions count {}.", resource, count);
        }


    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}
