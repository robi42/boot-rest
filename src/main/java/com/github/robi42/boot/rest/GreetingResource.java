package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@Path("/greetings")
public class GreetingResource {
    private final MessageRepository repository;

    @Inject
    public GreetingResource(final MessageRepository repository) {
        this.repository = repository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> greetings() {
        log.debug("Hi from `GET /greetings`");
        return repository.findAll();
    }

    @GET
    @Path("/{greetingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Message greeting(final @PathParam("greetingId") UUID greetingId) {
        final Optional<Message> greeting = repository.findById(greetingId);

        if (greeting.isPresent()) {
            return greeting.get();
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
}
