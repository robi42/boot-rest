package com.github.robi42.boot.rest;

import com.github.robi42.boot.domain.Message;
import com.github.robi42.boot.util.IntegrationTestBase;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.Assertions.assertThat;

public class MessageResourceTest extends IntegrationTestBase {
    private static final String MESSAGE_RESOURCE_PATH = "/messages";

    @Test
    public void shouldRespondSuccessfully() throws Exception {
        final Response response = restApi.path(MESSAGE_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(Response.class);

        assertThat(response.getStatusInfo().getFamily())
                .isEqualTo(Response.Status.Family.SUCCESSFUL);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @Test
    public void shouldRespondWithMessages() throws Exception {
        final List<Message> messages = restApi.path(MESSAGE_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Message>>() {});

        assertThat(messages).isNotEmpty();
        assertThat(messages.size()).isEqualTo(3);
        assertThat(messages.get(0).getBody()).isEqualTo("Foo");
    }
}
