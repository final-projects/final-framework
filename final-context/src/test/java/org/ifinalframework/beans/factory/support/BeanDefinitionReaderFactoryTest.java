/*
 * Copyright 2020-2022 the original author or authors.
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

package org.ifinalframework.beans.factory.support;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ilikly
 * @version 1.2.4
 **/
@ExtendWith(MockitoExtension.class)
class BeanDefinitionReaderFactoryTest {


    @Test
    void create() {

        BeanDefinitionReaderFactory factory = new BeanDefinitionReaderFactory(new StandardEnvironment(), new PathMatchingResourcePatternResolver(), new AnnotationConfigApplicationContext());

        BeanDefinitionReader reader = factory.create("*.xml");
        assertTrue(reader instanceof XmlBeanDefinitionReader);

    }
}