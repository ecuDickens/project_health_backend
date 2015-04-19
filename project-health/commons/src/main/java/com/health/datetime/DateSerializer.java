package com.health.datetime;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

import java.io.IOException;
import java.sql.Date;

public class DateSerializer extends JsonSerializer<Date> {

    public Class<Date> handledType() {
        return Date.class;
    }

    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(date.toString());
    }

    public void serializeWithType(Date date, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForScalar(date, jgen);
        serialize(date, jgen, provider);
        typeSer.writeTypeSuffixForScalar(date, jgen);
    }
}
