package com.github.robi42.boot.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    @Override
    public Response toResponse(final JsonProcessingException exception) {
        return Response.status(422) // -> Unprocessable Entity
                .entity(ErrorResponseBody.builder()
                        .errorMessage(exception.getMessage())
                        .build())
                .build();
    }
}
