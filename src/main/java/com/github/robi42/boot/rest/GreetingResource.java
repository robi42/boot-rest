package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    public List<Message> getGreetings() {
        log.debug("Hi from `GET /greetings`");
        return repository.findAll();
    }
}
