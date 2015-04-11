package net.robi42.boot.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.robi42.boot.util.ErrorResponseBody;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public @Provider @Slf4j class JsonExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    public @Override Response toResponse(JsonProcessingException exception) {
        log.warn("JSON processing failed", exception);
        val body = new ErrorResponseBody(exception.getMessage());
        return Response.status(422).entity(body).build();
        // -> Unprocessable Entity
    }
}
