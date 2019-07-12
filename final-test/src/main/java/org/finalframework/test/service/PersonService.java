package org.finalframework.test.service;

import org.finalframework.monitor.annotation.OperationAction;
import org.finalframework.test.entity.Person;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-28 17:47:13
 * @since 1.0
 */
public interface PersonService {
    @OperationAction(type = 1, name = "PersonService", target = "{#id}")
    Person findById(Long id);
}
