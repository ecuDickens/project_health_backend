package com.health.types;


import com.health.core.map.MapperProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

/**
 * Represents errors or exceptions
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErrorType {

    private String message;
    private Iterable<String> messages;

    public ErrorType() {
    }

    public ErrorType(final String message) {
        this.setMessage(message);
    }

    public ErrorType(final Throwable message) {
        this.setMessage(message);
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Iterable<String> getMessages() {
        return messages;
    }
    public void setMessages(Iterable<String> messages) {
        this.messages = messages;
    }

    public void setMessage(Throwable message) {
        this.message = message.getMessage();
    }

    @Override
    public String toString() {
        try {
            return MapperProvider.INSTANCE.writeValueAsString(this);
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
