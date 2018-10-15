package uk.ac.reigate.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Custom Jackson deserializer for transforming a JSON object (using the ISO 8601 date format) to a
 * Joda LocalDate object.
 */
public class ISO8601LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    
    @SuppressWarnings("deprecation")
    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            return ISODateTimeFormat.dateTimeParser().parseDateTime(str).toLocalDate();
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return new LocalDate(jp.getLongValue());
        }
        throw ctxt.mappingException(handledType());
    }
}
