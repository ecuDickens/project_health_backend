package com.health.datetime;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdScalarDeserializer;
import org.joda.time.DateTime;

import java.io.IOException;
import java.sql.Date;

public class DateDeserializer extends StdScalarDeserializer<Date> {

    public DateDeserializer() {
        super(DateTime.class);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException {
        try {
            return new Date(DateTimeUtils.parse(jsonparser.getText()).getMillis());
        }
        catch(UnsupportedOperationException e) {
            throw new IOException("Error parsing date time: " + jsonparser.getText(), e);
        }
        catch(IllegalArgumentException e) {
            throw new IOException("Error parsing date time: " + jsonparser.getText(), e);
        }
    }
}
