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

package org.ifinalframework.poi.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Row
 *
 * @author iimik
 * @since 1.5.2
 **/
@Setter
@Getter
public class Row {

    private List<Cell> cells;

    private Float height;

    private String style;

    public Row() {
    }

    public Row(final List<Cell> cells) {
        this.cells = cells;
    }

}
