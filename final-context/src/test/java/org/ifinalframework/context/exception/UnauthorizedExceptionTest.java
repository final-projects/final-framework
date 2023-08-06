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

package org.ifinalframework.context.exception;

import org.ifinalframework.core.IException;
import org.ifinalframework.core.ResponseStatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author ilikly
 * @version 1.3.1
 **/
class UnauthorizedExceptionTest {

    @Test
    void test() {
        UnauthorizedException exception = new UnauthorizedException("haha");
        assertEquals("haha", exception.getMessage());
        assertEquals(UnauthorizedException.DEFAULT.getCode(), exception.getCode());

        IException mock = mock(IException.class);
        when(mock.getCode()).thenReturn(ResponseStatus.UNAUTHORIZED.getCode().toString());
        when(mock.getMessage()).thenReturn(ResponseStatus.UNAUTHORIZED.getDesc());
        exception = new UnauthorizedException(mock);

        assertEquals(ResponseStatus.UNAUTHORIZED.getCode().toString(), exception.getCode());
        assertEquals(ResponseStatus.UNAUTHORIZED.getDesc(), exception.getMessage());

    }

}