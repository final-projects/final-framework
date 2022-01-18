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

package org.ifinalframework.poi.databind.deser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.ifinalframework.poi.databind.ExcelDeserializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

/**
 * @author likly
 * @version 1.2.4
 **/
public class LocalDateExcelDeserializer implements ExcelDeserializer<LocalDate> {
    @Nullable
    @Override
    public LocalDate deserialize(@NonNull Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                }
                return null;
            default:
                return null;
        }
    }
}
