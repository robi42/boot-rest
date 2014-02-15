package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.MessageRepository;
import com.github.robi42.boot.domain.Message;
import com.google.common.collect.ImmutableList;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GreetingResourceTest extends JerseyTest {
    private static final String GREETING_RESOURCE_PATH = "/greetings";
    private static final UUID GREETING_ID = randomUUID();

    @Override
    protected Application configure() {
        final MessageRepository repositoryMock = mock(MessageRepository.class);
        final Message greetingMock = Message.builder().id(GREETING_ID).build();
        when(repositoryMock.findAll()).thenReturn(ImmutableList.of(greetingMock));
        when(repositoryMock.findById(GREETING_ID)).thenReturn(Optional.of(greetingMock));

        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new GreetingResource(repositoryMock));
        return resourceConfig;
    }

    @Test
    public void shouldRespondSuccessfully() throws Exception {
        final Response response = target(GREETING_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(Response.class);

        assertThat(response.getStatusInfo().getFamily())
                .isEqualTo(Response.Status.Family.SUCCESSFUL);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void shouldRespondWithGreetings() throws Exception {
        final List<Message> greetings = target(GREETING_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Message>>() { });

        assertThat(greetings).isNotEmpty();
        assertThat(greetings.size()).isEqualTo(1);
    }

    @Test
    public void shouldRespondWithGreetingById() throws Exception {
        final Message greeting = target(String.format("%s/%s", GREETING_RESOURCE_PATH, GREETING_ID))
                .request(APPLICATION_JSON_TYPE)
                .get(Message.class);

        assertThat(greeting).isNotNull();
        assertThat(greeting.getId()).isEqualTo(GREETING_ID);
    }
}
