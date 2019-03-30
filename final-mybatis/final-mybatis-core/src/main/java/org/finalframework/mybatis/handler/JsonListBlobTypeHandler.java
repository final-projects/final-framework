package org.finalframework.mybatis.handler;

import org.finalframework.json.Json;

import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2018-11-20 11:20:53
 * @since 1.0
 */
public class JsonListBlobTypeHandler<E> extends JsonCollectionBlobTypeHandler<E, List<E>> {
    public JsonListBlobTypeHandler(Class<E> type) {
        super(type);
    }

    @Override
    protected List<E> getNullableResult(String json, Class type) {
        return Json.toCollection(json, List.class, type);
    }
}
