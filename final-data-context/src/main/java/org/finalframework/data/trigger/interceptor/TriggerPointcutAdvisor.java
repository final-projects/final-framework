/*
 * Copyright (c) 2018-2020.  the original author or authors.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.finalframework.data.trigger.interceptor;


import org.finalframework.auto.spring.factory.annotation.SpringComponent;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import javax.annotation.PostConstruct;

/**
 * @author likly
 * @version 1.0
 * @date 2020-04-03 15:38:22
 * @since 1.0
 */
@SpringComponent
public class TriggerPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {


    @PostConstruct
    public void init() {
        this.setAdviceBeanName("triggerMethodInterceptor");
    }

    @Override
    public Pointcut getPointcut() {
        return new TriggerPointcut();
    }


}

