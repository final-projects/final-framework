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

package org.ifinalframework.context.result;

import org.ifinalframework.context.result.consumer.CommonResultConsumer;
import org.ifinalframework.context.result.function.ObjectResultFunction;
import org.ifinalframework.core.result.Result;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ResultFunctionConsumerCompositeTest.
 *
 * @author ilikly
 * @version 1.2.4
 * @since 1.2.4
 */
class ResultFunctionConsumerCompositeTest {
    private ObjectResultFunction objectResultFunction = new ObjectResultFunction();
    private CommonResultConsumer commonResultConsumer = new CommonResultConsumer();
    private ResultFunctionConsumerComposite resultFunctionConsumerComposite = new ResultFunctionConsumerComposite(
            Arrays.asList(objectResultFunction),
            Arrays.asList(commonResultConsumer)
    );

    @Test
    void apply() {
        final Result<?> result = resultFunctionConsumerComposite.apply("hello");
        assertEquals("hello", result.getData());
    }

}
