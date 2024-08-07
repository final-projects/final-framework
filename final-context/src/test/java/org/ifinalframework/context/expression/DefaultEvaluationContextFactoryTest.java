/*
 * Copyright 2020-2024 the original author or authors.
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

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DefaultEvaluationContextFactoryTest
 *
 * @author iimik
 * @since 1.5.6
 **/
@ExtendWith(MockitoExtension.class)
class DefaultEvaluationContextFactoryTest {

    @Test
    void create() {

        EvaluationContextFactory factory = new DefaultEvaluationContextFactory();

        final Object object = new Object();

        final EvaluationContext context = factory.create(object);

        Assertions.assertInstanceOf(StandardEvaluationContext.class,context);

    }
}