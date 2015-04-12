package com.health.exception.mappers;


import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.health.types.ErrorType;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.health.collect.MoreIterables.asFluent;

@Provider
public class SafeJsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

    private static Function<JsonMappingException.Reference, String> getFieldName = new Function<JsonMappingException.Reference, String>() {

        @Override
        public String apply(JsonMappingException.Reference reference) {
            return reference.getFieldName();
        }
    };

    private static Joiner pathJoiner = Joiner.on(" -> ").skipNulls();

    @Override
    public Response toResponse(JsonMappingException exception) {
        if (exception instanceof UnrecognizedPropertyException) {
            return handleUnrecognizedPropertyException((UnrecognizedPropertyException)exception);
        } else {
            String msg = "Error parsing: " + pathJoiner.join(asFluent(exception.getPath()).map(getFieldName));

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorType(msg))
                    .build();
        }
    }

    private Response handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorType("Unrecognized property: " + e.getUnrecognizedPropertyName()))
                .build();
    }
}
