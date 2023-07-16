/*
 * Copyright 2020-2023 the original author or authors.
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

package org.ifinalframework.validation.beanvalidation;

import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.ifinalframework.validation.GlobalValidationGroupsProvider;
import org.ifinalframework.validation.MethodValidationGroupsProvider;
import org.ifinalframework.validation.NoGlobalValidationGroupsProvider;
import org.ifinalframework.validation.NoMethodValidationGroupsProvider;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;

import jakarta.validation.Validator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * FinalMethodValidationInterceptor.
 *
 * @author ilikly
 * @version 1.5.0
 * @since 1.5.0
 */
public class FinalMethodValidationInterceptor extends MethodValidationInterceptor {

    @Setter
    private GlobalValidationGroupsProvider globalValidationGroupsProvider = new NoGlobalValidationGroupsProvider();
    @Setter
    private MethodValidationGroupsProvider methodValidationGroupsProvider = new NoMethodValidationGroupsProvider();

    public FinalMethodValidationInterceptor() {
    }

    public FinalMethodValidationInterceptor(Validator validator) {
        super(validator);
    }

    @Override
    @NonNull
    protected Class<?>[] determineValidationGroups(@NonNull MethodInvocation invocation) {

        final Class<?>[] determineValidationGroups = super.determineValidationGroups(invocation);

        final List<Class<?>> allValidationGroups = new LinkedList<>(Arrays.asList(determineValidationGroups));

        allValidationGroups.addAll(globalValidationGroupsProvider.getValidationGroups(determineValidationGroups.length > 0 ? determineValidationGroups[0] : null));
        allValidationGroups.addAll(methodValidationGroupsProvider.getValidationGroups(invocation));


        return ClassUtils.toClassArray(allValidationGroups);
    }
}
