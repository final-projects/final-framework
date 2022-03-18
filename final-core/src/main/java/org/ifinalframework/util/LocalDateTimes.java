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

package org.ifinalframework.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author ilikly
 * @version 1.3.0
 **/
@UtilityClass
public final class LocalDateTimes {

    public static LocalDateTime localDateTime(LocalDateTime localDateTime, ZoneId sourceZoneId, ZoneId targetZoneId) {
        return localDateTime.atZone(sourceZoneId).withZoneSameInstant(targetZoneId).toLocalDateTime();
    }

}
