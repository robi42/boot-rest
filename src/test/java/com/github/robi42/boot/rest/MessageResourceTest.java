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

import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageResourceTest extends JerseyTest {
    private static final String MESSAGE_RESOURCE_PATH = "/messages";
    private static final String MESSAGE_ID = randomUUID().toString();

    @Override
    protected Application configure() {
        final MessageRepository repositoryMock = mock(MessageRepository.class);
        final Message messageMock = Message.builder().id(MESSAGE_ID).build();
        when(repositoryMock.findAll()).thenReturn(ImmutableList.of(messageMock));
        when(repositoryMock.findOne(MESSAGE_ID)).thenReturn(messageMock);

        final ResourceConfig resourceConfig = new ResourceConfig();
        // TODO: mock/inject bean validator or wire up Spring app context
        resourceConfig.register(new MessageResource(repositoryMock, null));
        return resourceConfig;
    }

    @Test
    public void shouldRespondSuccessfully() throws Exception {
        final Response response = target(MESSAGE_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(Response.class);

        assertThat(response.getStatusInfo().getFamily())
                .isEqualTo(Response.Status.Family.SUCCESSFUL);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void shouldRespondWithMessages() throws Exception {
        final List<Message> messages = target(MESSAGE_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Message>>() {});

        assertThat(messages).isNotEmpty();
        assertThat(messages.size()).isEqualTo(1);
    }

    @Test
    public void shouldRespondWithMessageById() throws Exception {
        final Message message = target(String.format("%s/%s", MESSAGE_RESOURCE_PATH, MESSAGE_ID))
                .request(APPLICATION_JSON_TYPE)
                .get(Message.class);

        assertThat(message).isNotNull();
        assertThat(message.getId()).isEqualTo(MESSAGE_ID);
    }
}
