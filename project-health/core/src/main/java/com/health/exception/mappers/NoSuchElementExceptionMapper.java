package com.health.exception.mappers;


import com.health.types.ErrorType;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.NoSuchElementException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class NoSuchElementExceptionMapper implements ExceptionMapper<NoSuchElementException> {

    @Override
    public Response toResponse(NoSuchElementException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(new ErrorType(e)).build();
    }
}
