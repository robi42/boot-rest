package net.robi42.boot.rest;

import com.google.common.collect.ImmutableSet;
import net.robi42.boot.domain.ErrorDto;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class NotFoundWebException extends WebApplicationException {

    public NotFoundWebException(String message) {
        super(message, Response.status(NOT_FOUND).entity(ImmutableSet.of(new ErrorDto(message))).build());
    }
}
