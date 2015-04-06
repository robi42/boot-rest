package net.robi42.boot.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.robi42.boot.dao.MessageRepository;
import net.robi42.boot.domain.Message;
import net.robi42.boot.search.ElasticsearchProvider;
import net.robi42.boot.search.SearchHitDto;
import net.robi42.boot.util.BootRestException;
import org.elasticsearch.ElasticsearchException;
import org.hibernate.validator.constraints.NotEmpty;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

@Api(value = MessageResource.BASE_PATH, description = "CRUD & Search")
@Path(MessageResource.BASE_PATH)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public @Slf4j class MessageResource {
    public static final String BASE_PATH = "messages";
    private static final String NOT_FOUND_FORMAT = "Message with ID '%s' not found";

    private final @NonNull MessageRepository repository;
    private final @NonNull ElasticsearchProvider searchProvider;

    @ApiOperation("Create a new message")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public @POST Response createMessage(@NotNull @Valid Message.Input payload) {
        final Message messageToPersist = Message.builder()
                .id(randomUUID().toString())
                .lastModifiedAt(new Date())
                .body(payload.getBody())
                .build();
        final Message persistedMessage = repository.save(messageToPersist);
        final String messagePath = String.format("%s/%s", BASE_PATH, persistedMessage.getId());
        return Response.created(URI.create(messagePath))
                .entity(persistedMessage)
                .build();
    }

    @ApiOperation("Get all messages")
    @Produces(MediaType.APPLICATION_JSON)
    public @GET List<Message> getMessages() {
        final List<Message> allMessages = newArrayList(repository.findAll());
        log.debug("Number of messages to serve: {}", allMessages.size());
        return allMessages;
    }

    @ApiOperation("Get a message by ID")
    @Produces(MediaType.APPLICATION_JSON)
    public @GET @Path("/{messageId}") Message getMessage(@NotNull @PathParam("messageId") UUID messageId) {
        final Message message = repository.findOne(messageId.toString());
        if (message != null) {
            return message;
        }
        throw new BootRestException(Response.Status.NOT_FOUND, String.format(NOT_FOUND_FORMAT, messageId));
    }

    @ApiOperation("Update a message")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public @PUT @Path("/{messageId}") Message updateMessage(@NotNull @PathParam("messageId") UUID messageId,
                                                            @NotNull @Valid Message.Input payload) {
        final Message messageToUpdate = repository.findOne(messageId.toString());
        if (messageToUpdate == null) {
            throw new BootRestException(Response.Status.NOT_FOUND, String.format(NOT_FOUND_FORMAT, messageId));
        }
        messageToUpdate.setBody(payload.getBody());
        messageToUpdate.setLastModifiedAt(new Date());
        return repository.save(messageToUpdate);
    }

    @ApiOperation("Delete a message")
    public @DELETE @Path("/{messageId}") void deleteMessage(@NotNull @PathParam("messageId") UUID messageId) {
        repository.delete(messageId.toString());
    }

    @ApiOperation("Search for messages by body content")
    @Produces(MediaType.APPLICATION_JSON)
    public @GET @Path("/search") List<SearchHitDto> search(@NotEmpty @QueryParam("q") String term) {
        try {
            return searchProvider.search(Message.INDEX_NAME, matchPhrasePrefixQuery("body", term));
        } catch (ElasticsearchException e) {
            throw new BootRestException(Response.Status.PRECONDITION_FAILED,
                    String.format("Searching for '%s' failed; %s", term, e.getMessage()));
        }
    }
}
