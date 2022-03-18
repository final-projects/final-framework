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

package org.ifinalframework.poi.databind.ser;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

/**
 * LocalDateTimeExcelSerializerTest.
 *
 * @author ilikly
 * @version 1.2.4
 * @since 1.2.4
 */
@ExtendWith(MockitoExtension.class)
class LocalDateTimeExcelSerializerTest {

    private LocalDateTimeExcelSerializer serializer = new LocalDateTimeExcelSerializer();

    @Mock
    private Cell cell;


    @Test
    void serialize() {
        final LocalDateTime now = LocalDateTime.now();
        serializer.serialize(cell, now);

        final ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(cell, only()).setCellValue(captor.capture());
        assertEquals(now, captor.getValue());
    }
}
