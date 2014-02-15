package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.GreetingRepository;
import com.github.robi42.boot.domain.Message;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.fest.assertions.Assertions.assertThat;

public class GreetingResourceTest extends JerseyTest {
    private static final String TEST_NAME = "Barbert";
    private static final String GREETING_RESOURCE_PATH = "/greetings";

    @Override
    protected Application configure() {
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new GreetingResource(new GreetingRepository(TEST_NAME)));
        return resourceConfig;
    }

    @Test
    public void shouldRespondSuccessfully() throws Exception {
        final Response response = target(GREETING_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(Response.class);

        assertThat(response.getStatusInfo().getFamily())
                .isEqualTo(Response.Status.Family.SUCCESSFUL);
    }

    @Test
    public void shouldRespondWithGreetings() throws Exception {
        final List<Message> greetings = target(GREETING_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Message>>() { });
        assertThat(greetings).isNotEmpty();
        assertThat(greetings.size()).isEqualTo(2);

        final Message greeting = greetings.get(0);
        final String lastModifiedAtFormatted = new DateTime(greeting.getLastModifiedAt())
                .toString("yyyy-MM-dd HH:mm:ss.SSS");
        assertThat(greeting.getBody())
                .isEqualTo(String.format("Hello, %s! The time is: %s", TEST_NAME, lastModifiedAtFormatted));
    }

    @Test
    public void shouldRespondWithGreetingById() throws Exception {
        final List<Message> greetings = target(GREETING_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Message>>() { });
        final UUID greetingId = greetings.get(0).getId();
        final Message greeting = target(String.format("%s/%s", GREETING_RESOURCE_PATH, greetingId))
                .request(APPLICATION_JSON_TYPE)
                .get(Message.class);

        assertThat(greeting).isNotNull();
        assertThat(greeting.getId()).isEqualTo(greetingId);
        assertThat(greeting.getBody()).isNotEmpty();
    }
}
