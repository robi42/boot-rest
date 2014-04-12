package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.domain.Message;
import com.github.robi42.boot.domain.util.BeanValidator;
import com.github.robi42.boot.rest.util.BootRestException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;

@Slf4j
@Path("/messages")
@Api(value = "messages", description = "CRUD")
public class MessageResource {
    private final MessageRepository repository;
    private final BeanValidator validator;

    @Inject
    public MessageResource(@SuppressWarnings("SpringJavaAutowiringInspection")
                           final MessageRepository repository, final BeanValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new message")
    public Response createMessage(final Message.Input payload) {
        validate(payload);

        final Message messageToPersist = Message.builder()
                .id(randomUUID().toString())
                .lastModifiedAt(LocalDateTime.now())
                .body(payload.getBody())
                .build();
        final Message persistedMessage = repository.save(messageToPersist);
        final String messagePath = String.format("/api/messages/%s", persistedMessage.getId());
        return Response.created(URI.create(messagePath))
                .entity(persistedMessage)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Get all messages")
    public List<Message> getMessages() {
        final List<Message> allMessages = newArrayList(repository.findAll());
        log.debug("Number of messages to serve: {}", allMessages.size());
        return allMessages;
    }

    @GET
    @Path("/{messageId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Get a message by ID")
    public Message getMessage(final @PathParam("messageId") UUID messageId) {
        final Optional<Message> messageOptional = Optional.ofNullable(repository.findOne(messageId.toString()));
        if (messageOptional.isPresent()) {
            return messageOptional.get();
        }
        throw new BootRestException(Response.Status.NOT_FOUND,
                String.format("Message with ID '%s' not found", messageId));
    }

    @PUT
    @Path("/{messageId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Update a message")
    public Message updateMessage(final @PathParam("messageId") UUID messageId, final Message.Input payload) {
        validate(payload);

        final Optional<Message> messageOptional = Optional.ofNullable(repository.findOne(messageId.toString()));
        if (!messageOptional.isPresent()) {
            throw new BootRestException(Response.Status.NOT_FOUND,
                    String.format("Message with ID '%s' not found", messageId));
        }
        final Message message = messageOptional.get();
        message.setBody(payload.getBody());
        message.setLastModifiedAt(LocalDateTime.now());
        return repository.save(message);
    }

    @DELETE
    @Path("/{messageId}")
    @ApiOperation("Delete a message")
    public void deleteMessage(final @PathParam("messageId") UUID messageId) {
        repository.delete(messageId.toString());
    }

    private void validate(final Message.Input payload) {
        try {
            validator.validate(payload);
        } catch (ValidationException e) {
            log.warn("{} Payload: {}", e.getMessage(), payload);
            throw new BootRestException(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
}
