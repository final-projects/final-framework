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

package org.finalframework.mybatis.builder.xml;


import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author likly
 * @version 1.0
 * @date 2020-04-18 16:09:24
 * @since 1.0
 */
public class XMLMapperEntityResolver implements EntityResolver {
    InputSource source = new InputSource(new StringReader("<?xml version='1.0' encoding='UTF-8'?>"));

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return source;
    }
}
