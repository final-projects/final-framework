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

package org.ifinalframework.junit;


import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * AssumptionsExample.
 *
 * @author iimik
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
class AssumptionsExampleTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void assumeTrue(Boolean value) {
        logger.info("value={}", value);
        Assumptions.assumeTrue(value);
        assertTrue(value);
        logger.info("可以执行测试。。。");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void assumeThat(Boolean value) {
        assumingThat(value, () -> {
            logger.info("value is true: {}", value);
            assertTrue(value);
        });

        assumingThat(!value, () -> {
            logger.info("value is false: {}", value);
            assertFalse(value);
        });
    }

}
