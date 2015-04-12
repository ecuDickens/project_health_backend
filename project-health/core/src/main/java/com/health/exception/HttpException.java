package com.health.exception;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Checked exception along the same lines as a WebApplicationException.
 */
public class HttpException extends Exception {
    private final int _statusCode;

    @JsonCreator
    public HttpException(@JsonProperty("statusCode") int statusCode, @JsonProperty("message") String message, @JsonProperty("cause") Throwable t) {
        super(message, t);
        _statusCode = statusCode;
    }

    public int getStatusCode() {
        return _statusCode;
    }
}