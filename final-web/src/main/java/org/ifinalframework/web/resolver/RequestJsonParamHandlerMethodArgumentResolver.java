/*
 * Copyright 2020-2021 the original author or authors.
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

package org.ifinalframework.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import org.ifinalframework.context.exception.BadRequestException;
import org.ifinalframework.json.Json;
import org.ifinalframework.util.Asserts;
import org.ifinalframework.web.annotation.bind.RequestJsonParam;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author likly
 * @version 1.0.0
 * @see RequestJsonParam
 * @since 1.0.0
 */
@Component
public final class RequestJsonParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(RequestJsonParamHandlerMethodArgumentResolver.class);

    @Setter
    private Charset defaultCharset = StandardCharsets.UTF_8;

    /**
     * return {@code true} if the {@linkplain MethodParameter parameter} is annotated by {@link RequestJsonParam},
     * otherwise {@code false}.
     *
     * @param parameter the method parameter
     * @return {@code true} if the {@linkplain MethodParameter parameter} is annotated by {@link RequestJsonParam},
     *     otherwise {@code false}.
     */
    @Override
    public boolean supportsParameter(final @NonNull MethodParameter parameter) {
        // only support the method parameter annotated by @RequestJsonParam
        return parameter.hasParameterAnnotation(RequestJsonParam.class);
    }

    @Override
    public Object resolveArgument(final @NonNull MethodParameter parameter,
        final @Nullable ModelAndViewContainer mavContainer,
        final @NonNull NativeWebRequest webRequest, final @Nullable WebDataBinderFactory binderFactory)
        throws Exception {

        final String contentType = webRequest.getHeader("content-type");

        String value;

        if (Objects.nonNull(contentType) && contentType.startsWith("application/json")) {
            value = parseBody(webRequest);
        } else {
            value = parseParameter(parameter, webRequest);
        }

        if (Objects.isNull(value)) {
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parse RequestJsonParam name={},value={},", parameter.getParameterName(), value);
        }

        return parseJson(value, parameter);

    }

    private String parseBody(final @NonNull NativeWebRequest webRequest) throws IOException {

        final Object nativeRequest = webRequest.getNativeRequest();
        if (nativeRequest instanceof HttpServletRequest) {
            ServletServerHttpRequest inputMessage = new ServletServerHttpRequest((HttpServletRequest) nativeRequest);
            Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
            return StreamUtils.copyToString(inputMessage.getBody(), charset);
        }
        return null;
    }

    private String parseParameter(final @NonNull MethodParameter parameter,
        final @NonNull NativeWebRequest webRequest) {
        // find the @RequestJsonParam annotation
        final RequestJsonParam requestJsonParam = Objects
            .requireNonNull(parameter.getParameterAnnotation(RequestJsonParam.class),
                "requestJsonParam annotation is null");

        final String parameterName = getParameterName(requestJsonParam, parameter);
        Objects.requireNonNull(parameterName);
        String value = webRequest.getParameter(parameterName);
        if (Asserts.isBlank(value) && requestJsonParam.required()) {
            throw new BadRequestException(String.format("parameter %s is required", parameterName));
        }

        if (Asserts.isBlank(value) && !ValueConstants.DEFAULT_NONE.equals(requestJsonParam.defaultValue())) {
            value = requestJsonParam.defaultValue();
        }

        if (Asserts.isBlank(value)) {
            return null;
        }

        return value;
    }

    private Object parseJson(final String json, final MethodParameter parameter) {
        return Json.toObject(json, parameter.getGenericParameterType());
    }

    /**
     * 获取指定的参数名，如果{@link RequestJsonParam#value()}未指定，则使用{@link MethodParameter#getParameterName()}。
     */
    private String getParameterName(final RequestJsonParam requestJsonParam, final MethodParameter parameter) {

        return Asserts.isEmpty(requestJsonParam.value()) ? parameter.getParameterName()
            : requestJsonParam.value().trim();
    }

    @NonNull
    private Charset getContentTypeCharset(final @Nullable MediaType contentType) {

        if (contentType != null && contentType.getCharset() != null) {
            return Optional.ofNullable(contentType.getCharset()).orElse(getDefaultCharset());
        } else {
            return getDefaultCharset();
        }
    }

    @NonNull
    public Charset getDefaultCharset() {
        return this.defaultCharset;
    }

}
