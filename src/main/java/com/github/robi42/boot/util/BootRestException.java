package com.github.robi42.boot.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class BootRestException extends WebApplicationException {

    public BootRestException(final Response.Status status, final String message) {
        super(Response.status(status)
                .entity(new ErrorResponseBody(message))
                .build());
    }
}
