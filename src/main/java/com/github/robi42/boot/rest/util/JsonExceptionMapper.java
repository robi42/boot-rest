package com.github.robi42.boot.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class JsonExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    @Override
    public Response toResponse(final JsonProcessingException exception) {
        return Response.status(BAD_REQUEST)
                .entity(ErrorResponseBody.builder()
                        .message(exception.getMessage())
                        .build())
                .build();
    }
}
