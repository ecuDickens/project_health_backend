package com.health.types;


import com.health.core.map.MapperProvider;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.IOException;

/**
 * Represents errors or exceptions
 */
public class ErrorType {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ErrorType.class);

    /** message */
    private String message;

    /** messages */
    private Iterable<String> messages;

    public ErrorType() {
    }

    public ErrorType(final String message) {
        this.setMessage(message);
    }

    public ErrorType(final Throwable message) {
        this.setMessage(message);
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public Iterable<String> getMessages() {
        return messages;
    }

    public void setMessages(Iterable<String> messages) {
        this.messages = messages;
    }

    public void setMessage(Throwable message) {
        this.message = message.getMessage();
    }

    @JsonProperty("tid")
    public long getThreadId() {
        return Thread.currentThread().getId();
    }

    @Override
    public String toString() {
        try {
            return MapperProvider.INSTANCE.writeValueAsString(this);
        } catch (IOException e) {
            LOGGER.error("Failed to serialize " + this.getClass().getCanonicalName(), e);
            return e.getMessage();
        }
    }
}
