package com.ilikly.finalframework.json.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ilikly.finalframework.core.Assert;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author likly
 * @version 1.0
 * @date 2018-12-28 16:59:46
 * @since 1.0
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private DateTimeFormatters dateTimeFormatters = new DateTimeFormatters();

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getValueAsString();
        if (Assert.isEmpty(value)) return null;

        LocalDateTime localDateTime = dateTimeFormatters.parse(value);
        if (localDateTime != null) return localDateTime;
        if (!p.isNaN()) {
            long timestamp = p.getValueAsLong();
            Instant instant = Instant.ofEpochMilli(timestamp);
            ZoneId zone = ZoneId.systemDefault();
            return LocalDateTime.ofInstant(instant, zone);
        }
        return null;

    }
}
