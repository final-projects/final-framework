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
 */

package org.ifinalframework.context.user;

import org.ifinalframework.core.IUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * UserContextHolderTest.
 *
 * @author ilikly
 * @version 1.0.0
 * @since 1.0.0
 */
class UserContextHolderTest {

    @Test
    void test() throws InterruptedException {

        User user = new User(1L, "xiaoMing");

        UserContextHolder.setUser(user);

        new Thread(() -> assertNull(UserContextHolder.getUser())).start();

        UserContextHolder.reset();
        UserContextHolder.setUser(user);

        new Thread(() -> assertNotNull(UserContextHolder.getUser())).start();

        Thread.sleep(1000);

    }

    @Data
    @AllArgsConstructor
    static class User implements IUser<Long> {

        private Long id;

        private String name;

    }

}
