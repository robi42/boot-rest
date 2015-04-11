package net.robi42.boot.rest;

import lombok.val;
import net.robi42.boot.util.ErrorResponseBody;
import net.robi42.boot.util.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public @Provider class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    public @Override Response toResponse(NotFoundException exception) {
        val body = new ErrorResponseBody(exception.getMessage());
        return Response.status(NOT_FOUND).entity(body).build();
    }
}
