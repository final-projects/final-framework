package org.finalframework.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.finalframework.json.JsonService;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author likly
 * @version 1.0
 * @date 2018-11-23 01:42:54
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class FastJsonService implements JsonService {
    @Override
    public String toJson(Object object) throws Throwable {
        return JSON.toJSONString(object, SerializerFeature.WriteNonStringKeyAsString);
    }

    @Override
    public <T> T toObject(String json, Class<T> classOfT) throws Throwable {
        return JSON.parseObject(json, classOfT);
    }

    @Override
    public <T> T toObject(String json, Type typeOfT) throws Throwable {
        return JSON.parseObject(json, typeOfT);
    }

    @Override
    public <E, T extends Collection<E>> T toCollection(String json, Class<T> collectionClass, Class<E> elementClass) throws Throwable {
        if (List.class.isAssignableFrom(collectionClass)) {
            return (T) JSON.parseArray(json, elementClass);
        } else if (Set.class.isAssignableFrom(collectionClass)) {
            return (T) new HashSet<E>(JSON.parseArray(json, elementClass));
        }
        throw new UnsupportedOperationException("fast json not support toObject " + collectionClass.getName());
    }
}
