package org.ifinal.finalframework.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.ifinal.finalframework.annotation.core.IEnum;

/**
 * 枚举{@link IEnum}对象序列化器，将枚举序列化成一个Json对象。
 *
 * <pre>
 *     <code>
 *         {
 *             "name" : "NAME",
 *             "code" : 1,
 *             "description" : "名称",
 *         }
 *     </code>
 * </pre>
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class EnumSerializer extends JsonSerializer<IEnum<?>> {

    public static final EnumSerializer instance = new EnumSerializer();

    @Override
    public void serialize(final IEnum<?> value, final JsonGenerator gen, final SerializerProvider serializers)
        throws IOException {

        gen.writeStartObject();
        gen.writeFieldName("code");
        gen.writeObject(value.getCode());
        if (value instanceof Enum) {
            gen.writeFieldName("name");
            gen.writeObject(((Enum<?>) value).name());
        }
        gen.writeFieldName("description");
        gen.writeObject(value.getDesc());
        gen.writeEndObject();
    }

}
