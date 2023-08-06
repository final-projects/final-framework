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

package org.ifinalframework.poi.databind;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Serialize a value of {@link T} into {@link Cell}.
 *
 * @author ilikly
 * @version 1.2.4
 * @see ExcelDeserializer
 **/
@FunctionalInterface
public interface ExcelSerializer<T> {
    /**
     * Serialize a value of {@link T} into {@link Cell}.
     */
    void serialize(@NonNull Cell cell, @Nullable T value);
}
