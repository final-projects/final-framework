package org.finalframework.dashboard.mybaits.controller.api;

import org.finalframework.dashboard.mybaits.service.XmlMapperService;
import org.finalframework.dashboard.mybaits.service.query.XmlMapperQuery;
import org.finalframework.web.response.annotation.ResponseIgnore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/17 23:59:25
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/api/mybatis/xml", produces = MediaType.APPLICATION_XML_VALUE)
@ResponseIgnore
public class XmlMapperApiController {

    @Resource
    private XmlMapperService xmlMapperService;


    @RequestMapping
    public String xml(XmlMapperQuery query) {
        return xmlMapperService.xml(query);
    }

}