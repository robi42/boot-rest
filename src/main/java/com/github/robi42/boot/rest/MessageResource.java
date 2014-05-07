package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.domain.Message;
import com.github.robi42.boot.rest.util.BootRestException;
import com.github.robi42.boot.search.ElasticsearchProvider;
import com.github.robi42.boot.search.SearchHitDto;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

@Slf4j
@Path("/messages")
@Api(value = "messages", description = "CRUD & Search")
public class MessageResource {
    private static final String NOT_FOUND_FORMAT = "Message with ID '%s' not found";

    private final MessageRepository repository;
    private final ElasticsearchProvider searchProvider;

    @Inject
    public MessageResource(@SuppressWarnings("SpringJavaAutowiringInspection")
                           final MessageRepository repository,
                           final ElasticsearchProvider searchProvider) {
        this.repository = repository;
        this.searchProvider = searchProvider;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new message")
    public Response createMessage(final @Valid Message.Input payload) {
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
        throw new BootRestException(Response.Status.NOT_FOUND, String.format(NOT_FOUND_FORMAT, messageId));
    }

    @PUT
    @Path("/{messageId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Update a message")
    public Message updateMessage(final @PathParam("messageId") UUID messageId, final @Valid Message.Input payload) {
        final Optional<Message> messageOptional = Optional.ofNullable(repository.findOne(messageId.toString()));
        if (!messageOptional.isPresent()) {
            throw new BootRestException(Response.Status.NOT_FOUND, String.format(NOT_FOUND_FORMAT, messageId));
        }
        final Message messageToUpdate = messageOptional.get();
        messageToUpdate.setBody(payload.getBody());
        messageToUpdate.setLastModifiedAt(LocalDateTime.now());
        return repository.save(messageToUpdate);
    }

    @DELETE
    @Path("/{messageId}")
    @ApiOperation("Delete a message")
    public void deleteMessage(final @PathParam("messageId") UUID messageId) {
        repository.delete(messageId.toString());
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Search for messages by body content")
    public List<SearchHitDto> search(final @QueryParam("q") String term) {
        try {
            return searchProvider.search(Message.INDEX_NAME, matchPhrasePrefixQuery("body", term));
        } catch (final ElasticsearchException e) {
            throw new BootRestException(Response.Status.PRECONDITION_FAILED,
                    String.format("Searching for '%s' failed; %s", term, e.getMessage()));
        }
    }
}
