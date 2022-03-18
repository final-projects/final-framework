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

package org.ifinalframework.web.converter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ifinalframework.core.IEnum;
import org.ifinalframework.web.converter.EnumConverterFactory.EnumConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * EnumConverterFactoryTest.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
class EnumConverterFactoryTest {

    private final EnumConverterFactory enumConverterFactory = new EnumConverterFactory();

    @Test
    void getEnumConverter() {
        Assertions.assertTrue(enumConverterFactory.getConverter(YN.class) instanceof EnumConverter);
    }

    @Test
    void enumConvert() {
        EnumConverter<YN> converter = new EnumConverter<>(YN.class);
        assertEquals(YN.class.getSimpleName() + "Converter", converter.toString());
        assertEquals(YN.YES, converter.convert(YN.YES.getCode().toString()));
        assertEquals(YN.NO, converter.convert(YN.NO.getCode().toString()));
        assertNull(converter.convert("-1"));

    }

    @Getter
    @AllArgsConstructor
    public enum YN implements IEnum<Integer> {
        YES(1, "YES"),
        NO(0, "YES");

        private final Integer code;

        private final String desc;
    }

}
