package net.robi42.boot.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.robi42.boot.util.ErrorResponseBody;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public @Provider class JsonExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    public @Override Response toResponse(JsonProcessingException exception) {
        return Response.status(422) // -> Unprocessable Entity
                .entity(new ErrorResponseBody(exception.getMessage()))
                .build();
    }
}
