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

package org.ifinalframework.beans;

import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author iimik
 * @version 1.2.4
 * @see PropertyEditorRegistrySupport
 **/
public class NumberPropertyEditorTest {

    @Test
    void number() {
        CustomNumberEditor editor = new CustomNumberEditor(Integer.class, false);
        editor.setAsText("1");
        Assertions.assertEquals(1, editor.getValue());
    }
}
