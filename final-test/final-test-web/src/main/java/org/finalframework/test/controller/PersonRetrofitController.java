package org.finalframework.test.controller;

import org.finalframework.test.entity.Person;
import org.finalframework.test.service.PersonRetrofit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * @author likly
 * @version 1.0
 * @date 2020-04-25 17:43:01
 * @since 1.0
 */
@RestController
@RequestMapping("/retrofit/person")
public class PersonRetrofitController {
    public static final Logger logger = LoggerFactory.getLogger(PersonRetrofitController.class);

    @Resource
    private PersonRetrofit personRetrofit;

    @GetMapping
    public Person findById(Long id) throws IOException {
        return personRetrofit.findById(id).execute().body().getData();
    }
}
