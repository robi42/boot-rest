package com.github.robi42.boot.rest;

import com.github.robi42.boot.dao.GreetingRepository;
import com.github.robi42.boot.domain.Message;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    public void shouldRespondSuccessfully() {
        final Response response = target(GREETING_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(Response.class);

        assertThat(response.getStatusInfo().getFamily())
                .isEqualTo(Response.Status.Family.SUCCESSFUL);
    }

    @Test
    public void shouldRespondWithGreetings() {
        final List<Message> greetings = target(GREETING_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Message>>() { });

        assertThat(greetings).isNotEmpty();
        assertThat(greetings.size()).isEqualTo(1);
        assertThat(greetings.get(0).getBody())
                .startsWith(String.format("Hello %s! The time is ", TEST_NAME));
    }
}
