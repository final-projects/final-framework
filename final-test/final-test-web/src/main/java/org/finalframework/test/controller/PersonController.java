package org.finalframework.test.controller;

import org.finalframework.data.api.service.EntityService;
import org.finalframework.data.query.Query;
import org.finalframework.test.entity.Person;
import org.finalframework.test.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author likly
 * @version 1.0
 * @date 2020-03-18 13:07:50
 * @since 1.0
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    public static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Resource
    private PersonService personService;
    @Resource
    private EntityService entityService;

    @GetMapping
    public List<Person> query() {
        return personService.select(new Query().page(1, 2));
    }

    @PostMapping
    public Person insert(Person person, @RequestParam(required = false, defaultValue = "false") boolean ignore) {
        int save = personService.insert(ignore, person);
        logger.info("save={}", save);
        return person;
    }


    @PostMapping("/save")
    public Person save(Person person) {
        int save = personService.save(person);
        logger.info("save={}", save);
        return person;
    }
}

