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

package org.ifinalframework.json;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TypeReferenceTest.
 *
 * @author iimik
 * @version 1.0.0
 * @since 1.0.0
 */
class TypeReferenceTest {

    @Test
    void primaryType() {
        TypeReference<Integer> reference = new TypeReference<Integer>() {
        };

        assertEquals(Integer.class, reference.getRawType());
        assertEquals(Integer.class, reference.getType());
    }

    @Test
    void listType() {
        TypeReference<List<Integer>> reference = new TypeReference<List<Integer>>() {
        };

        assertEquals(List.class, reference.getRawType());
        assertTrue(reference.getType() instanceof ParameterizedType);
    }

}
