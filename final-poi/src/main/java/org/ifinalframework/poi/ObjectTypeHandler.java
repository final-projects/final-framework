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

package org.ifinalframework.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.ifinalframework.poi.databind.TypeHandler;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * ObjectTypeHandler.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ObjectTypeHandler implements TypeHandler<Object> {

    private static final Set<Class<?>> NUMBERS = new HashSet<>(Arrays.asList(
            short.class, Short.class,
            int.class, Integer.class,
            long.class, Long.class,
            float.class, Float.class,
            double.class, Double.class
    ));

    private static final Set<Class<?>> BOOLEANS = new HashSet<>(Arrays.asList(boolean.class, Boolean.class));

    @Override
    public void serialize(@NonNull final Cell cell, @Nullable final Object value) {
        if (Objects.isNull(value)) {
            return;
        }

        Class<?> clazz = value.getClass();
        String text = value.toString();
        if (NUMBERS.contains(clazz)) {
            cell.setCellValue(Double.parseDouble(text));
        } else if (BOOLEANS.contains(clazz)) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else {

            if (text.startsWith("=")) {
                cell.setCellFormula(text.substring(1));
            } else {
                cell.setCellValue(text);
            }
        }
    }

    @Override
    public Object deserialize(Cell cell) {
        throw new UnsupportedOperationException("");
    }

}
