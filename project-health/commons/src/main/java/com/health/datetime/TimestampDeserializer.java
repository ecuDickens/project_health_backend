package com.health.datetime;


import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        try {
            DateTime dateTime =  DateTimeUtils.parse(jp.getText());
            return new Timestamp(dateTime.toInstant().getMillis());
        }
        catch(UnsupportedOperationException e) {
            throw new IOException("Error parsing date time: " + jp.getText(), e);
        }
        catch(IllegalArgumentException e) {
            throw new IOException("Error parsing date time: " + jp.getText(), e);
        }
    }
}
