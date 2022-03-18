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

package org.ifinalframework.web.servlet.response.advice;

import org.ifinalframework.core.result.Responsible;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ilikly
 * @version 1.0.0
 * @see Responsible
 * @since 1.0.0
 */
@Order
@RestControllerAdvice
@ConditionalOnProperty(prefix = "final.web.response.advice", name = "responsible", havingValue = "true", matchIfMissing = true)
public class ResponsibleResponseBodyAdvice implements RestResponseBodyAdvice<Object> {


    @Override
    @Nullable
    public Object doBeforeBodyWrite(final @Nullable Object body, final @NonNull MethodParameter returnType,
                                    final MediaType selectedContentType,
                                    final Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                    final ServerHttpRequest request,
                                    final ServerHttpResponse response) {

        if (body instanceof Responsible) {
            final HttpStatus httpStatus = HttpStatus.resolve(((Responsible) body).getStatus());
            if (httpStatus != null) {
                response.setStatusCode(httpStatus);
            }
        }

        return body;
    }

}
