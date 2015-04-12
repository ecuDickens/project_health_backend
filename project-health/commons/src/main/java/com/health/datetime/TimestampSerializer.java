package com.health.datetime;


import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.joda.time.DateTime;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampSerializer extends JsonSerializer<Timestamp> {

    @Override
    public Class<Timestamp> handledType() {
        return Timestamp.class;
    }

    @Override
    public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        DateTime dateTime = DateTimeUtils.getDateTime(value.getTime());
        jgen.writeString(DateTimeUtils.print(dateTime));
    }

    @Override
    public void serializeWithType(Timestamp value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForScalar(value, jgen);
        serialize(value, jgen, provider);
        typeSer.writeTypeSuffixForScalar(value, jgen);
    }
}
