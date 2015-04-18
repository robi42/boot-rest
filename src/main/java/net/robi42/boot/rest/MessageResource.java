package net.robi42.boot.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.robi42.boot.domain.MessageDto;
import net.robi42.boot.search.ElasticsearchProvider.SearchHitDto;
import net.robi42.boot.service.MessageService;
import org.hibernate.validator.constraints.NotEmpty;

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
import java.util.List;
import java.util.UUID;

@Api(value = MessageResource.BASE_PATH, description = "CRUD & Search")
@Path(MessageResource.BASE_PATH) @RequiredArgsConstructor
public @Slf4j class MessageResource {
    public static final String BASE_PATH = "messages";

    private final @NonNull MessageService service;

    @ApiOperation("Create a new message")
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    public @POST Response create(@NotNull @Valid MessageDto.Input payload) {
        val message = service.create(payload.getBody());
        val path = String.format("%s/%s", BASE_PATH, message.getId());
        return Response.created(URI.create(path))
                .entity(message).build();
    }

    @ApiOperation("Get all messages")
    @Produces(MediaType.APPLICATION_JSON)
    public @GET List<MessageDto> getAll() {
        val messages = service.getAll();
        log.debug("Number of messages to serve: {}", messages.size());
        return messages;
    }

    @ApiOperation("Get a message by ID")
    @Produces(MediaType.APPLICATION_JSON)
    public @GET @Path("/{id}") MessageDto get(@PathParam("id") UUID id) {
        return service.get(id).orElseThrow(() ->
                new NotFoundWebException(notFoundMessageWith(id)));
    }

    @ApiOperation("Update a message")
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    public @PUT @Path("/{id}") MessageDto update(@PathParam("id") UUID id,
                                                 @NotNull @Valid MessageDto.Input payload) {
        return service.update(id, payload.getBody())
                .orElseThrow(() -> new NotFoundWebException(notFoundMessageWith(id)));
    }

    @ApiOperation("Delete a message")
    public @DELETE @Path("/{id}") void delete(@PathParam("id") UUID id) {
        service.delete(id);
    }

    @ApiOperation("Search for messages by body content")
    @Produces(MediaType.APPLICATION_JSON)
    public @GET @Path("/search") List<SearchHitDto> search(@NotEmpty @QueryParam("q") String term) {
        return service.search(term);
    }

    private String notFoundMessageWith(UUID id) {
        return String.format("Message with ID '%s' not found", id);
    }
}
