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
 *
 */

package org.ifinal.finalframework.util;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Enums {

    private Enums() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E extends Enum<E>> E findEnum(final @NonNull Class<? extends Enum> clazz,
        final @NonNull String creator,
        final @Nullable Class<?> valueType, final @NonNull Object value) {

        final Method valueOf = Objects.isNull(valueType) ? ReflectionUtils.findMethod(clazz, creator)
            : ReflectionUtils.findMethod(clazz, creator, valueType);

        if (valueOf == null) {
            throw new IllegalArgumentException(
                "can not find method named " + creator + " as enum class " + clazz.getCanonicalName());
        }

        return (E) ReflectionUtils.invokeMethod(valueOf, clazz, value);
    }

    public static String getEnumI18NCode(final Enum<?> value) {
        return String.format("%s.%s", value.getClass().getCanonicalName(), value.name().toLowerCase(Locale.ENGLISH));
    }

}

