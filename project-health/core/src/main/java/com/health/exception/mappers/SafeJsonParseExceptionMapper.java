package com.health.exception.mappers;


import com.health.types.ErrorType;
import org.codehaus.jackson.JsonParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SafeJsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Override
    public Response toResponse(JsonParseException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorType(exception.getMessage().replaceAll("Source:[^;]+;?\\s*", ""))).build();
    }
}