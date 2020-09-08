/*
 * Copyright (c) 2018-2020.  the original author or authors.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.finalframework.data.entity.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.finalframework.annotation.data.YN;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author likly
 * @version 1.0
 * @date 2019-02-16 09:30:26
 * @since 1.0
 */
public class YNTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void of() throws Throwable {
        assertEquals(YN.YES, objectMapper.readValue(objectMapper.writeValueAsString(YN.YES), YN.class));
    }

    @Test
    public void value() throws Throwable {
        assertEquals(YN.YES.getCode().toString(), objectMapper.writeValueAsString(YN.YES));

    }
}