package com.github.robi42.boot.rest;

import com.github.robi42.boot.domain.Message;
import com.github.robi42.boot.search.SearchHitDto;
import com.github.robi42.boot.util.IntegrationTestBase;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.Assertions.assertThat;

public class MessageResourceTest extends IntegrationTestBase {
    private static final String MESSAGE_RESOURCE_PATH = "/messages";

    @Test
    public void shouldRespondSuccessfully() throws Exception {
        final Response response = restApi.path(MESSAGE_RESOURCE_PATH)
                .request(APPLICATION_JSON_TYPE).get();

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

        final List<String> messageBodies = messages.stream()
                .map(Message::getBody).collect(toList());
        assertThat(messageBodies).contains("Foo");
    }

    @Test
    public void shouldFindMessageViaSearch() throws Exception {
        final List<SearchHitDto> hits = restApi.path(MESSAGE_RESOURCE_PATH + "/search")
                .queryParam("q", "bar")
                .request(APPLICATION_JSON_TYPE)
                .get(new GenericType<List<SearchHitDto>>() {});
        assertThat(hits.size()).isEqualTo(1);

        final SearchHitDto hit = hits.get(0);
        assertThat(hit.getScore()).isPositive();

        final Map<String, Object> source = hit.getSource();
        assertThat(source.get("id")).isEqualTo(hit.getId());
        assertThat(source.get("body")).isEqualTo("Bar");
    }
}
