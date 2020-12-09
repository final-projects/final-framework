package org.ifinal.finalframework.json.jackson.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ifinal.finalframework.annotation.core.Enums;
import org.ifinal.finalframework.annotation.data.EnumValue;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class EnumValueDescSerializer extends JsonSerializer<Object> {

    private final EnumValue enumValue;

    public EnumValueDescSerializer(EnumValue enumValue) {
        this.enumValue = enumValue;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        Enum<?> anEnum = Enums.findEnum(enumValue, value);
        Field field = ReflectionUtils.findField(enumValue.value(), enumValue.desc());
        Objects.requireNonNull(field, "");
        ReflectionUtils.makeAccessible(field);
        gen.writeObject(ReflectionUtils.getField(field, anEnum));
    }
}

